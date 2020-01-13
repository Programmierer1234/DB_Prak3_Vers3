package Zettel3;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.print.attribute.Attribute;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.EventListener;
import java.util.LinkedList;



public class DeliveryNote {

    Order order;
    Customer customer;
    //Attribute für Bestellung

    LinkedList<Stock> alleBaestaende;
    LinkedList<Lpos2box> alleLpos2boxes;
    LinkedList<Box> alleBoxen;

    Boxtypecounter boxtypecounter;


    public DeliveryNote(Order o, Customer c,
                        LinkedList<Stock> s,
                        LinkedList<Lpos2box> l2b,
                        LinkedList<Box> b,
                        Boxtypecounter btc) {

        this.order = o;
        this.customer = c;
        this.alleBaestaende = s;
        this.alleLpos2boxes = l2b;
        this.alleBoxen = b;
        this.boxtypecounter = btc;

    }

    public void addBestand(Stock b) {
        this.alleBaestaende.addLast(b);
    }

    public void addBox(Box b) {
        this.alleBoxen.addLast(b);
    }

    public void addLp2b(Lpos2box l) {
        this.alleLpos2boxes.addLast(l);
    }


    @Override
    public String toString() {

        StringBuilder s = new StringBuilder();

        String titel = "-----------LIEFERSCHEIN LIEF" + this.order.getBestnr() + "-----------";

        s.append(titel);

        s.append(this.order.toString());

        s.append(this.customer.toString());

        for (Stock stock : this.alleBaestaende) {
            s.append(stock.toString());
        }

        s.append(this.boxtypecounter);

        for (Lpos2box l : this.alleLpos2boxes) {
            s.append(l.toString());
        }

        return s.toString();
    }


    //NOCH ZU IMPLEMENTIEREN
    public void printToConole() {
        System.out.println(this.toString());
    }

