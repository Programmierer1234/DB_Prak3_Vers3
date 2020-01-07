package Zettel3;

import java.util.LinkedList;



public class DeliveryNote {
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

    public void calcAnzBoxes(){
        Transporttyp typ;
        for(Box b: this.alleBoxen){
            typ = b.getVbtyp();

            if(typ == Transporttyp.OR){
                this.anzBoxOr++;
            }
            else if(typ == Transporttyp.FR){
                this.anzBoxFr++;
            }
            else if(typ == Transporttyp.WP){
                this.anzBoxWp++;
            }
            else if(typ == Transporttyp.FW){
                this.anzBoxFw++;
            }

        }
    }



    //NOCH ZU IMPLEMENTIEREN
    public void printToConole(){
        String text =
                "Lieferschein:" +
                        "       Bestellungsdaten:" +
                        "       Bestelldatum: " + this.
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
