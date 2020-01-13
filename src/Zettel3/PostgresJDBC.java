package Zettel3;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.sql.*;
import java.util.*;


public class PostgresJDBC {

    private Connection con;
    private Dispoverwaltung dispoVerwaltung;


    public PostgresJDBC() throws SQLException, IOException{
        login();
        this.dispoVerwaltung = Dispoverwaltung.getInstance();
        this.dispoVerwaltung.setPostgres(this);
    }

    public Connection getConnection(){
        return this.con;
    }

    /*********************************************************************/
    /*                  Login zur Datenbank                              */
    /*********************************************************************/
    private void login()throws IOException,SQLException{

        String user, password, server;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            /*
            System.out.print("Server: ");
            server = in.readLine();
            System.out.print("Username: ");
            user = in.readLine();
            System.out.print("Password: ");
            password = in.readLine();

            con = connect(user,password,server);
             */

        con = connect("dbprak17","AlexUndTobi","feuerbach");
        System.out.println("Login erfolgreich!");

    }

    /*********************************************************************/
    /*                    Aufbau der DB-Verbindung                       */
    /*********************************************************************/
    private Connection connect(String user, String pass, String server) throws SQLException {

        String url = "jdbc:postgresql://"+server+".nt.fh-koeln.de/postgres?user="+user+"&password="+pass;
        Connection dbConnection=null;

        // Treiber laden
        try{
            Class.forName("org.postgresql.Driver").newInstance();
        } catch (Exception e){
            System.out.println("Fehler beim laden des Treibers: "+ e.getMessage());
        }

        System.out.println("Aufbau der Verbindung zur Datebank.");

        // Erstellung Datenbank-Verbindungsinstanz
        dbConnection = DriverManager.getConnection(url);

        return dbConnection;
    }

    /*********************************************************************/
    /*                    Einlesen der CSV Datei                         */
    /*********************************************************************/
    /*Lese alle daten aus ARTIKEL.DAT und gebe sie in der Konsole aus
     * 1. Erzeugen des Streams
     * 2. Endlosschleife, bis EOF erreicht ist. (Rückgabe von Readline = null)
     * 3. Einlesen jeder Zeile von ARTIKEL.DAT und ausgeben auf der Konsole
     * 3. Schließen des Streams
     * 4. Verlassen der Methode
     */
    private LinkedList<String> readAllLinesFromCsv(String filename) throws IOException {

        File f = new File(filename);
        LinkedList<String> allLines = new LinkedList<>();

        FileReader fr = new FileReader(f); //1.
        BufferedReader br = new BufferedReader(fr);
        String line;
        int i = 0;

        while((line=br.readLine())!=null) { //2.
            allLines.addLast(line);
        }

        br.close(); //3.
        fr.close();

        return allLines; //4.


    }

    /*********************************************************************/
    /*               Parsen von CSV Zeilen zu SQL Anweisungen            */
    /*********************************************************************/
    /*  Parst einen Kunden vom CSV Format zur INSERT Anweisung

        BSP Aufbau der Zeile
        kname,plz,ort,strasse,knr
        Meyer;12345;Blimsheim;Blubbstr;1

        Bemerkung: knr wird nicht benötigt, weil bei der INSERT Anweisung,
        aufgrund des Datentyps serial, die knr automatisch zugewiesen wird.
         */
    private LinkedList<String> parseToInsertStatementsFrom(LinkedList<String> customers){

        //Parsen einer CSV Zeile in eine SQL INSERT Anweisung
        String name,plz,ort,strasse,statement;

        LinkedList<String> statements = new LinkedList<>();


        for(String customer : customers){

            String[] seperatedValues = customer.split(";");
            if(seperatedValues.length<5 || seperatedValues.length>5){
                System.out.println("Falsche Anzahl Argumente bei: " + customer);
            }else{
                name = seperatedValues[0];
                plz = seperatedValues[1];
                ort = seperatedValues[2];
                strasse = seperatedValues[3];
                statement = "INSERT INTO \"KUNDE\" VALUES ('" + name + "'," + plz + ",'" + ort + "','" + strasse + "')\n";
                statements.addLast(statement);
            }

        }

        return statements;
    }

