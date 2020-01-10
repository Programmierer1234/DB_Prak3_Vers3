package Zettel3;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
        String filename = "LIEF" + this.order.getBestnr();
        try {
            FileWriter fw = new FileWriter(filename, false);
            PrintWriter pw = new PrintWriter(fw);

            pw.println(this.toString());

            pw.close();
            fw.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void printToXml() throws ParserConfigurationException {

        /*String tmp =
                "-----------Lieferschein-----------" +
                        "\nBestellungsdaten:" +
                        "\nBestellnummer:       " + this.bestnr +
                        "\nBestelldatum:        " + this.bestdat +
                        "\nKundennummer:        " + this.knr +
                        "\nSatus:               " + this.status +
                        "\n\n-----------Kunde-----------" +
                        "\nKundennummer:        " + this.knr +
                        "\nName:                " + this.kname +
                        "\nPLZ:                 " + this.plz +
                        "\nOrt:                 " + this.ort +
                        "\nStrasse:             " + this.strasse;*/

        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();

        Element root = document.createElement("Liefeerschein");
        document.appendChild(root);

        Element grundDaten = document.createElement();
        grundDaten.appendChild(grundDaten);


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


