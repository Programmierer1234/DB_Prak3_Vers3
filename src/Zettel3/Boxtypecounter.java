package Zettel3;

public class Boxtypecounter {
    private int anzBoxOr;
    private int anzBoxFr;
    private int anzBoxWp;
    private int anzBoxFw;
    private int anztypfremd;


    public Boxtypecounter() {

    }

    @Override
    public String toString(){
        String s = "\n\n-----------Boxtypen und deren Anzahl-----------" +
                "\nOR:                  " + this.anzBoxOr +
                "\nFR:                  " + this.anzBoxFr +
                "\nWP:                  " + this.anzBoxWp +
                "\nFW:                  " + this.anzBoxFw +
                "\nAnz typfremd:        " + this.anztypfremd;

        return s;
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

    public void setAnzBoxFw(int anzBoxFw) {
        this.anzBoxFw = anzBoxFw;
    }

    public int getAnztypfremd() {
        return anztypfremd;
    }

    public void setAnztypfremd(int anztypfremd) {
        this.anztypfremd = anztypfremd;
    }
}