    /*********************************************************************/
    /*                    INSERT aller CSV Kundendaten                   */
    /*********************************************************************/
    /*  Parameter : SQL Anweisung
        Rückgabe : Anweisungsspezifisch
        Beschreibung:
        Führt eine als Parameter übergebes Statement aus
     */
    private void insertAll() throws SQLException, IOException {

        int successfull = 0, numberOfSuccessfull = 0;
        LinkedList<String> customersCSV;
        LinkedList<String> insertStatements;

        customersCSV = readAllLinesFromCsv("KUNDE.CSV");
        insertStatements =  parseToInsertStatementsFrom(customersCSV);

        Statement stat = con.createStatement();

        for(String statement : insertStatements){
            try{
                System.out.print(statement);
                successfull = stat.executeUpdate(statement);
                if (successfull==1){
                    System.out.println("Insert erfolgreich!");
                    numberOfSuccessfull++;
                } else {
                    System.out.println("Insert fehlgeschlagen!");
                }
            }catch(SQLException e){
                System.out.println(e.getMessage());
            }

        }
        stat.close();

        System.out.println("Anzahl erfolgreicher INSERTS: " + numberOfSuccessfull);

        return;
    }

/**************************************************************************************************/
    /*                            AB hier Funktionen der LagerDB                                      */
/**************************************************************************************************/

