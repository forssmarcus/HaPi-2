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
      
      // Käyttöliittymän komponentit
      
      private JLabel imageLabel = new JLabel();
      private JPanel leftPanel = new JPanel();
      
      private JButton refreshB = new JButton("Päivitä");
      private JButton leftB = new JButton("<");
      private JButton rightB = new JButton(">");
      private JButton upB = new JButton("^");
      private JButton downB = new JButton("v");
      private JButton zoomInB = new JButton("+");
      private JButton zoomOutB = new JButton("-");
      
      public MapDialog() throws Exception {
        
        // Alustetaan ArrayListit joihin karttakerrosten nimet ja titlet kerätään
        layerNames = new ArrayList<String>();
        layerTitles = new ArrayList<String>();
        
        // Valmistele ikkuna ja lisää siihen komponentit
     
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
     
        // ALLA OLEVAN TESTIRIVIN VOI KORVATA JOLLAKIN MUULLA ERI ALOITUSNÄKYMÄN
        // LATAAVALLA RIVILLÄ
        imageLabel.setIcon(new ImageIcon(new URL("http://demo.mapserver.org/cgi-bin/wms?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&BBOX=-180,-90,180,90&SRS=EPSG:4326&WIDTH=953&HEIGHT=480&LAYERS=bluemarble,cities&STYLES=&FORMAT=image/png&TRANSPARENT=true")));
     
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
     
        
        // TODO:
        // ALLA OLEVIEN KOLMEN TESTIRIVIN TILALLE SILMUKKA JOKA LISÄÄ KÄYTTÖLIITTYMÄÄN
        // KAIKKIEN XML-DATASTA HAETTUJEN KERROSTEN VALINTALAATIKOT MALLIN MUKAAN
        
        URL contentsURL = new URL("http://demo.mapserver.org/cgi-bin/wms?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetCapabilities");
       
        // Haetaan XML yllä olevasta URLista ja käydään läpi.

          SAXParserFactory factory = SAXParserFactory.newInstance();
          SAXParser saxParser = factory.newSAXParser();
          UserHandler userHandler = new UserHandler(layerNames, layerTitles);
          
          // Käydään XML-tiedosto läpi ja poimitaan halutut tiedot
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
     
        add(leftPanel, BorderLayout.WEST);
     
        pack();
        setVisible(true);
     
      }
     
      public static void main(String[] args) throws Exception {
        new MapDialog();
      }
     
      // Kontrollinappien kuuntelija
      // KAIKKIEN NAPPIEN YHTEYDESSÄ VOINEE HYÖDYNTÄÄ updateImage()-METODIA
      private class ButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
          if(e.getSource() == refreshB) {
            //try { updateImage(); } catch(Exception ex) { ex.printStackTrace(); }
          }
          if(e.getSource() == leftB) {
            // TODO:
            // VASEMMALLE SIIRTYMINEN KARTALLA
            // MUUTA KOORDINAATTEJA, HAE KARTTAKUVA PALVELIMELTA JA PÄIVITÄ KUVA
          }
          if(e.getSource() == rightB) {
            // TODO:
            // OIKEALLE SIIRTYMINEN KARTALLA
            // MUUTA KOORDINAATTEJA, HAE KARTTAKUVA PALVELIMELTA JA PÄIVITÄ KUVA
          }
          if(e.getSource() == upB) {
            // TODO:
            // YLÖSPÄIN SIIRTYMINEN KARTALLA
            // MUUTA KOORDINAATTEJA, HAE KARTTAKUVA PALVELIMELTA JA PÄIVITÄ KUVA
          }
          if(e.getSource() == downB) {
            // TODO:
            // ALASPÄIN SIIRTYMINEN KARTALLA
            // MUUTA KOORDINAATTEJA, HAE KARTTAKUVA PALVELIMELTA JA PÄIVITÄ KUVA
          }
          if(e.getSource() == zoomInB) {
            // TODO:
            // ZOOM IN -TOIMINTO
            // MUUTA KOORDINAATTEJA, HAE KARTTAKUVA PALVELIMELTA JA PÄIVITÄ KUVA
          }
          if(e.getSource() == zoomOutB) {
            // TODO:
            // ZOOM OUT -TOIMINTO
            // MUUTA KOORDINAATTEJA, HAE KARTTAKUVA PALVELIMELTA JA PÄIVITÄ KUVA
          }
        }
      }
     
      // Valintalaatikko, joka muistaa karttakerroksen nimen
      private class LayerCheckBox extends JCheckBox {
        private String name = "";
        public LayerCheckBox(String name, String title, boolean selected) {
          super(title, null, selected);
          this.name = name;
        }
        public String getName() { return name; }
      }
     
      // Tarkastetaan mitkä karttakerrokset on valittu,
      // tehdään uudesta karttakuvasta pyyntö palvelimelle ja päivitetään kuva
      public void updateImage() throws Exception {
        String s = "";
     
        // Tutkitaan, mitkä valintalaatikot on valittu, ja
        // kerätään s:ään pilkulla erotettu lista valittujen kerrosten
        // nimistä (käytetään haettaessa uutta kuvaa)
        Component[] components = leftPanel.getComponents();
        for(Component com:components) {
            if(com instanceof LayerCheckBox)
              if(((LayerCheckBox)com).isSelected()) s = s + com.getName() + ",";
        }
        if (s.endsWith(",")) s = s.substring(0, s.length() - 1);
     
        // TODO:
        // getMap-KYSELYN URL-OSOITTEEN MUODOSTAMINEN JA KUVAN P?IVITYS ERILLISESSÄ SÄIKEESSÄ
        // imageLabel.setIcon(new ImageIcon(url));
      }
     
     
    } // MapDialog
    
    
    // Oma handleriluokka, joka osaa poimia käsittelemästään (juuri oikeanlaisesta) XML-tiedostosta karttakerrosten
    // nimet ja titlet. Saa parametrina viittaukset MapDialogin Arraylisteihin, joihin lisää keräämänsä Stringit.
    class UserHandler extends DefaultHandler{
      
      int layers = 0;
      boolean capability;
      boolean layer;
      boolean name;
      boolean title;
      ArrayList<String> names;
      ArrayList<String> titles;
      
      // KONSTRUKTORI - Saa viittaukset MapDialogin Arraylisteihin (siellä layerNames ja layerTitles, täällä names ja titles)
      public UserHandler (ArrayList<String> names, ArrayList<String> titles){
        capability = false;
        layer = false;
        name = false;
        title = false;
        this.names = names;
        this.titles = titles;
      }
      
      // Kohdatessaan elementin XML-tiedostossa parser kutsuu tätä metodia. Mikäli elementti täyttää ehdon, vaihdetaan
      // booleanin arvoa. Näillä valikoidaan mitkä nimet poimitaan ja lisätään Arraylisteihin.
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

      // Kun elementti saatu käsiteltyä, parser kutsuu tätä. Muutetaan booleaneja falseksi, jotta vääriä kohtia ei lisätä Arraylisteihin.
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

      // Kun parser kohtaa tekstiä, kutsuu tätä. Mikäli ehdot (ylläolevat metodit säätelevät booleanien avulla) täyttyvät,
      // lisätään tekstin pätkä Arraylistiin.
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
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    