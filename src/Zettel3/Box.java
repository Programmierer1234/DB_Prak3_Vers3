package Zettel3;

import java.util.LinkedList;

public class Box implements Comparable<Box> {

    int R, vbnr, vstat, vbstnr;
    Transporttyp vbtyp;

    public Box(int vbnr,String vbtyp) {
        this.R = 100;
        this.vbnr = vbnr;
        this.vstat = 0;
        this.vbstnr = 0;
        this.vbtyp = Transporttyp.valueOf(vbtyp);
    }

    @Override
    public int compareTo(Box o) {
        return (this.vbtyp.compareTo(o.getVbtyp())*(-1)); //negativ, wenn this größer
    }

    public void print(){
        System.out.println("       " + vbtyp + "       " + vbnr + "         " + vstat);
        /*
        System.out.println("Transporttyp:           " + vbtyp);
        System.out.println("Nummer:                 " + vbnr);
        System.out.println("Status:                 " + vstat);
        System.out.println("Bestandsnummer:         " + vbstnr);
        System.out.println();
        */
    }
    public boolean compatible(Transporttyp ttyp){
        if(ttyp.compareTo(this.vbtyp)<=0){
            return true;
        }else{
            return false;
        }
    }

    public Transporttyp getVbtyp() {
        return vbtyp;
    }

    public void setVbtyp(Transporttyp ttyp) {
        this.vbtyp = ttyp;
    }

    public int getR() {
        return R;
    }

    public void setR(int r) {
        R = r;
    }

    public int getVbnr() {
        return vbnr;
    }

    public void setVbnr(int vbnr) {
        this.vbnr = vbnr;
    }

    public int getVstat() {
        return vstat;
    }

    public void setVstat(int vstat) {
        this.vstat = vstat;
    }

    public int getVbstnr() {
        return vbstnr;
    }

    public void setVbstnr(int vbstnr) {
        this.vbstnr = vbstnr;
    }

}
