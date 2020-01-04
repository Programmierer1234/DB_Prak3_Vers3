package Zettel3;

import java.util.LinkedList;



public class DeliveryNote {
    int knr = 0;                    //Gemeinsam: Bestellungen und Kunde
    int status = 0, bestnr = 0;     //Attribute für Bestellung
    String kname, ort, strasse;     //Attribute für Kunden
    int plz;
    LinkedList<Stock> alleBaestaende;
    LinkedList<Lpos2box> lpos2boxes;
    int anzBoxOr;
    int anzBoxFr;
    int anzBoxWp;
    int antBoxFw;
    int anzBoxWithWeakerTranportCondition;



    public DeliveryNote(int knr, int status,
                        int bestnr, String kname,
                        String ort, String strasse,
                        int plz) {
        this.knr = knr;
        this.status = status;
        this.bestnr = bestnr;
        this.kname = kname;
        this.ort = ort;
        this.strasse = strasse;
        this.plz = plz;
        this.alleBaestaende = new LinkedList<>();
    }

    public void addBestand(Stock b){
        this.alleBaestaende.addLast(b);
    }

    //NOCH ZU IMPLEMENTIEREN
    public void printToConole(){

    }
    public void printToText(){

    }
    public void printToXml(){

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
}