    /*********************************************************************/
    /*                    Hauptmenue der Abgabe                          */
    /*********************************************************************/
    /*Hauptmenue, bietet alle Funktionen aus Zetel 2 an*/
    public void menue()throws IOException {
        String input;
        int knr = 0,bestnr = 0, artnr = 0, stuecke = 0, bstnr = 0;
        DeliveryNote deliveryNote;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        do {
            System.out.println("Hauptmenue.");
            System.out.println("A4      : Daten von KUNDE.CSV in die Tabelle Kunde einfügen - Aufgabe 4");
            System.out.println("A       : Anzeige aller Artikel - Aufgabe 5a");
            System.out.println("B       : Anzeige aller Lager - Aufgabe 5b");
            System.out.println("C       : Anzeige aller Lagerbestände eines Lagers X - Aufgabe 5c");
            System.out.println("D       : Anzeige aller Kunden - Aufgabe 5d");
            System.out.println("E       : Werte in den Lagerbestaenden berechnen - Aufgabe 5e");
            System.out.println("F       : Zu Artikel X alle Stammdaten und alle Lagerbestaende anzeigen - Aufgabe 5f");
            System.out.println("A6c     : Anlegen einer Bestellung - Aufgabe 6c");
            System.out.println("A6d     : Lagerbestände einer Bestellung zuordnen - Aufgabe 6d");
            System.out.println("G       : Versandplanung");
            System.out.println("H       : Erstellen eines Lieferschein");
            System.out.println("EXIT    : Um Programm zu verlassen");

            System.out.print("Eingabe: ");
            input = in.readLine();

            if(input.equals("A4")){
                try{
                    insertAll();
                }catch(SQLException e1){
                    System.out.println(e1.getMessage());
                }catch(IOException e2){
                    System.out.println(e2.getMessage());
                }
                System.out.println("PRESS ENTER TO CONTINUE");
                in.read();

            }else if(input.equals("A")){
                try{
                    showAllItems();
                }catch(SQLException e){
                    System.out.println(e.getMessage());
                }
                System.out.println("PRESS ENTER TO CONTINUE");
                in.read();
            }else if(input.equals("B")){
                try{
                    showAllWarehouses();
                }catch(SQLException e){
                    System.out.println(e.getMessage());
                }
                System.out.println("PRESS ENTER TO CONTINUE");
                in.read();
            }else if(input.equals("C")){
                try{
                    System.out.print("Lagernummer: ");
                    showStockFrom(Integer.parseInt(in.readLine()));
                }catch(SQLException e1){
                    System.out.println(e1.getMessage());
                }catch(NumberFormatException e2){
                    System.out.println("Wert ist keine Zahl!");
                }
                System.out.println("PRESS ENTER TO CONTINUE");
                in.read();
            }else if(input.equals("D")){
                try{
                    showAllCustomer();
                }catch (SQLException e){
                    System.out.println(e.getMessage());
                }
                System.out.println("PRESS ENTER TO CONTINUE");
                in.readLine();
            }else if(input.equals("E")){
                try{
                    System.out.print("Artikelnummer: ");
                    calculateItemValuesOf(Integer.parseInt(in.readLine()));
                }catch(SQLException e1){

                }catch(NumberFormatException e2){
                    System.out.println("Wert ist keine Zahl!");
                }
                System.out.println("PRESS ENTER TO CONTINUE");
                in.read();
            }else if(input.equals("F")){
                try{
                    System.out.print("Artikelnummer: ");
                    showBasedataFrom(Integer.parseInt(in.readLine()));
                }catch(SQLException e1){

                }catch(NumberFormatException e2){
                    System.out.println("Wert ist keine Zahl!");
                }
                System.out.println("PRESS ENTER TO CONTINUE");
                in.read();
            }else if(input.equals("A6c")) {
                try {
                    System.out.print("Kundennummer: ");
                    knr = Integer.parseInt(in.readLine());
                    System.out.print("Datum [JJJJ-MM-DD]: ");
                    String date = in.readLine();
                    java.sql.Date d = java.sql.Date.valueOf(date);
                    createOrder(knr, d);
                } catch (SQLException e1) {
                    System.out.println(e1.getMessage());
                } catch (NumberFormatException e2) {
                    System.out.println("Knr: Wert ist keine Zahl!");
                } catch (IllegalArgumentException e3) {
                    System.out.println("Datum: Wert hat ungültiges Format");
                }
                System.out.println("PRESS ENTER TO CONTINUE");
                in.readLine();
            }else if(input.equals("A6d")) {
                try {
                    System.out.print("Bestellnummer: ");
                    bestnr = Integer.parseInt(in.readLine());
                    System.out.print("Artikelnummer: ");
                    artnr = Integer.parseInt(in.readLine());
                    System.out.print("Stueckezahl: ");
                    stuecke = Integer.parseInt(in.readLine());
                    assignStockToAnOrder(bestnr, artnr, stuecke);
                } catch (SQLException e1) {
                    System.out.println(e1.getMessage());
                } catch (NumberFormatException e2) {
                    System.out.println("Wert ist keine Zahl!");
                }
                System.out.println("PRESS ENTER TO CONTINUE");
                in.readLine();
            }
            else if (input.equals("G")) {
                try{
                    dispoVerwaltung.showOrdersWithStatOne();//a)
                    System.out.print("Bestellnummer: ");//b)
                    bestnr = Integer.parseInt(in.readLine());
                    dispoVerwaltung.createDispoListFor(bestnr);
                    dispoVerwaltung.createEmptyBoxList();
                    System.out.println("Sortierte Bpd Liste:\n");
                    dispoVerwaltung.printBpdispo();
                    System.out.println("Sortierte Box Liste:\n");
                    dispoVerwaltung.printEmptyBoxList();
                    dispoVerwaltung.easyMachineDisposition();
                    deliveryNote = dispoVerwaltung.createDeliveryNoteFor(bestnr);
                    deliveryNote.printToConole();
                    deliveryNote.printToText();
                    deliveryNote.printToXml();

                }catch(SQLException e1) {
                    System.out.println(e1.getMessage());
                } catch(NumberFormatException e2) {
                    System.out.println("Keine gültige Zahl eingegeben!");
                } catch (ParserConfigurationException e) {
                    System.out.println(e.getMessage());
                } catch (TransformerException e) {
                    System.out.println(e.getMessageAndLocation());
                }

                System.out.println("PRESS ENTER TO CONTINUE");
                in.readLine();

            }else if(input.equals("H")){

                try{
                    dispoVerwaltung.showScheduledOrders();
                    System.out.println("Bestellnummer: ");
                    bestnr = Integer.parseInt(in.readLine());

                }catch(SQLException e1){
                    System.out.println(e1.getMessage());
                }catch(NumberFormatException e2){
                    System.out.println("Keine gültige Eingabe!");
                }

                System.out.println("PRESS ENTER TO CONTINUE");
                in.readLine();


            } else if(input.equals("EXIT")){
                try{
                    con.close();
                }catch(SQLException e){
                    System.out.println(e.getMessage());
                }
                System.out.println("Tschüss!");
            }else{
                System.out.println("Keine gültige Eingabe!");
            }

        } while (input.equals("EXIT")==false);

        return;
    }

