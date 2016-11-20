package databaseController;

import common.Consts;
import javafx.concurrent.Task;
import main.Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


/**
 * Created by no-one on 11/17/16.
 */
public class MySQLController {

    public Connection con;
    public boolean valid = true;
    private String conUrl;
    private Properties props;

    public MySQLController(String user, String password, String host, String port) throws SQLException {
        conUrl = "jdbc:mysql://"+host+":"+port+"/"+Consts.dbName;
        props = new Properties();
        props.put("user", user);
        props.put("password", password);

        System.out.println("Łączenie się z bazą danych");
        DriverManager.setLoginTimeout(5);
        this.con = DriverManager.getConnection(conUrl, props);
        System.out.println("Pomyślnie połączono się z bazą danych");
        con.setAutoCommit(false);
        con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        this.valid = true;
    }

    /**
     * dnawia połączenie z bazą danych
     */
    private void refresh(){
        Task t = new Task() {
            @Override
            protected Connection call() throws Exception {
                if(!con.isClosed()) con.close();
                DriverManager.setLoginTimeout(5);
                System.out.println("refreshing connection");
                return DriverManager.getConnection(conUrl, props);
            }
        };
        t.setOnSucceeded( (e) -> {
            this.con = (Connection) t.getValue();
            pinger();
        });
        t.setOnFailed( (e) -> {
            try {
                Thread.sleep(5000);
                refresh();
            } catch (InterruptedException e1) { refresh(); }
        });
        new Thread(t).start();
    }

    /**
     * PInguje bazę banych, w razie błędu połączenia stara się je odnowić
     */
    public void pinger(){
        Task t = new Task(){
            @Override
            protected Object call() throws SQLException, InterruptedException {
                Thread.sleep(5000);
                boolean valid = con.isValid(5);
                System.out.println(valid);
                if (valid) return true;
                else throw new SQLException();
            };
        };
        t.setOnFailed((e) -> {
            Main.gui.setDatabaseStatus("Błąd połączenia z bazą danych", false);
            this.valid=false;
            refresh();
        });
        t.setOnSucceeded((e) -> {
            boolean valid = (boolean) t.getValue();
            if (valid) {
                Main.gui.setDatabaseStatus("Połączono z bazą danych", true);
                this.valid=true;
                pinger();
            }
            else {
                Main.gui.setDatabaseStatus("Błąd połączenia z bazą danych", false);
                this.valid=false;
                refresh();
            }
        });
        new Thread(t).start();
    }
}
