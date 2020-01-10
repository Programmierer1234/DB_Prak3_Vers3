package Zettel3;

public class Stock {
    int artnr, lnr, stuecke, wert, bstnr;

    public Stock(int artnr, int lnr, int stuecke, int wert, int bstnr) {
        this.artnr = artnr; //Artikelnummer
        this.lnr = lnr; //Lagernummer
        this.stuecke = stuecke; //Stueckzahl des Artikels
        this.wert = wert; // Warenwert des Artikels
        this.bstnr = bstnr; // Bestandnummer
    }

    @Override
    public String toString(){
        String stock =
                "\n\n-----------Lagerbestand-----------" +
                "\nBestandnummer:       " + this.bstnr +
                "\nArtikelnummer:       " + this.artnr +
                "\nLagernummer:         " + this.lnr +
                "\nStueckzahl:          " + this.stuecke +
                "\nWarenwert:           " + this.wert ;

        return stock;
    }

    public int getArtnr() {
        return artnr;
    }

    public void setArtnr(int artnr) {
        this.artnr = artnr;
    }

    public int getLnr() {
        return lnr;
    }

    public void setLnr(int lnr) {
        this.lnr = lnr;
    }

    public int getStuecke() {
        return stuecke;
    }

    public void setStuecke(int stuecke) {
        this.stuecke = stuecke;
    }

    public int getWert() {
        return wert;
    }

    public void setWert(int wert) {
        this.wert = wert;
    }

    public int getBstnr() {
        return bstnr;
    }

    public void setBstnr(int bstnr) {
        this.bstnr = bstnr;
    }
}