    /*********************************************************************/
    /*                  SELECT aller Kundendaten                         */
    /*********************************************************************/
    private void showAllCustomer() throws SQLException {

        String SQL, name, ort, strasse,columnName;
        int knr, plz;
        Statement Stmt = con.createStatement();
        ResultSet RS;

        // Eine SQL Select Anweisung
        SQL = "select * from \"KUNDE\"";
        // SQL-Anweisung ausführen und Ergebnis in ein ResultSet schreiben
        RS = Stmt.executeQuery(SQL);

        System.out.println();
        // Das ResultSet Datensatzweise durchlaufen
        while (RS.next()) {
            name = RS.getString("kname");
            plz = RS.getInt("plz");
            ort = RS.getString("ort");
            strasse = RS.getString("strasse");
            knr = RS.getInt("knr");

            System.out.println("Kundennummer:   " + knr);
            System.out.println("Name:           " + name);
            System.out.println("PLZ:            " + plz);
            System.out.println("Ort:            " + ort);
            System.out.println("Strasse:        " + strasse);
            System.out.println();
        }

        Stmt.close();

    }

    /*********************************************************************/
    /*                  SELECT aller Artikel                             */
    /*********************************************************************/
    private void showAllItems() throws SQLException {

        String SQL, columnName, artbez, mge;
        int preis, steu, artnr;
        Statement Stmt = con.createStatement();
        ResultSet RS;

        // Eine SQL Select Anweisung
        SQL = "select * from \"ARTIKEL\"";
        // SQL-Anweisung ausführen und Ergebnis in ein ResultSet schreiben
        RS = Stmt.executeQuery(SQL);

        System.out.println();
        // Das ResultSet Datensatzweise durchlaufen
        while (RS.next()) {
            artbez = RS.getString("artbez");
            preis = RS.getInt("preis");
            steu = RS.getInt("steu");
            artnr = RS.getInt("artnr");
            mge = RS.getString("mge");

            System.out.println("Artikelnummer:  " + artnr);
            System.out.println("Bezeichnung:    " + artbez);
            System.out.println("Preis:          " + preis);
            System.out.println("Steuern:        " + steu);
            System.out.println("Menge:          " + mge);
            System.out.println();
        }

        Stmt.close();

    }

    /*********************************************************************/
    /*                  SELECT aller Lager                        */
    /*********************************************************************/
    private void showAllWarehouses() throws SQLException {

        String SQL,columnName, lort;
        int lnr, lplz;
        Statement Stmt = con.createStatement();
        ResultSet RS;

        // Eine SQL Select Anweisung
        SQL = "select * from \"LAGER\"";
        // SQL-Anweisung ausführen und Ergebnis in ein ResultSet schreiben
        RS = Stmt.executeQuery(SQL);

        System.out.println();
        // Das ResultSet Datensatzweise durchlaufen
        while (RS.next()) {
            lnr = RS.getInt("lnr");
            lort = RS.getString("lort");
            lplz = RS.getInt("lplz");

            System.out.println("Lagernummer:    " + lnr);
            System.out.println("Ort:            " + lort);
            System.out.println("PLZ:            " + lplz);
            System.out.println();
        }

        Stmt.close();

    }

    /*********************************************************************/
    /*                  SELECT aller Lager                        */
    /*********************************************************************/
    private void showStockFrom(int warehouseNumber) throws SQLException {

        String SQL, artbez, mge;
        int preis = 0, steu = 0, artnr = 0, lnr = 0, stuecke = 0 ,bstnr = 0;
        Statement Stmt = con.createStatement();
        ResultSet RS;

        // Eine SQL Select Anweisung
        SQL = "select * from  \"LAGERBESTAND\" lb, \"ARTIKEL\" a " +
                "where lb.lnr = "+ warehouseNumber+ " and lb.artnr = a.artnr";
        // SQL-Anweisung ausführen und Ergebnis in ein ResultSet schreiben
        RS = Stmt.executeQuery(SQL);

        System.out.println();
        // Das ResultSet Datensatzweise durchlaufen
        System.out.println("Bestand im Lager " + warehouseNumber);
        System.out.println("--------------------------------");
        while (RS.next()) {
            artbez = RS.getString("artbez");
            preis = RS.getInt("preis");
            steu = RS.getInt("steu");
            artnr = RS.getInt("artnr");
            mge = RS.getString("mge");
            stuecke = RS.getInt("stuecke");
            bstnr = RS.getInt("bstnr");

            System.out.println("Bestandsnummer:     " + bstnr);
            System.out.println("Artikel:            " + artbez);
            System.out.println("Artikelnummer:      " + artnr);
            System.out.println("Stueckzahl:         " + stuecke);
            System.out.println("Mengeneinheit:      " + mge);
            System.out.println("Preis:              " + preis);
            System.out.println("Steuern:            " + steu);
            System.out.println("--------------------------------");
        }
        Stmt.close();
    }

