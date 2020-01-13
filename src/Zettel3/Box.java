package Zettel3;

import java.util.LinkedList;

public class Box implements Comparable<Box> {

    private int R, vbnr, vstat, vbestnr;
    private boolean verpackt;
    private Transporttyp vbtyp;

    public Box(int r, int vbnr, int vstat, int vbestnr, String vbtyp) {
        R = r;
        this.vbnr = vbnr;
        this.vstat = vstat;
        this.vbestnr = vbestnr;
        this.vbtyp = Transporttyp.valueOf(vbtyp);
        this.verpackt = false;
    }

    @Override
    public int compareTo(Box o) {
        return (this.vbtyp.compareTo(o.getVbtyp())*(-1)); //negativ, wenn this größer
    }

    @Override
    public String toString(){
        String s = "\n\n-----------Box-----------" +
                "\nBoxnummer:               " + this.vbnr +
                "\nStatus:                  " + this.vstat +
                "\nBestellnummer:           " + this.vbestnr +
                "\nTransporttyp:            " + this.vbtyp.toString();

        return s;
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
    public boolean compatible(Bpd d, Typfremd anzTypenfremd){
        if(d.getTtyp().compareTo(this.vbtyp)==0){
            return true;
        }else if(d.getTtyp().compareTo(this.vbtyp)<0){
            if((d.getVerpackt()==false)&&(this.verpackt==false)){
                anzTypenfremd.setAnzTypfremd(anzTypenfremd.getAnzTypfremd()+1);
            }
            return true;
        }else{
            return false;
        }
    }

    public Transporttyp getVbtyp() {
        return vbtyp;
    }

    public void setVbtyp(String ttyp) {
        this.vbtyp = Transporttyp.valueOf(ttyp);
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
        return vbestnr;
    }

    public void setVbstnr(int vbestnr) {
        this.vbestnr = vbestnr;
    }

    public int getVbestnr() {
        return vbestnr;
    }

    public void setVbestnr(int vbestnr) {
        this.vbestnr = vbestnr;
    }

    public boolean isVerpackt() {
        return verpackt;
    }

    public void setVerpackt(boolean verpackt) {
        this.verpackt = verpackt;
    }

    public void setVbtyp(Transporttyp vbtyp) {
        this.vbtyp = vbtyp;
    }


}
