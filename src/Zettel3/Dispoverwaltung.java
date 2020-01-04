package Zettel3;

import java.sql.*;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Bpd_Verwaltung
 * @author Tobias M
 * @version 1
 *
 */
public class Dispoverwaltung {

    private static Dispoverwaltung ourInstance;
    private PostgresJDBC postgres;
    private LinkedList<Box> emptyBoxList;
    private LinkedList<Bpd> bpdispo;
    int bestnr;

    private Dispoverwaltung() {
        this.bpdispo = new LinkedList<>();
        this.emptyBoxList = new LinkedList<>();
    }

    public static Dispoverwaltung getInstance() {
        if(ourInstance==null){
            ourInstance = new Dispoverwaltung();
        }
        return ourInstance;
    }

    public PostgresJDBC getPostgres() {
        return postgres;
    }

    public void setPostgres(PostgresJDBC postgres) {
        this.postgres = postgres;
    }

    public void showOrdersWithStatOne()throws SQLException{
        String SQL;
        int bestnr,status;
        java.sql.Date bestdat;
        Statement Stmt = this.postgres.getConnection().createStatement();
        ResultSet RS;

        // Eine SQL Select Anweisung
        SQL = "select bestdat, bestnr, status from \"BESTELLUNG\" b where b.status = 1";
        // SQL-Anweisung ausführen und Ergebnis in ein ResultSet schreiben
        RS = Stmt.executeQuery(SQL);

        System.out.println();
        // Das ResultSet Datensatzweise durchlaufen
        while (RS.next()) {
            bestnr = RS.getInt("bestnr");
            status = RS.getInt("status");
            bestdat = RS.getDate("bestdat");

            System.out.println();
            System.out.println("Bestellung:     " + bestnr);
            System.out.println("Bestelldatum:   " + bestdat);
            System.out.println("Status:         " + status);
        }
        Stmt.close();
    }

    /**
     * createDispoListFor
     * @param bestnr
     * @throws SQLException
     * @implNote Erzeugt eine Dispoliste zur übergebenen Bestandsnummer.
     */
    public void createDispoListFor(int bestnr) throws SQLException {

        int bstnr,artnr,menge,anzPbox;
        String ttyp,artbez;

        //Initialisieren der Lokalen Parameter
        this.bestnr = bestnr;
        this.bpdispo.clear();

        String SQL = "select status from \"BESTELLUNG\" b where bestnr = ? and status = 1";
        ResultSet RS;

        PreparedStatement ps = this.postgres.getConnection().prepareStatement(SQL);

        ps.setInt(1, bestnr);

        RS = ps.executeQuery();

        if (!RS.next())throw new SQLException("Besstellung " + bestnr + " wurde bereits disponiert.");

        SQL = "select lb.bstnr, a.ttyp, lb.artnr, a.artbez, lb.stuecke, a.anzpbox \n" +
                "from \"ARTIKEL\" a \n" +
                "join \"LAGERBESTAND\" lb \n" +
                "on a.artnr = lb.artnr \n" +
                "where lb.bestnr = ?";

       ps = postgres.getConnection().prepareStatement(SQL);

        ps.setInt(1, bestnr);

        RS = ps.executeQuery();

        while(RS.next()){

            bstnr = RS.getInt("bstnr");
            artnr = RS.getInt("artnr");
            menge = RS.getInt("stuecke");
            ttyp = RS.getString("ttyp");
            artbez = RS.getString("artbez");
            anzPbox = RS.getInt("anzpbox");

            this.bpdispo.addLast(new Bpd(bstnr, ttyp, artnr, artbez, menge,anzPbox));
        }
        this.sortBpdispo();
        RS.close();
        ps.close();
    }