    /*********************************************************************/
    /*              Berechnen und Einfügen der Bestandswerte             */
    /*********************************************************************/
    private void calculateItemValuesOf(int artnr)throws SQLException{
        String SQL;
        int wert = 0, bstnr = 0;

        Statement Stmt = con.createStatement(), Stmt2;
        ResultSet RS;

        SQL = "select a.artnr, lb.bstnr,(lb.stuecke * a.preis) as wert from \"LAGERBESTAND\" lb, \"ARTIKEL\" a\n" +
                "where a.artnr = " + artnr + " and lb.artnr = a.artnr order by lb.bstnr;";
        RS = Stmt.executeQuery(SQL);

        while(RS.next()){
            wert = RS.getInt("wert");
            bstnr = RS.getInt("bstnr");
            SQL = "update \"LAGERBESTAND\" set wert = " + wert + " where artnr = " + artnr + " and bstnr = " + bstnr;
            Stmt2 = con.createStatement();

            if(Stmt2.executeUpdate(SQL)!=1){
                Stmt.close();
                Stmt2.close();
                throw new SQLException("Insert fehlgeschlagen!");
            }

            Stmt2.close();
        }

        System.out.println("Alle Werte zur Artikelnummer " + artnr + " erfolgreich berechnet und hinzugefügt!");

        Stmt.close();

    }

    /*********************************************************************/
    /*            SELECT aller Artikelbasisdaten und Bestand             */
    /*********************************************************************/
    private void showBasedataFrom(int artnr) throws SQLException{
        String SQL;

        Statement Stmt = con.createStatement();
        ResultSet RS;

        // Eine SQL Select Anweisung
        SQL = "select * from  \"LAGERBESTAND\" lb, \"ARTIKEL\" a " +
                "where lb.artnr = " + artnr + " and lb.artnr = a.artnr";
        // SQL-Anweisung ausführen und Ergebnis in ein ResultSet schreiben
        RS = Stmt.executeQuery(SQL);

        System.out.println();
        // Das ResultSet Datensatzweise durchlaufen
        int i = 0;
        while (RS.next()) {

            if(i==0) {
                System.out.println("Stammdaten zum Artikel:     " + RS.getString("artbez"));
                System.out.println("Artikelnummer:              " + RS.getInt("artnr"));
                System.out.println("Preis:                      " + RS.getInt("preis"));
                System.out.println("Steuern:                    " + RS.getInt("steu"));
                System.out.println("--------------------------------");
                i++;
            }

            System.out.println("Bestand im Lager " + RS.getInt("lnr") + ":");
            System.out.println("Bestandsnummer:             " + RS.getInt("bstnr"));
            System.out.println("Stueckzahl:                 " + RS.getString("stuecke"));
            System.out.println("Mengeneinheit:              " + RS.getString("mge"));
            System.out.println("Wert:                       " + RS.getInt("wert"));
            System.out.println("--------------------------------");
        }
        Stmt.close();
    }

    /*********************************************************************/
    /*                  Aufnehmen einer Bestellung  6 c)                 */
    /*********************************************************************/

    private void createOrder(int knr, java.sql.Date bestdat)throws SQLException{

        String SQL;
        ResultSet RS;

        //Checken, ob es Kundennummer in der Datenbank gibt 6. c)
        SQL = "select knr from \"KUNDE\" where knr = ?";
        PreparedStatement ps1 = con.prepareStatement(SQL);
        ps1.setInt(1,knr);
        RS = ps1.executeQuery();
        if(RS.next()==false){
            ps1.close();
            throw new SQLException("Kunde nicht registriert!");
        }
        ps1.close();

        //Einfügen einer Zeile in die Tabelle BESTELLUNG 6. c)
        SQL = "insert into \"BESTELLUNG\"(bestdat, knr, status) values (? , ? , 1)";
        PreparedStatement ps2 = con.prepareStatement(SQL);
        ps2.setDate(1,bestdat); //Die erste Integer Zahl ist der Index des Fragezeichens, dass ersetzt werden soll.
        ps2.setInt(2, knr);
        ps2.executeUpdate();
        ps2.close();

        System.out.println("Bestellung erfolgreich aufgenommen!");

    }

