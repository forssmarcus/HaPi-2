// Viel‰ tekem‰tt‰:
// -aloitusn‰kym‰ (laitoin t‰h‰nkin nyt jonkun v‰h‰n eri)
// -zoomaus v‰‰rist‰‰
// -Kysely-threadien tappaminen (Ei ehk‰ tarvii tappaa? Suoritus loppuu itsest‰‰n kun tulee tehty‰?)
// -kommentointia ehk‰ lis‰tt‰v‰, v‰hint‰‰n k‰yt‰v‰ l‰pi ett‰ on ok
// -toiseksi viimeisen pallukan juttuja voi mietti‰ ett‰ tuliko tehty‰ j‰rkev‰sti
// -Muuten t‰m‰ varmaan tulikin valmiiksi?


// Kartankatseluohjelman graafinen k‰yttˆliittym‰
     
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
      
      //N‰ihin ker‰t‰‰n karttakerrosten nimet ja titlet XML-tiedostosta
      ArrayList<String> layerNames;
      ArrayList<String> layerTitles;
      
      // getMap-kyselyn numeroparametrit j‰rjestyksess‰ xmin, ymin, xmax, ymax, leveys, korkeus. Ei ehk‰ loogisin 
      // paikka t‰lle mutta on monen sis‰luokan k‰ytˆss‰ niin helpompaa n‰in. Oletusn‰kym‰n luvut.
      int[] rajat = {-180,-90,180,90, 953, 480};
      
      // K‰yttˆliittym‰n komponentit
      
      private JLabel imageLabel = new JLabel();
      private JPanel leftPanel = new JPanel();
      
      private JButton refreshB = new JButton("P‰ivit‰");
      private JButton leftB = new JButton("<");
      private JButton rightB = new JButton(">");
      private JButton upB = new JButton("^");
      private JButton downB = new JButton("v");
      private JButton zoomInB = new JButton("+");
      private JButton zoomOutB = new JButton("-");
      
      // Konstruktori
      public MapDialog() throws Exception {
        
        // Alustetaan ArrayListit joihin karttakerrosten nimet ja titlet ker‰t‰‰n
        layerNames = new ArrayList<String>();
        layerTitles = new ArrayList<String>();
        
        // Valmistele ikkuna ja lis‰‰ siihen komponentit
     
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
     
        // Aloitusn‰kym‰
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
     
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        leftPanel.setMaximumSize(new Dimension(100, 600));
        
        
        // getCapabilities-kyselyn URL
        URL contentsURL = new URL("http://demo.mapserver.org/cgi-bin/wms?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetCapabilities");
        
        // Luodaan XML:n k‰sittelemiseen tarvittavat oliot
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        UserHandler userHandler = new UserHandler(layerNames, layerTitles);
        
        // Haetaan ja k‰yd‰‰n XML-tiedosto l‰pi, poimitaan halutut tiedot (tapahtuu oikeastaan UserHandlerissa)
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
        
        // Luodaan k‰yttˆliittym‰‰n CheckBox-oliot kutakin karttakerrosta varten:
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
     
        add(leftPanel, BorderLayout.WEST);
     
        pack();
        setVisible(true);
      } // Konstruktori
     
      public static void main(String[] args) throws Exception {
        new MapDialog();
      } // main
      
     
      // Luodaan uusi kysely ja k‰ynnistet‰‰n se omana threadinaan
      public void updateImage() throws Exception {
        Kysely kysely = new Kysely(rajat);
        kysely.start();
      }
      
      // Kontrollinappien kuuntelija
      private class ButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
          // P‰ivit‰
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
          // Ylˆs
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
          // L‰hemm‰s
          if(e.getSource() == zoomInB) {
            rajat[4] = rajat[4]+10;
            rajat[5] = rajat[5]+5;
            
            rajat[0] = rajat[0]+18;
            rajat[1] = rajat[1]+9;
            rajat[2] = rajat[2]-9;
            rajat[3] = rajat[3]-18;
            try{
              updateImage();
            } catch(Exception ex){
              ex.printStackTrace();
            }
          }
          // Kauemmas
          if(e.getSource() == zoomOutB) {
            rajat[4] = rajat[4]-10;
            rajat[5] = rajat[5]-5;
            
            rajat[0] = rajat[0]-18;
            rajat[1] = rajat[1]-9;
            rajat[2] = rajat[2]+18;
            rajat[3] = rajat[3]+9;
            try{
              updateImage();
            } catch(Exception ex){
              ex.printStackTrace();
            }
          }
        }
      }
      
      // Sis‰luokka, joka omassa threadissaan muodostaa getMap()-kyselyn URL-osoitteen ja saatuaan kuvan p‰ivitt‰‰ kuvan
      private class Kysely extends Thread{
        // T‰h‰n tallennetaan parametrina saatu viittaus taulukkoon josta lˆytyv‰t lukuparametrit
        int[] rajat;

        // T‰h‰n Stringiin kootaan osista lopullinen kysely  
        String getMap;
        
        // N‰ist‰ p‰tkist‰ kootaan konstruktorissa kysely
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
        // Jokainen kysely luodaan ja tehd‰‰n omassa threadissaan:
        public void run(){
          alku = "http://demo.mapserver.org/cgi-bin/wms?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&";
          rajatString = "BBOX="; // + xmin + "," + ymin + "," + xmax + "," + ymax
          keskiosa = "&SRS=EPSG:4326&";
          leveysString = "WIDTH="; // +leveys, eli rajat[4]
          korkeusString = "&HEIGHT="; // +korkeus, eli rajat[5]
          layersString = "&LAYERS=";
          loppu = "&STYLES=&FORMAT=image/png&TRANSPARENT=true";
          
          // Lis‰t‰‰n tekstin joukkoon rajat
          for (int i = 0; i<rajat.length-2; i++){
            rajatString = rajatString + rajat[i];
            if (i<rajat.length-3){
              rajatString = rajatString + ",";
            }
          }
          
          // Tutkitaan, mitk‰ valintalaatikot on valittu, ja
          // ker‰t‰‰n layersString:iin pilkulla erotettu lista valittujen kerrosten
          // nimist‰ (k‰ytet‰‰n haettaessa uutta kuvaa)
          Component[] components = leftPanel.getComponents();
          for(Component com:components) {
            if(com instanceof LayerCheckBox)
              if(((LayerCheckBox)com).isSelected()) layersString = layersString + com.getName() + ",";
          }
          if (layersString.endsWith(",")) layersString = layersString.substring(0, layersString.length() - 1);
          
          // Kootaan kysely ja lis‰t‰‰n v‰liin loput luvut
          getMap = alku + rajatString + keskiosa + leveysString + rajat[4] + korkeusString + rajat[5] + layersString + loppu;
          
          // Testitulostus
          System.out.println(getMap + " - T‰m‰ tulostuu Kyselyn run()-metodista");
          
          // Tehd‰‰n kysely ja p‰ivitet‰‰n kuva
          try{
          imageLabel.setIcon(new ImageIcon(new URL (getMap)));
          } catch (Exception e){
            System.out.println(e.getMessage());
          }
        }
        
      } // Kysely
      
      // Valintalaatikko, joka muistaa karttakerroksen nimen
      private class LayerCheckBox extends JCheckBox {
        private String name = "";
        public LayerCheckBox(String name, String title, boolean selected) {
          super(title, null, selected);
          this.name = name;
        }
        public String getName() { return name; }
      } // LayerCheckBox
      
      // Oma handleriluokka, joka osaa poimia k‰sittelem‰st‰‰n (juuri oikeanlaisesta) XML-tiedostosta karttakerrosten
      // nimet ja titlet. Saa parametrina viittaukset MapDialogin Arraylisteihin, joihin lis‰‰ ker‰‰m‰ns‰ Stringit.
      private class UserHandler extends DefaultHandler{
        
        int layers = 0;
        boolean capability;
        boolean layer;
        boolean name;
        boolean title;
        ArrayList<String> names;
        ArrayList<String> titles;
        
        // KONSTRUKTORI - Saa viittaukset MapDialogin Arraylisteihin (siell‰ layerNames ja layerTitles, t‰‰ll‰ names ja titles)
        public UserHandler (ArrayList<String> names, ArrayList<String> titles){
          capability = false;
          layer = false;
          name = false;
          title = false;
          this.names = names;
          this.titles = titles;
        }
        
        // Kohdatessaan elementin XML-tiedostossa parser kutsuu t‰t‰ metodia. Mik‰li elementti t‰ytt‰‰ ehdon, vaihdetaan
        // booleanin arvoa. N‰ill‰ valikoidaan mitk‰ nimet poimitaan ja lis‰t‰‰n Arraylisteihin.
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
        
        // Kun elementti saatu k‰sitelty‰, parser kutsuu t‰t‰. Muutetaan booleaneja falseksi, jotta v‰‰ri‰ kohtia ei lis‰t‰ Arraylisteihin.
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
        
        // Kun parser kohtaa teksti‰, kutsuu t‰t‰. Mik‰li ehdot (yll‰olevat metodit s‰‰telev‰t booleanien avulla) t‰yttyv‰t,
        // lis‰t‰‰n tekstin p‰tk‰ Arraylistiin.
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
    
    