    public void printToText() {
        String filename = "LIEF" + this.order.getBestnr() + ".TXT";
        try {
            FileWriter fw = new FileWriter(filename, false);
            PrintWriter pw = new PrintWriter(fw);

            pw.println(this.toString());

            String ausgabetext = "\n\nTextdatei " + filename + " wurde erstellt!";
            System.out.println(ausgabetext);

            pw.close();
            fw.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void printToXml() throws ParserConfigurationException, TransformerException {

        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();

        //Ueberschrift erstellen
        String rootname = "LIEF" + this.order.getBestnr();
        Element lieferschein = document.createElement(rootname);
        document.appendChild(lieferschein);

        //Bestelldaten
        Element bestelldaten = document.createElement("Bestellung");
        Attr bestelnr = document.createAttribute("Bestellnummer");
        bestelnr.setValue(Integer.toString(this.order.getBestnr()));
        Element bestelldatum = document.createElement("Bestelldatum");
        bestelldatum.appendChild(document.createTextNode(this.order.getBestdat().toString()));
        Element bestellknr = document.createElement("Kundennummer");
        bestellknr.appendChild(document.createTextNode(Integer.toString(this.order.getKnr())));
        Element bestellstatus = document.createElement("Status");
        bestellstatus.appendChild(document.createTextNode(Integer.toString(this.order.getStatus())));

        lieferschein.appendChild(bestelldaten);
        bestelldaten.setAttributeNode(bestelnr);
        bestelldaten.appendChild(bestelldatum);
        bestelldaten.appendChild(bestellknr);
        bestelldaten.appendChild(bestellstatus);


        //Attribut Kunde
        Element kunde = document.createElement("Kunde");
        Attr knr = document.createAttribute("Kundennummer");
        knr.setValue(Integer.toString(this.customer.getKnr()));
        Element name = document.createElement("Name");
        name.appendChild(document.createTextNode(this.customer.getKname()));
        Element strasse = document.createElement("Strasse");
        strasse.appendChild(document.createTextNode(this.customer.getStrasse()));
        Element plz = document.createElement("Plz");
        strasse.appendChild(document.createTextNode(Integer.toString(this.customer.getPlz())));
        Element ort = document.createElement("Ort");
        ort.appendChild(document.createTextNode(this.customer.getOrt()));

        lieferschein.appendChild(kunde);
        kunde.setAttributeNode(knr);
        kunde.appendChild(name);
        kunde.appendChild(strasse);
        kunde.appendChild(plz);
        kunde.appendChild(ort);

        //Attribut Lagerbestand
        for(Stock stock : this.alleBaestaende){
            Element lagerbestand = document.createElement("Lagerbestand");
            Attr bestandnummer = document.createAttribute("Bestandnummer");
            bestandnummer.appendChild(document.createTextNode(Integer.toString(stock.getBstnr())));
            Element artnr = document.createElement("Artikelnummer");
            artnr.appendChild(document.createTextNode(this.customer.getOrt()));
            Element lagernr = document.createElement("Lagernummer");
            lagernr.appendChild(document.createTextNode(Integer.toString(stock.getLnr())));
            Element stuecke = document.createElement("Stueckzahl");
            stuecke.appendChild(document.createTextNode(Integer.toString(stock.getStuecke())));
            Element wert = document.createElement("Warenwert");
            wert.appendChild(document.createTextNode(Integer.toString(stock.getWert())));

            lieferschein.appendChild(lagerbestand);
            lagerbestand.setAttributeNode(bestandnummer);
            lagerbestand.appendChild(artnr);
            lagerbestand.appendChild(lagernr);
            lagerbestand.appendChild(stuecke);
            lagerbestand.appendChild(wert);
        }

        //Anzahl der Boxtypen
        Element boxtypzahlen = document.createElement("BoxtypenAnz");
        Element anzor = document.createElement("AnzOR");
        anzor.appendChild(document.createTextNode(Integer.toString(this.boxtypecounter.getAnzBoxOr())));
        Element anzfr = document.createElement("AnzFR");
        anzfr.appendChild(document.createTextNode(Integer.toString(this.boxtypecounter.getAnzBoxFr())));
        Element anzwp = document.createElement("AnzWP");
        anzwp.appendChild(document.createTextNode(Integer.toString(this.boxtypecounter.getAnzBoxWp())));
        Element anzfw = document.createElement("AnzFW");
        anzfw.appendChild(document.createTextNode(Integer.toString(this.boxtypecounter.getAnzBoxFw())));
        Element anzTypenfremd = document.createElement("AnzTypenfremd");
        anzTypenfremd.appendChild(document.createTextNode(Integer.toString(this.boxtypecounter.getAnztypfremd())));

        lieferschein.appendChild(anzor);
        lieferschein.appendChild(anzfr);
        lieferschein.appendChild(anzwp);
        lieferschein.appendChild(anzfw);
        lieferschein.appendChild(anzTypenfremd);

        for(Lpos2box pl : this.alleLpos2boxes){
            Element plos2box = document.createElement("Packliste");
            Attr packlistennr = document.createAttribute("Packlistennummer");
            packlistennr.appendChild(document.createTextNode(Integer.toString(pl.getB2bnr())));
            Element lposbestandsnr = document.createElement("Bestandsnummer");
            lposbestandsnr.appendChild(document.createTextNode(Integer.toString(pl.getBstnr())));
            Element boxnr = document.createElement("Boxnummer");
            boxnr.appendChild(document.createTextNode(Integer.toString(pl.getVbnr())));
            Element menge = document.createElement("Menge");
            menge.appendChild(document.createTextNode(Integer.toString(pl.getVmenge())));

            lieferschein.appendChild(plos2box);
            plos2box.setAttributeNode(packlistennr);
            plos2box.appendChild(lposbestandsnr);
            plos2box.appendChild(boxnr);
            plos2box.appendChild(menge);
        }

        // create the xml file
        //transform the DOM Object to an XML File
        String filename = rootname + ".XML";
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new File(filename));

        transformer.transform(domSource, streamResult);

        String ausgabetext = "\n\nXML Datei " + filename + " wurde erstellt!";
        System.out.println(ausgabetext);

    }


    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public LinkedList<Stock> getAlleBaestaende() {
        return alleBaestaende;
    }

    public void setAlleBaestaende(LinkedList<Stock> alleBaestaende) {
        this.alleBaestaende = alleBaestaende;
    }

    public LinkedList<Lpos2box> getAlleLpos2boxes() {
        return alleLpos2boxes;
    }

    public void setAlleLpos2boxes(LinkedList<Lpos2box> alleLpos2boxes) {
        this.alleLpos2boxes = alleLpos2boxes;
    }

    public LinkedList<Box> getAlleBoxen() {
        return alleBoxen;
    }

    public void setAlleBoxen(LinkedList<Box> alleBoxen) {
        this.alleBoxen = alleBoxen;
    }

    public Boxtypecounter getBoxtypecounter() {
        return boxtypecounter;
    }

    public void setBoxtypecounter(Boxtypecounter boxtypecounter) {
        this.boxtypecounter = boxtypecounter;
    }
}