    /*********************************************************************/
    /*       Zuordnen eines Bestandes zu einer Bestellung  6 d)          */
    /*********************************************************************/
    private void assignStockToAnOrder(int bestnr, int artnr, int eSTUECKE)throws SQLException{

        String SQL;
        ResultSet RS;
        int STUECKE = 0, DIFF = 0, lnr = 0, bstnr = 0;

        //Checken, ob es Bestellung in der Tabelle BESTELLUNG gibt 6. d0)
        SQL = "select bestnr from \"BESTELLUNG\" where bestnr = ?";
        PreparedStatement ps1 = con.prepareStatement(SQL);
        ps1.setInt(1,bestnr);
        RS = ps1.executeQuery();
        if(RS.next()==false){
            ps1.close();
            RS.close();
            throw new SQLException("Bestellung mit der Nr. "+bestnr+" existiert nicht!");
        }
        ps1.close();
        RS.close();

        //checken, ob es den Artikel in der Datenbank gibt 6. d0)
        SQL = "select artnr from \"ARTIKEL\" where artnr = ?";
        PreparedStatement ps2 = con.prepareStatement(SQL);
        ps2.setInt(1,artnr);
        RS = ps2.executeQuery();
        if(RS.next()==false){
            ps2.close();
            RS.close();
            throw new SQLException("Artikel mit der Artnr. " + artnr + " gibt es nicht!");
        }
        ps2.close();
        RS.close();


        //Checken, ob es die Anzahl eSTUECKE vom Artikel x in Mindestens einem Lager gibt 6. d1)
        SQL = "select stuecke, lnr, bstnr from \"LAGERBESTAND\" where stuecke >= ? and artnr = ?";
        PreparedStatement ps3 = con.prepareStatement(SQL);
        ps3.setInt(1,eSTUECKE);
        ps3.setInt(2,artnr);
        RS = ps3.executeQuery();
        if(RS.next()==false){
            ps3.close();
            RS.close();
            throw new SQLException("Kein Lagerbestand verfügt über eine ausreichende Stückzahl!");
        }else{
            //Einfügen eines neuen Lagerbestandes mit neuer Stückzahl und Bestellnummer d2)
            lnr = RS.getInt("lnr");
            STUECKE = RS.getInt("stuecke");
            bstnr = RS.getInt("bstnr");
            DIFF = STUECKE - eSTUECKE;  //Berechnen von DIFF
            SQL = "insert into \"LAGERBESTAND\"(artnr, lnr, stuecke, bestnr) values (?, ?, ?, ?)";
            PreparedStatement ps4 = con.prepareStatement(SQL);
            ps4.setInt(1,artnr);
            ps4.setInt(2,lnr);
            ps4.setInt(3,eSTUECKE);
            ps4.setInt(4,bestnr);
            SQL = "update \"LAGERBESTAND\" set stuecke = ? where bstnr = ?";
            PreparedStatement ps5 = con.prepareStatement(SQL);
            ps5.setInt(1, DIFF);
            ps5.setInt(2, bstnr);
            if(ps4.executeUpdate()!=1){
                ps3.close();
                ps4.close();
                ps5.close();
                RS.close();
                throw new SQLException("Fehler beim Einfügen eines neuen Bestandes!");
            }
            System.out.println("Neuer Bestand wurde eingefügt:");
            System.out.println("Artikelnr:      " + artnr);
            System.out.println("Lagernr:        " + lnr);
            System.out.println("Stückzahl:      " + eSTUECKE);
            System.out.println("Bestellnr:      " + bestnr);
            if(ps5.executeUpdate()!=1){
                ps3.close();
                ps4.close();
                ps5.close();
                RS.close();
                throw new SQLException("Fehler beim Einfügen der neuen Stueckzahl!");
            }
            System.out.println("Im Bestand Nr " + bstnr + " wurde die Stueckzahl auf " + DIFF+ " Stueck aktualisiert!");
            ps3.close();
            ps4.close();
        }
        ps3.close();
        RS.close();

        calculateItemValuesOf(artnr);
        System.out.println("Die Werte für Artikel Nr "+ artnr + " wurden im Bestand aktuallisiert!");
    }

    /**************************************************************************************************/
    /*                            AB hier Funktionen zum Zettel3                                      */
/**************************************************************************************************/

    /*********************************************************************/
    /*                      A8 Versangplanung                            */
    /*********************************************************************/







}


