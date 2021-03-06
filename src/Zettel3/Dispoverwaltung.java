package Zettel3;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Bpd_Verwaltung
 * @author Tobias M
 * @version 1
 *
 */
public class Dispoverwaltung {

    private static Dispoverwaltung ourInstance;
    private Typfremd anztypFremd;
    private PostgresJDBC postgres;
    private LinkedList<Box> emptyBoxList;
    private LinkedList<Bpd> bpdispo;
    int bestnr;

    private Dispoverwaltung() {
        this.bpdispo = new LinkedList<>();
        this.emptyBoxList = new LinkedList<>();
        this.anztypFremd = new Typfremd();
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
        SQL = "select bestdat, bestnr, status from \"BESTELLUNG\" b where b.status = 1 order by b.bestnr";
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
        int vbnr, vbstnr;

        this.emptyBoxList.clear();

        ResultSet RS;
        String SQL = "select vbnr,vbtyp,vbstnr from box b where vstat = 0";

        PreparedStatement ps = this.postgres.getConnection().prepareStatement(SQL);
        RS = ps.executeQuery();

        while(RS.next()){
            vbtyp = RS.getString("vbtyp");
            vbnr = RS.getInt("vbnr");
            vbstnr = RS.getInt("vbstnr");
            this.emptyBoxList.addLast(new Box(100, vbnr, 0 ,vbstnr ,vbtyp));
        }
        this.sortBoxesListTopDown();
        RS.close();
        ps.close();

    }

    public void sortBpdispo(){
        Collections.sort(this.bpdispo);
    }

    public void sortBoxesListTopDown(){ Collections.sort(this.emptyBoxList); }