    public void createEmptyBoxList() throws SQLException {
        String vbtyp;
        int vbnr;

        this.emptyBoxList.clear();

        ResultSet RS;
        String SQL = "select vbnr,vbtyp from box b where vstat = 0";

        PreparedStatement ps = this.postgres.getConnection().prepareStatement(SQL);
        RS = ps.executeQuery();

        while(RS.next()){
            vbtyp = RS.getString("vbtyp");
            vbnr = RS.getInt("vbnr");
            this.emptyBoxList.addLast(new Box(vbnr,vbtyp));
        }
        this.sortBoxesList();
        RS.close();
        ps.close();

    }

    public void sortBpdispo(){
        Collections.sort(this.bpdispo);
    }

    public void sortBoxesList(){ Collections.sort(this.emptyBoxList); }

    public void printBpdispo(){ //NICH FUER AUFGABE C VERWENDEN, HIER IST DIE AUFLISTUNG FREUER BOXENVERLANGT
        int i = 0;
        for(Bpd o : this.bpdispo){
            System.out.println("Index:  " + i);
            o.print();
            i++;
        }
    }

    public void printEmptyBoxList(){ //DAS HIER VERWENDEN FÜR AUFGABE C
        int i = 1;
        System.out.println("  Transporttyp | Nummer |  Status");
        for(Box o : this.emptyBoxList){
            System.out.print(i+": ");
            o.print();
            i++;
        }

    }

    /**
     * easyMachineDisposition()
     * Disponiert den Bestand einer Bestellung in Boxen und entstehlt
     * entsprechende Packlisten. Nach erfolgreicher Disponierung wird
     * die Packliste in lpos2box und der Status der diponierten Boxen
     * gespeichert.
     *
     * BISHER BEKANNTE BUGS
     *
     * BSTNR WIR NICHT RICHTIF IN lpos2box GESETZT
     * ANZAHL DER BOXEN WIRD NOCH NICHT RICHTIG BESTIMMT
     */
    public void easyMachineDisposition() throws SQLException {

        int anzBOX, algrad, menge, vmenge, vmengeLast, i;
        String SQL;
        Box filledBox;
        LinkedList<Box> dispoBoxList = new LinkedList<>();
        PreparedStatement ps;
        boolean foundBox;
        Bpd disp;

        for(Box box: this.emptyBoxList){
            while(box.getR()<100){
                for(Bpd d : this.bpdispo){
                    if((d.getAlgrad()<box.getR())&&box.compatible(d.getTtyp())){
                        foundBox = true;
                        disp = d;
                        break;
                    }else {
                        foundBox = false;
                    }

                }
                if(foundBox = false){
                    throw SQLException("Zur Disposition "  + )
                }
            }
        }

        //Einfügen des Bestellstatus
        SQL = "update \"BESTELLUNG\" set status = 2 where bestnr = ?";
        ps = this.postgres.getConnection().prepareStatement(SQL);
        ps.setInt(1, this.bestnr);
        ps.executeUpdate();
        ps.close();

        System.out.println("Der Status der Bestellung " + bestnr + " wurde auf 2 gesetzt.");

        //Update der lpos2box tabelle
        for(Packlist o : packlist){
            SQL = "insert into lpos2box(bstnr, vbnr, vmenge) values (?,?,?)";
            ps = this.postgres.getConnection().prepareStatement(SQL);
            ps.setInt(1, o.getBstnr());
            ps.setInt(2, o.getVbnr());
            ps.setInt(3, o.getVmenge());
            ps.executeUpdate();
            ps.close();
            System.out.println("Neuen lpos2box Eintrag erstellt mit" +
                    " bstnr = " + o.getBstnr()
                    + " vbnr = " + o.getVbnr()
                    + " vmenge = " + o.getVmenge());
        }



        //Update der Box Tabelle

        //FEHLER: BESTANDSNUMMER WIRD NUR AUF 0 GESETZT
        for(Box o : dispoBoxList){
            SQL = "update box set vstat = 1, vbstnr = ? where vbnr = ?";
            ps = this.postgres.getConnection().prepareStatement(SQL);
            ps.setInt(1, o.getVbstnr());
            ps.setInt(2, o.getVbnr());
            ps.executeUpdate();
            ps.close();
            System.out.println("Box " + o.getVbnr()+" aktuallisiert: vstat = 1, vbstnr = "+o.getVbstnr());
        }



    }

