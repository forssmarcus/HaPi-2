    // Kartankatseluohjelman graafinen k�ytt�liittym�
     
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
      
      //N�ihin ker�t��n karttakerrosten nimet ja titlet XML-tiedostosta
      ArrayList<String> layerNames;
      ArrayList<String> layerTitles;
      
      // K�ytt�liittym�n komponentit
      
      private JLabel imageLabel = new JLabel();
      private JPanel leftPanel = new JPanel();
      
      private JButton refreshB = new JButton("P�ivit�");
      private JButton leftB = new JButton("<");
      private JButton rightB = new JButton(">");
      private JButton upB = new JButton("^");
      private JButton downB = new JButton("v");
      private JButton zoomInB = new JButton("+");
      private JButton zoomOutB = new JButton("-");
      
      public MapDialog() throws Exception {
        
        // Alustetaan ArrayListit joihin karttakerrosten nimet ja titlet ker�t��n
        layerNames = new ArrayList<String>();
        layerTitles = new ArrayList<String>();
        
        // Valmistele ikkuna ja lis�� siihen komponentit
     
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
     
        // ALLA OLEVAN TESTIRIVIN VOI KORVATA JOLLAKIN MUULLA ERI ALOITUSN�KYM�N
        // LATAAVALLA RIVILL�
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
        // ALLA OLEVIEN KOLMEN TESTIRIVIN TILALLE SILMUKKA JOKA LIS�� K�YTT�LIITTYM��N
        // KAIKKIEN XML-DATASTA HAETTUJEN KERROSTEN VALINTALAATIKOT MALLIN MUKAAN
        
        URL contentsURL = new URL("http://demo.mapserver.org/cgi-bin/wms?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetCapabilities");
       
        // Haetaan XML yll� olevasta URLista ja k�yd��n l�pi.

          SAXParserFactory factory = SAXParserFactory.newInstance();
          SAXParser saxParser = factory.newSAXParser();
          UserHandler userHandler = new UserHandler(layerNames, layerTitles);
          
          // K�yd��n XML-tiedosto l�pi ja poimitaan halutut tiedot
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
        
        // Luodaan k�ytt�liittym��n CheckBox-oliot kutakin karttakerrosta varten:
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
      // KAIKKIEN NAPPIEN YHTEYDESS� VOINEE HY�DYNT�� updateImage()-METODIA
      private class ButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
          if(e.getSource() == refreshB) {
            //try { updateImage(); } catch(Exception ex) { ex.printStackTrace(); }
          }
          if(e.getSource() == leftB) {
            // TODO:
            // VASEMMALLE SIIRTYMINEN KARTALLA
            // MUUTA KOORDINAATTEJA, HAE KARTTAKUVA PALVELIMELTA JA P�IVIT� KUVA
          }
          if(e.getSource() == rightB) {
            // TODO:
            // OIKEALLE SIIRTYMINEN KARTALLA
            // MUUTA KOORDINAATTEJA, HAE KARTTAKUVA PALVELIMELTA JA P�IVIT� KUVA
          }
          if(e.getSource() == upB) {
            // TODO:
            // YL�SP�IN SIIRTYMINEN KARTALLA
            // MUUTA KOORDINAATTEJA, HAE KARTTAKUVA PALVELIMELTA JA P�IVIT� KUVA
          }
          if(e.getSource() == downB) {
            // TODO:
            // ALASP�IN SIIRTYMINEN KARTALLA
            // MUUTA KOORDINAATTEJA, HAE KARTTAKUVA PALVELIMELTA JA P�IVIT� KUVA
          }
          if(e.getSource() == zoomInB) {
            // TODO:
            // ZOOM IN -TOIMINTO
            // MUUTA KOORDINAATTEJA, HAE KARTTAKUVA PALVELIMELTA JA P�IVIT� KUVA
          }
          if(e.getSource() == zoomOutB) {
            // TODO:
            // ZOOM OUT -TOIMINTO
            // MUUTA KOORDINAATTEJA, HAE KARTTAKUVA PALVELIMELTA JA P�IVIT� KUVA
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
     
      // Tarkastetaan mitk� karttakerrokset on valittu,
      // tehd��n uudesta karttakuvasta pyynt� palvelimelle ja p�ivitet��n kuva
      public void updateImage() throws Exception {
        String s = "";
     
        // Tutkitaan, mitk� valintalaatikot on valittu, ja
        // ker�t��n s:��n pilkulla erotettu lista valittujen kerrosten
        // nimist� (k�ytet��n haettaessa uutta kuvaa)
        Component[] components = leftPanel.getComponents();
        for(Component com:components) {
            if(com instanceof LayerCheckBox)
              if(((LayerCheckBox)com).isSelected()) s = s + com.getName() + ",";
        }
        if (s.endsWith(",")) s = s.substring(0, s.length() - 1);
     
        // TODO:
        // getMap-KYSELYN URL-OSOITTEEN MUODOSTAMINEN JA KUVAN P?IVITYS ERILLISESS� S�IKEESS�
        // imageLabel.setIcon(new ImageIcon(url));
      }
     
     
    } // MapDialog
    
    
    // Oma handleriluokka, joka osaa poimia k�sittelem�st��n (juuri oikeanlaisesta) XML-tiedostosta karttakerrosten
    // nimet ja titlet. Saa parametrina viittaukset MapDialogin Arraylisteihin, joihin lis�� ker��m�ns� Stringit.
    class UserHandler extends DefaultHandler{
      
      int layers = 0;
      boolean capability;
      boolean layer;
      boolean name;
      boolean title;
      ArrayList<String> names;
      ArrayList<String> titles;
      
      // KONSTRUKTORI - Saa viittaukset MapDialogin Arraylisteihin (siell� layerNames ja layerTitles, t��ll� names ja titles)
      public UserHandler (ArrayList<String> names, ArrayList<String> titles){
        capability = false;
        layer = false;
        name = false;
        title = false;
        this.names = names;
        this.titles = titles;
      }
      
      // Kohdatessaan elementin XML-tiedostossa parser kutsuu t�t� metodia. Mik�li elementti t�ytt�� ehdon, vaihdetaan
      // booleanin arvoa. N�ill� valikoidaan mitk� nimet poimitaan ja lis�t��n Arraylisteihin.
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

      // Kun elementti saatu k�sitelty�, parser kutsuu t�t�. Muutetaan booleaneja falseksi, jotta v��ri� kohtia ei lis�t� Arraylisteihin.
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

      // Kun parser kohtaa teksti�, kutsuu t�t�. Mik�li ehdot (yll�olevat metodit s��telev�t booleanien avulla) t�yttyv�t,
      // lis�t��n tekstin p�tk� Arraylistiin.
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
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    