    public void printBpdispo(){ //NICH FUER AUFGABE C VERWENDEN, HIER IST DIE AUFLISTUNG FREIER BOXEN VERLANGT
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

        int anzBOX, vmenge, vmengeLast;
        String SQL, fehler;
        Box box2;
        int k;
        LinkedList<Box> fullBoxList = new LinkedList<>();
        LinkedList<Packlist> packlist = new LinkedList<>();
        LinkedList<Bpd> bpdclone = new LinkedList<>(this.bpdispo);
        LinkedList<Box> emtyBoxListClone = new LinkedList<>(this.emptyBoxList);
        Box box;
        PreparedStatement ps;

        this.anztypFremd.setAnzTypfremd(0);

        if(this.emptyBoxList.isEmpty() || this.bpdispo.isEmpty()){
            throw new SQLException("Keine Boxen oder Bestände zum disponieren vohanden!");
        }

        for(int i = emptyBoxList.size()-1; i >= 0; i--){
            box = emptyBoxList.get(i);
            for(Bpd d : bpdispo){
                if(box.compatible(d,this.anztypFremd)&&(!d.getVerpackt())&&(box.isVerpackt()==false)){
                    if(box == null){
                        fehler = "Nicht genug Boxen mit der Transportbedingung " + d.getTtyp() + " vorhanden!";
                        throw new SQLException(fehler);
                    }
                    if(d.getAlgrad()<100){ //Bestand passt in die Kiste
                        //Abfuellen der Box
                        d.setVerpackt(true);
                        box.setR(100-d.getAlgrad());
                        packlist.addLast(new Packlist(d.getBstnr(), box.getVbnr(), d.getMenge()));
                        //Suchen nach einem weiteren Bestand, der abgefuellt werden kann
                        for(Bpd d2 : bpdispo){
                            if(d2.getAlgrad()<box.getR()&& (d2.getVerpackt()==false)){
                                d2.setVerpackt(true);
                                box.setR(box.getR()-d2.getAlgrad());
                                packlist.addLast(new Packlist(d2.getBstnr(), box.getVbnr(), d.getMenge()));
                            }

                        }
                        box.setVerpackt(true);
                    }
                    else{
                        //Berechnen der erforderlichen Boxen
                        anzBOX = d.getAlgrad()/100+1;
                        vmenge = d.getMenge()/anzBOX;
                        vmengeLast = vmenge%anzBOX;

                        box.setVerpackt(true);
                        packlist.addLast(new Packlist(d.getBstnr(), box.getVbnr(), vmenge));

                        for(k = i-1; k > (i-anzBOX) ; k--){//Rückwerts durch die Boxliste
                            box2 = emptyBoxList.get(k);
                            if(box2 == null || (!box2.compatible(d,this.anztypFremd))||(k-1<0)){
                                fehler = "Nicht genug Boxen mit der Transportbedingung " + d.getTtyp() + " vorhanden!";
                                throw new SQLException(fehler);
                            }
                            if(box2.isVerpackt()==false){
                                fullBoxList.addLast(box2);
                                emtyBoxListClone.remove(box2);
                                packlist.addLast(new Packlist(d.getBstnr(), box2.getVbnr(), vmenge));
                                box2.setVerpackt(true);
                            }

                        }

                        if(vmengeLast != 0){
                            box2 = emptyBoxList.get(k);
                            if(box2 == null || !box2.compatible(d,this.anztypFremd)){
                                fehler = "Nicht genug Boxen mit der Transportbedingung " + d.getTtyp() + " vorhanden!";
                                throw new SQLException(fehler);
                            }
                            if(box2.isVerpackt()==false){
                                fullBoxList.addLast(box2);
                                emtyBoxListClone.remove(box2);
                                packlist.addLast(new Packlist(d.getBstnr(), box2.getVbnr(), vmengeLast));
                                box2.setVerpackt(true);
                            }

                        }
                        d.setVerpackt(true);
                    }
                }
            }
        }

        for(Bpd d : this.bpdispo){
            if(!d.getVerpackt()){
                throw new SQLException("Nicht alle Bestaende verpackt!");
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
        for(Box o : this.emptyBoxList){
            if(o.isVerpackt()){
                SQL = "update box set vstat = 1, vbstnr = ? where vbnr = ?";
                ps = this.postgres.getConnection().prepareStatement(SQL);
                ps.setInt(1, bestnr);
                ps.setInt(2, o.getVbnr());
                ps.executeUpdate();
                ps.close();
                System.out.println("Box " + o.getVbnr()+" aktuallisiert: vstat = 1, vbstnr = "+bestnr);
            }

        }

    }



    public void showScheduledOrders() throws SQLException{
        java.sql.Date bestdat;
        int knr = 0, status = 0, bestnr = 0;
        Order tmpOrder;

        String SQL = "select * from \"BESTELLUNG\" b where status = 2";
        ResultSet resultSet;
        PreparedStatement ps = this.postgres.getConnection().prepareStatement(SQL);
        resultSet = ps.executeQuery();

        if(resultSet.next()){
            bestdat = resultSet.getDate("bestDat");
            knr = resultSet.getInt("knr");
            status = resultSet.getInt("status");
            bestnr = resultSet.getInt("bestnr");

            System.out.println("Bestellnummer:      " + bestnr);
            System.out.println("Kundennummer:       " + knr);
            System.out.println("Bestelldatum:       " + bestdat);
            System.out.println("Status:             " + status);
            System.out.println();
        }else{
            throw new SQLException("Keine disponierte Bestellung vorhanden!");
        }

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

    public DeliveryNote createDeliveryNoteFor(int bestnr)throws SQLException{

        Customer customer;
        Order order;
        LinkedList<Stock> alleBaestaende = new LinkedList<>();
        LinkedList<Lpos2box> alleLpos2boxes = new LinkedList<>();
        LinkedList<Box> alleBoxen = new LinkedList<>();
        Boxtypecounter boxtypecounter;


        //Laden von Bestelldaten und Kunden
        String SQL = "select b.bestdat, b.bestnr, b.status, k.knr, k.kname, k.ort, k.plz, k.strasse\n" +
                "from \"BESTELLUNG\" b\n" +
                "join \"KUNDE\" k\n" +
                "on k.knr = b.knr\n" +
                "where status = 2 and b.bestnr = ?";
        ResultSet resultSet;

        PreparedStatement ps = this.postgres.getConnection().prepareStatement(SQL);
        ps.setInt(1, bestnr);
        resultSet = ps.executeQuery();

        if(resultSet.next()){

            customer = new Customer(
                    resultSet.getInt("knr"),
                    resultSet.getString("kname"),
                    resultSet.getString("strasse"),
                    resultSet.getInt("plz"),
                    resultSet.getString("ort")
                    );

            order = new Order(
                    resultSet.getDate("bestdat"),
                    resultSet.getInt("knr"),
                    resultSet.getInt("status"),
                    resultSet.getInt("bestnr")
            );

            resultSet.close();
            ps.close();
        }else{
            resultSet.close();
            ps.close();
            throw new SQLException("Bestellung nicht gefunden.");
        }

        //Laden des Lagebstandes
        SQL = "select * from \"LAGERBESTAND\" l where bestnr = ?";

        ps = this.postgres.getConnection().prepareStatement(SQL);

        ps.setInt(1, bestnr);

        resultSet = ps.executeQuery();

        while(resultSet.next()){
            alleBaestaende.addLast(new Stock(
                    resultSet.getInt("artnr"),
                    resultSet.getInt("lnr"),
                    resultSet.getInt("stuecke"),
                    resultSet.getInt("wert"),
                    resultSet.getInt("bstnr")));
        }
        resultSet.close();
        ps.close();


        //Laden der Boxen mit der Bestellnummer = ?
        SQL = "select * from box b where vbstnr = ?";
        ps = this.postgres.getConnection().prepareStatement(SQL);
        ps.setInt(1, bestnr);
        resultSet = ps.executeQuery();

        while(resultSet.next()){
            alleBoxen.addLast(new Box(
                    0,
                    resultSet.getInt("vbnr"),
                    resultSet.getInt("vstat"),
                    resultSet.getInt("vbstnr"),
                    resultSet.getString("vbtyp")));
        }
        resultSet.close();
        ps.close();

        //Laden der Zuordnung lpos2Box
        SQL = "select *\n" +
                "from lpos2box lb\n" +
                "where exists(\n" +
                "\tselect *\n" +
                "\tfrom \"LAGERBESTAND\" l\n" +
                "\twhere l.bestnr = ? and lb.bstnr = l.bstnr\n" +
                ")";

        ps = this.postgres.getConnection().prepareStatement(SQL);
        ps.setInt(1, bestnr);
        resultSet = ps.executeQuery();

        while(resultSet.next()){
            alleLpos2boxes.addLast(new Lpos2box(
                    resultSet.getInt("b2bnr"),
                    resultSet.getInt("bstnr"),
                    resultSet.getInt("vbnr"),
                    resultSet.getInt("vmenge")
            ));
        }
        resultSet.close();
        ps.close();


        //Anzahl Boxen bestimmen
        SQL = "select b.vbtyp, count(b.vbtyp) as anz\n" +
                "from box b\n" +
                "where vbstnr = ?\n" +
                "group by b.vbtyp";

        ps = this.postgres.getConnection().prepareStatement(SQL);
        ps.setInt(1, bestnr);

        resultSet = ps.executeQuery();

        boxtypecounter = new Boxtypecounter();

        boxtypecounter.setAnztypfremd(this.anztypFremd.getAnzTypfremd());

        while(resultSet.next()){

            String typ = resultSet.getString("vbtyp");
            int anz = resultSet.getInt("anz");

            switch(typ){
                case ("OR"):
                    boxtypecounter.setAnzBoxOr(anz);
                    break;
                case ("FR"):
                    boxtypecounter.setAnzBoxFr(anz);
                    break;
                case ("WP"):
                    boxtypecounter.setAnzBoxWp(anz);
                    break;
                case ("FW"):
                    boxtypecounter.setAnzBoxFw(anz);
                    break;
            }
        }

        DeliveryNote deliveryNote = new DeliveryNote(order, customer, alleBaestaende, alleLpos2boxes, alleBoxen, boxtypecounter);

        return deliveryNote;

    }



}

