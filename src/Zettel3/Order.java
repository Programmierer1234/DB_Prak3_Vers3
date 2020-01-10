package Zettel3;

import java.sql.Date;

public class Order {

    private java.sql.Date bestdat;
    private int knr = 0;                    //Gemeinsam: Bestellungen und Kunde
    private int status = 0, bestnr = 0;


    public Order(Date bestdat, int knr, int status, int bestnr) {
        this.bestdat = bestdat;
        this.knr = knr;
        this.status = status;
        this.bestnr = bestnr;
    }

    @Override
    public String toString(){
        String s =
                "\n\nBestelldaten:" +
                "\nBestellnummer:       " + this.bestnr +
                "\nBestelldatum:        " + this.bestdat +
                "\nKundennummer:        " + this.knr +
                "\nSatus:               " + this.status;

        return s;
    }


    public Date getBestdat() {
        return bestdat;
    }

    public void setBestdat(Date bestdat) {
        this.bestdat = bestdat;
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
}
