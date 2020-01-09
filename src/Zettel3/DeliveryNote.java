package Zettel3;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;



public class DeliveryNote {
    java.sql.Date bestdat;
    int knr = 0;                    //Gemeinsam: Bestellungen und Kunde
    int status = 0, bestnr = 0;     //Attribute für Bestellung
    String kname, ort, strasse;     //Attribute für Kunden
    int plz;
    LinkedList<Stock> alleBaestaende;
    LinkedList<Lpos2box> alleLpos2boxes;
    LinkedList<Box> alleBoxen;
    int anzBoxOr;
    int anzBoxFr;
    int anzBoxWp;
    int anzBoxFw;
    int anztypfremd;



    public DeliveryNote(java.sql.Date bestdat, int knr, int status,
                        int bestnr, String kname,
                        String ort, String strasse,
                        int plz) {
        this.bestdat = bestdat;
        this.knr = knr;
        this.status = status;
        this.bestnr = bestnr;
        this.kname = kname;
        this.ort = ort;
        this.strasse = strasse;
        this.plz = plz;
        this.alleBaestaende = new LinkedList<>();
        this.alleLpos2boxes = new LinkedList<>();
        this.alleBoxen = new LinkedList<>();

    }

    public void addBestand(Stock b){
        this.alleBaestaende.addLast(b);
    }

    public void addBox(Box b){
        this.alleBoxen.addLast(b);
    }

    public void addLp2b(Lpos2box l){
        this.alleLpos2boxes.addLast(l);
    }



    @Override
    public String toString(){

        StringBuilder s = new StringBuilder();
        String tmp =
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
                        "\nStrasse:             " + this.strasse;

        s.append(tmp);

        s.append("\n\n-----------Lagerbestand-----------");
        for(Stock stock : this.alleBaestaende){
            s.append(stock.toString());
        }

        String anzBoxen = "\n\n-----------Boxtypen und deren Anzahl-----------" +
                "\nOR:                  " + this.anzBoxOr +
                "\nFR:                  " + this.anzBoxFr +
                "\nWP:                  " + this.anzBoxWp +
                "\nFW:                  " + this.anzBoxFw +
                "\nAnz typfremd:        " + this.anztypfremd;

        s.append(anzBoxen);

        s.append("\n\n-----------Packlsite-----------");
        for(Lpos2box l : this.alleLpos2boxes){
            s.append(l.toString());
        }

        return s.toString();

    }


    //NOCH ZU IMPLEMENTIEREN
    public void printToConole(){
        System.out.println(this.toString());
    }
    public void printToText(){
        String filename = "LIEF" + this.bestnr;
        try{
            FileWriter fw = new FileWriter(filename, false);
            PrintWriter pw = new PrintWriter(fw);

            pw.println(this.toString());

            pw.close();
            fw.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    public void printToXml(){
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

        Document document = documentBuilder.newDocument();
    }






    public int getKnr() {
        return knr;
    }

    public void setKnr(int knr) {
        this.knr = knr;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getBestnr() {
        return bestnr;
    }

    public void setBestnr(int bestnr) {
        this.bestnr = bestnr;
    }

    public String getKname() {
        return kname;
    }

    public void setKname(String kname) {
        this.kname = kname;
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public int getPlz() {
        return plz;
    }

    public void setPlz(int plz) {
        this.plz = plz;
    }

    public LinkedList<Stock> getAlleBeataende() {
        return alleBaestaende;
    }

    public void setAlleBeataende(LinkedList<Stock> alleBeataende) {
        this.alleBaestaende = alleBeataende;
    }

    public LinkedList<Stock> getAlleBaestaende() {
        return alleBaestaende;
    }

    public void setAlleBaestaende(LinkedList<Stock> alleBaestaende) {
        this.alleBaestaende = alleBaestaende;
    }

    public LinkedList<Lpos2box> getLpos2boxes() {
        return alleLpos2boxes;
    }

    public void setLpos2boxes(LinkedList<Lpos2box> lpos2boxes) {
        this.alleLpos2boxes = lpos2boxes;
    }

    public LinkedList<Box> getAlleBoxen() {
        return alleBoxen;
    }

    public void setAlleBoxen(LinkedList<Box> alleBoxen) {
        this.alleBoxen = alleBoxen;
    }

    public int getAnzBoxOr() {
        return anzBoxOr;
    }

    public void setAnzBoxOr(int anzBoxOr) {
        this.anzBoxOr = anzBoxOr;
    }

    public int getAnzBoxFr() {
        return anzBoxFr;
    }

    public void setAnzBoxFr(int anzBoxFr) {
        this.anzBoxFr = anzBoxFr;
    }

    public int getAnzBoxWp() {
        return anzBoxWp;
    }

    public void setAnzBoxWp(int anzBoxWp) {
        this.anzBoxWp = anzBoxWp;
    }

    public int getAnzBoxFw() {
        return anzBoxFw;
    }

    public void setAnzBoxFw(int antBoxFw) {
        this.anzBoxFw = antBoxFw;
    }

    public int getAnzBoxWithWeakerTranportCondition() {
        return anzBoxWithWeakerTranportCondition;
    }

    public void setAnzBoxWithWeakerTranportCondition(int anzBoxWithWeakerTranportCondition) {
        this.anzBoxWithWeakerTranportCondition = anzBoxWithWeakerTranportCondition;
    }
}
