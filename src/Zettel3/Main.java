package Zettel3;

import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {

        int TTL = 3;
        boolean error;

        PostgresJDBC customer;


        try {
            customer = new PostgresJDBC();
            customer.menue();
        } catch (SQLException e1) {
            System.out.println(e1.getMessage());
            error = true;
        }catch (IOException e2){
            System.out.println(e2.getMessage());
            error = true;
        }

    }
}
