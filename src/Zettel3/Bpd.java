package Zettel3;

/**
 Entitaetsklasse Bpd
 @author Tobias M
 @version 1

 Beschreibung:
 Die Klasse stellt die Disposition zu einem Bestand einer Bestellung dar.
 */
//vmtl. BoxPerDispo
public class Bpd implements Comparable<Bpd> {
    private int bstnr; //bestellnummer
    private int artnr; //artikelnummer
    private int anzbo; //anzahl der Boxen, die in eine Box passen
    private int menge; //vmtl. menge von boxen
    private int algrad; //auslastungsgrad des arktikels in einer box in prozent
    private String artbez; //artikelBezeichnung

    private Transporttyp ttyp; //transportTyp
    private boolean verpackt;  //istVerpackt


    public Bpd(int bstnr, String ttyp,
               int artnr, String artbez,
               int menge, int anzPbox){

        //Parameter
        this.bstnr = bstnr;
        this.ttyp = Transporttyp.valueOf(ttyp);
        this.artnr = artnr;
        this.artbez = artbez;
        this.menge = menge;
        //Benoetigte Anzahl der boxen bestimmen
        this.anzbo = anzPbox;
        if(anzbo>0){
            this.algrad = (menge*100)/anzbo;
        }

        this.verpackt = false;
    }

    @Override
    public int compareTo(Bpd o) {

        int erg = this.ttyp.compareTo(o.getTtyp()); //negativ, wenn this kleiner

        if (erg == 0) {
             return (o.getAlgrad()-this.algrad);
        }else{
            return erg;
        }
    }

    public void print(){
        System.out.println("Transporttyp:               " + ttyp.toString());
        System.out.println("Auslastungsgrad:            " + algrad);
        System.out.println("Bestandsnummer:             " + bstnr);
        System.out.println("Artikelnummer:              " + artnr);
        System.out.println("Max. Menge p. Box:          " + anzbo);
        System.out.println("Menge des Bestadnes:        " + menge);
        System.out.println();
    }

    public int getBstnr() {
        return bstnr;
    }

    public void setBstnr(int bstnr) {
        this.bstnr = bstnr;
    }

    public int getArtnr() {
        return artnr;
    }

    public void setArtnr(int artnr) {
        this.artnr = artnr;
    }

    public int getAnzbo() {
        return anzbo;
    }

    public void setAnzbo(int anzbo) {
        this.anzbo = anzbo;
    }

    public int getMenge() {
        return menge;
    }

    public void setMenge(int menge) {
        this.menge = menge;
    }

    public int getAlgrad() {
        return algrad;
    }

    public void setAlgrad(int algrad) {
        this.algrad = algrad;
    }

    public String getArtbez() {
        return artbez;
    }

    public void setArtbez(String artbez) {
        this.artbez = artbez;
    }

    public Transporttyp getTtyp() {
        return ttyp;
    }

    public void setTtyp(Transporttyp ttyp) {
        this.ttyp = ttyp;
    }

    public boolean isVerpackt() {
        return verpackt;
    }

    public void setVerpackt(boolean verpackt) {
        this.verpackt = verpackt;
    }

    public boolean getVerpackt(){ return this.verpackt;}

}
