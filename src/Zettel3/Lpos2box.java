package Zettel3;

public class Lpos2box {

    int b2bnr, bstnr, vbnr, vmenge;

    public Lpos2box(int id, int bestandsnummer, int boxnummer, int menge){
        this.b2bnr = id;
        this.bstnr = bestandsnummer;
        this.vbnr = boxnummer;
        this.vmenge = menge;
    }


    @Override
    public String toString(){
        String s =
                "\nID:                      " + this.b2bnr +
                "\nBestandsnummer:          " + this.bstnr +
                "\nBoxnummer:               " + this.vbnr +
                "\nMenge:                   " + this.vmenge;

        return s;
    }

    public int getB2bnr() {
        return b2bnr;
    }

    public void setB2bnr(int b2bnr) {
        this.b2bnr = b2bnr;
    }

    public int getBstnr() {
        return bstnr;
    }

    public void setBstnr(int bstnr) {
        this.bstnr = bstnr;
    }

    public int getVbnr() {
        return vbnr;
    }

    public void setVbnr(int vbnr) {
        this.vbnr = vbnr;
    }

    public int getVmenge() {
        return vmenge;
    }

    public void setVmenge(int vmenge) {
        this.vmenge = vmenge;
    }
}
