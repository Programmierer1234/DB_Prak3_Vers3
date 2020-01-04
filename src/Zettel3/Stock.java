package Zettel3;

public class Stock {
    int artnr, lnr, stuecke, wert, bstnr;

    public Stock(int artnr, int lnr, int stuecke, int wert, int bstnr) {
        this.artnr = artnr;
        this.lnr = lnr;
        this.stuecke = stuecke;
        this.wert = wert;
        this.bstnr = bstnr;
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