    /**
     * searchEmtyBox
     * Sucht eine Box aus der Boxliste, die den Transporttypen entspricht und gibt sie als
     * Rueckgabeparameter zurueck.
     * Gibt es keine Box mit entsprechenden Typen, wird der nächst höhere Typ
     * Verwendet. Sollte gar keine Box mehr geben, die die Bedingung erfuellt,
     * wird null zurueckgegeben.
     * @param vbtyp
     * @return
     */
    public Box searchEmptyBox(String vbtyp) throws SQLException {
        Transporttyp typ = Transporttyp.valueOf(vbtyp);
        Box o;
        int i = 0;
        for(i = this.emptyBoxList.size()-1; i >= 0 ; i--){
            o = this.emptyBoxList.get(i);
            if(o.getVbtyp().compareTo(typ)>=0){
                this.emptyBoxList.remove(o);
                return o;
            }
        }
        String error = "Nicht genug Boxen fuer den Transporttyp " + vbtyp + " vorhanden!";
        throw new SQLException(error);
    }

    public void showScheduledOrders() throws SQLException{
        java.sql.Date bestdat;
        int knr = 0, status = 0, bestnr = 0;

        String SQL = "select * from \"BESTELLUNG\" b where status = 2";
        ResultSet resultSet;
        PreparedStatement ps = this.postgres.getConnection().prepareStatement(SQL);
        resultSet = ps.executeQuery();

        while(resultSet.next()){
            bestdat = resultSet.getDate("bestDat");
            knr = resultSet.getInt("knr");
            status = resultSet.getInt("status");
            bestnr = resultSet.getInt("bestnr");

            System.out.println("Bestellnummer:      " + bestnr);
            System.out.println("Kundennummer:       " + knr);
            System.out.println("Bestelldatum:       " + bestdat);
            System.out.println("Status:             " + status);
            System.out.println();
        }
    }

    public void createDeliveryNoteFor(int bestnr)throws SQLException{

        String SQL = "select b.bestdat, b.bestnr, b.status, k.knr, k.kname, k.ort, k.plz, k.strasse\n" +
                "from \"BESTELLUNG\" b\n" +
                "join \"KUNDE\" k\n" +
                "on k.knr = b.knr\n" +
                "where status = 2 and b.bestnr = ?";
        ResultSet resultSet;
        DeliveryNote deliveryNote;

        PreparedStatement ps = this.postgres.getConnection().prepareStatement(SQL);

        ps.setInt(1, bestnr);

        resultSet = ps.executeQuery();

        if(resultSet.next()){
            deliveryNote = new DeliveryNote(
                    resultSet.getInt("knr"),
                    resultSet.getInt("status"),
                    resultSet.getInt("bestnr"),
                    resultSet.getString("kname"),
                    resultSet.getString("ort"),
                    resultSet.getString("strasse"),
                    resultSet.getInt("plz"));
            resultSet.close();
            ps.close();
        }else{
            resultSet.close();
            ps.close();
            throw new SQLException("Bestellung nicht gefunden.");
        }

        SQL = "select * from \"LAGERBESTAND\" l where bestnr = ?";

        ps = this.postgres.getConnection().prepareStatement(SQL);

        ps.setInt(1, bestnr);

        resultSet = ps.executeQuery();

        while(resultSet.next()){
            deliveryNote.addBestand(new Stock(
                    resultSet.getInt("artnr"),
                    resultSet.getInt("lnr"),
                    resultSet.getInt("stuecke"),
                    resultSet.getInt("wert"),
                    resultSet.getInt("bstnr")));
        }

    }

}
