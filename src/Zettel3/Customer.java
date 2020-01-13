package Zettel3;

public class Customer {
    private String kname, ort, strasse;     //Attribute für Kunden
    private int plz, knr;

    public Customer( int knr, String kname, String strasse, int plz, String ort) {
        this.kname = kname;
        this.ort = ort;
        this.strasse = strasse;
        this.plz = plz;
        this.knr = knr;
    }

    @Override
    public String toString(){
        String s ="\n\n-----------Kunde-----------" +
                "\nKundennummer:        " + this.knr +
                "\nName:                " + this.kname +
                "\nStrasse:             " + this.strasse +
                "\nPLZ:                 " + this.plz +
                "\nOrt:                 " + this.ort;

        return s;
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

    public int getKnr() {
        return knr;
    }

    public void setKnr(int knr) {
        this.knr = knr;
    }
}
