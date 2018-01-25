// Vielä tekemättä:
// -aloitusnäkymä (laitoin tähänkin nyt jonkun vähän eri)
// -Kysely-threadien tappaminen (Ei ehkä tarvii tappaa? Suoritus loppuu itsestään kun tulee tehtyä?)
// -kommentointia ehkä lisättävä, vähintään käytävä läpi että on ok
// -toiseksi viimeisen pallukan juttuja voi miettiä että tuliko tehtyä järkevästi
// -Muuten tämä varmaan tulikin valmiiksi?

    import java.lang.Math;
// Kartankatseluohjelman graafinen käyttöliittymä
    import javax.swing.*;
    import javax.swing.event.*;
    import java.awt.*;
    import java.awt.event.*;
    import java.net.*;
    
    // XML-jutut
    import javax.xml.parsers.*;
    import org.xml.sax.*;
    import org.xml.sax.helpers.*;
    import java.util.*;
    import java.io.*;

     
    public class MapDialog extends JFrame { 
      
      //Näihin kerätään karttakerrosten nimet ja titlet XML-tiedostosta
      ArrayList<String> layerNames;
      ArrayList<String> layerTitles;
      
      // getMap-kyselyn numeroparametrit järjestyksessä xmin, ymin, xmax, ymax, leveys, korkeus. Ei ehkä loogisin 
      // paikka tälle mutta on monen sisäluokan käytössä niin helpompaa näin. Oletusnäkymän luvut.
      int[] rajat = {-180,-90,180,90, 953, 480};
      
      // Käyttöliittymän komponentit
      private JLabel imageLabel = new JLabel();
      private JPanel leftPanel = new JPanel();
      private JButton refreshB = new JButton("Päivitä");
      private JButton leftB = new JButton("<");
      private JButton rightB = new JButton(">");
      private JButton upB = new JButton("^");
      private JButton downB = new JButton("v");
      private JButton zoomInB = new JButton("+10%");
      private JButton zoomOutB = new JButton("-10%");
      private JButton nollaa = new JButton("nollaa");   // lisäsin
      
      // ----------------------------------------------------------------------Konstruktori MapDialog()
      public MapDialog() throws Exception {
        
        // Alustetaan ArrayListit joihin karttakerrosten nimet ja titlet kerätään
        layerNames = new ArrayList<String>();
        layerTitles = new ArrayList<String>();
        
        // Valmistele ikkuna ja lisää siihen komponentit
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
     
        // Aloitusnäkymä
        imageLabel.setIcon(new ImageIcon(new URL("http://demo.mapserver.org/cgi-bin/wms?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&BBOX=-180,-90,180,90&SRS=EPSG:4326&WIDTH=953&HEIGHT=480&LAYERS=continents,country_bounds&STYLES=&FORMAT=image/png&TRANSPARENT=true")));
     
        add(imageLabel, BorderLayout.EAST);
     
        ButtonListener bl = new ButtonListener();
        refreshB.addActionListener(bl);  
        leftB.addActionListener(bl);
        rightB.addActionListener(bl);
        upB.addActionListener(bl);
        downB.addActionListener(bl);
        zoomInB.addActionListener(bl);
        zoomOutB.addActionListener(bl);
        nollaa.addActionListener(bl);
     
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        leftPanel.setMaximumSize(new Dimension(100, 600));
        
        
        // getCapabilities-kyselyn URL
        URL contentsURL = new URL("http://demo.mapserver.org/cgi-bin/wms?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetCapabilities");
        
        // Luodaan XML:n käsittelemiseen tarvittavat oliot
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        UserHandler userHandler = new UserHandler(layerNames, layerTitles);
        
        // Haetaan ja käydään XML-tiedosto läpi, poimitaan halutut tiedot (tapahtuu oikeastaan UserHandlerissa)
        try {
          saxParser.parse(contentsURL.openStream(), userHandler);
        } catch (Exception e){
          System.out.println(e.getMessage());
        }
        
        // Testitulostuksia:
        for (String name : layerNames){
          System.out.println(name);
        }
        for (String title : layerTitles){
          System.out.println(title);
        }
        
        // Luodaan käyttöliittymään CheckBox-oliot kutakin karttakerrosta varten:
        for (int i = 0; i<layerNames.size(); i++){
          leftPanel.add(new LayerCheckBox(layerNames.get(i), layerTitles.get(i), false));
        }
     
        leftPanel.add(refreshB);
        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(leftB);
        leftPanel.add(rightB);
        leftPanel.add(upB);
        leftPanel.add(downB);
        leftPanel.add(zoomInB);
        leftPanel.add(zoomOutB);
        leftPanel.add(nollaa);
     
        add(leftPanel, BorderLayout.WEST);
     
        pack();
        setVisible(true);
      } // Konstruktori MapDialog()
     
      
      // -------------------------------------------------------------------------main
      public static void main(String[] args) throws Exception {
        new MapDialog();
      } // main
      
      // -------------------------------------------------------------------------updateImage()
      // Luodaan uusi kysely ja käynnistetään se omana threadinaan
      public void updateImage() throws Exception {
        Kysely kysely = new Kysely(rajat);
        kysely.start();
      }
      
      
      
      
      // ------------------------------------------------------------------------ ButtonListener
      // Kontrollinappien kuuntelija
      private class ButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
          // Päivitä
          if(e.getSource() == refreshB) {
            try{
              updateImage();
            } catch(Exception ex){
              ex.printStackTrace();
            }
          }
          // Vasemmalle
          if(e.getSource() == leftB) {
            rajat[0] = rajat[0]-18;
            rajat[2] = rajat[2]-18;
            try{
              updateImage();
            } catch(Exception ex){
              ex.printStackTrace();
            }
          }
          // Oikealle
          if(e.getSource() == rightB) {
            rajat[0] = rajat[0]+18;
            rajat[2] = rajat[2]+18;
            try{
              updateImage();
            } catch(Exception ex){
              ex.printStackTrace();
            }
          }
          // Ylös
          if(e.getSource() == upB) {
            rajat[1] = rajat[1]+9;
            rajat[3] = rajat[3]+9;
            try{
              updateImage();
            } catch(Exception ex){
              ex.printStackTrace();
            }
          }
          // Alas
          if(e.getSource() == downB) {
            rajat[1] = rajat[1]-9;
            rajat[3] = rajat[3]-9;
            try{
              updateImage();
            } catch(Exception ex){
              ex.printStackTrace();
            }
          }
          // Lähemmäs 10%
          if(e.getSource() == zoomInB) {  
            // Kartan koko pysyy samana, joten rajat[4] ja rajat[5] vakiona
            // Miltä väliltä kuva näytetään: 
            rajat[0] = (int) (rajat[0] * 0.90);
            rajat[1] = (int) (rajat[1] * 0.90);
            rajat[2] = (int) (rajat[2] * 0.90);
            rajat[3] = (int) (rajat[3] * 0.90);
            
            try{
              updateImage();
            } catch(Exception ex){
              ex.printStackTrace();
            }
          }
          // Kauemmas 10%
          if(e.getSource() == zoomOutB) {
            rajat[0] = (int) (rajat[0] * 1.10);
            rajat[1] = (int) (rajat[1] * 1.10);
            rajat[2] = (int) (rajat[2] * 1.10);
            rajat[3] = (int) (rajat[3] * 1.10);

            try{
              updateImage();
            } catch(Exception ex){
              ex.printStackTrace();
            }
          }
          
         //Kartan sovitus ja keskitys alkuperäiseen asemaan.
          if(e.getSource() == nollaa) {
            rajat[4] = 953;
            rajat[5] = 480;
            rajat[0] = -180;
            rajat[1] = -90;
            rajat[2] = 180;
            rajat[3] = 90;
            try{
              updateImage();
            } catch(Exception ex){
              ex.printStackTrace();
            }
          }
        }// actionPerformed
      }// ButtonListener
      
     
      // -------------------------------------------------------------------------- Kysely 
      // Sisäluokka, joka omassa threadissaan muodostaa getMap()-kyselyn 
      // URL-osoitteen ja saatuaan kuvan päivittää kuvan
      private class Kysely extends Thread{
        // Tähän tallennetaan parametrina saatu viittaus taulukkoon josta löytyvät lukuparametrit
        int[] rajat;

        // Tähän Stringiin kootaan osista lopullinen kysely  
        String getMap;
        
        // Näistä pätkistä kootaan konstruktorissa kysely
        String alku;
        String rajatString;
        String keskiosa;
        String leveysString;
        String korkeusString;
        String layersString;
        String loppu; 
        
        // Konstruktori
        public Kysely(int[] rajat){  
          this.rajat = rajat;
        }
        
        // ------------------------------------------------------------------------ run()
        // Jokainen kysely luodaan ja tehdään omassa threadissaan:
        public void run(){
          alku = "http://demo.mapserver.org/cgi-bin/wms?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&";
          rajatString = "BBOX="; // + xmin + "," + ymin + "," + xmax + "," + ymax
          keskiosa = "&SRS=EPSG:4326&";
          leveysString = "WIDTH="; // +leveys, eli rajat[4]
          korkeusString = "&HEIGHT="; // +korkeus, eli rajat[5]
          layersString = "&LAYERS=";
          loppu = "&STYLES=&FORMAT=image/png&TRANSPARENT=true";
          
          // Lisätään tekstin joukkoon rajat
          for (int i = 0; i<rajat.length-2; i++){
            rajatString = rajatString + rajat[i];
            if (i<rajat.length-3){
              rajatString = rajatString + ",";
            }
          }
          
          // Tutkitaan, mitkä valintalaatikot on valittu, ja
          // kerätään layersString:iin pilkulla erotettu lista valittujen kerrosten
          // nimistä (käytetään haettaessa uutta kuvaa)
          Component[] components = leftPanel.getComponents();
          for(Component com:components) {
            if(com instanceof LayerCheckBox)
              if(((LayerCheckBox)com).isSelected()) layersString = layersString + com.getName() + ",";
          }
          if (layersString.endsWith(",")) layersString = layersString.substring(0, layersString.length() - 1);
          
          // Kootaan kysely ja lisätään väliin loput luvut
          getMap = alku + rajatString + keskiosa + leveysString + rajat[4] + korkeusString + rajat[5] + layersString + loppu;
          
          // Testitulostus
          System.out.println(getMap + " - Tämä tulostuu Kyselyn run()-metodista");
          
          // Tehdään kysely ja päivitetään kuva
          try{
          imageLabel.setIcon(new ImageIcon(new URL (getMap)));
          } catch (Exception e){
            System.out.println(e.getMessage());
          }
        }//run
        
      } // Kysely
      
      // Valintalaatikko, joka muistaa karttakerroksen nimen
      private class LayerCheckBox extends JCheckBox {
        private String name = "";
        public LayerCheckBox(String name, String title, boolean selected) {
          super(title, null, selected);
          this.name = name;
        }
        public String getName() { return name; }
      } 
      
      
      // Oma handleriluokka, joka osaa poimia käsittelemästään (juuri oikeanlaisesta) XML-tiedostosta karttakerrosten
      // nimet ja titlet. Saa parametrina viittaukset MapDialogin Arraylisteihin, joihin lisää keräämänsä Stringit.
      private class UserHandler extends DefaultHandler{
        
        int layers = 0;
        boolean capability;
        boolean layer;
        boolean name;
        boolean title;
        ArrayList<String> names;
        ArrayList<String> titles;
        
        // KONSTRUKTORI - Saa viittaukset MapDialogin Arraylisteihin (siellä layerNames ja 
        // layerTitles, täällä names ja titles)
        public UserHandler (ArrayList<String> names, ArrayList<String> titles){
          capability = false;
          layer = false;
          name = false;
          title = false;
          this.names = names;
          this.titles = titles;
        }
        
        // ---------------------------------------------------------------------- startElement()
        // Kohdatessaan elementin XML-tiedostossa parser kutsuu tätä metodia. 
        // Mikäli elementti täyttää ehdon, vaihdetaan
        // booleanin arvoa. Näillä valikoidaan mitkä nimet poimitaan 
        // ja lisätään Arraylisteihin.
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException{
          if (qName.equalsIgnoreCase("capability")){
            capability = true;
          } 
          if (qName.equalsIgnoreCase("layer")){
            layers++;
            if (layers >1){
              layer = true;
            }
          }
          if (qName.equalsIgnoreCase("name")){
            name = true;
          }
          if (qName.equalsIgnoreCase("title")){
            title = true;
          }
        }
        
        // --------------------------------------------------------------------- endElement()
        // Kun elementti saatu käsiteltyä, parser kutsuu tätä. Muutetaan 
        // booleaneja falseksi, jotta vääriä kohtia ei lisätä Arraylisteihin.
        @Override
        public void endElement (String uri, String localName, String qName) throws SAXException {
          if (qName.equalsIgnoreCase("layer")) {
            layers--;
            layer = false;
          }
          if (qName.equalsIgnoreCase("capability")) {
            capability = false;
          }
        }
        
        // -------------------------------------------------------------------- characters()
        // Kun parser kohtaa tekstiä, kutsuu tätä. Mikäli ehdot (ylläolevat 
        // metodit säätelevät booleanien avulla) täyttyvät, lisätään tekstin 
        // pätkä Arraylistiin.
        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
          if (capability && layer && name){
            //System.out.println(new String(ch, start, length));
            names.add(new String(ch, start, length));
            name = false;
          } else if (capability && layer && title){
            //System.out.println(new String(ch, start, length));
            titles.add(new String(ch, start, length));
            title = false;
            layer = false;
          }
        }
      } // UserHandler
      
    } // MapDialog
    
    
