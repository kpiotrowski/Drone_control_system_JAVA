package databaseController;

import common.Consts;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


/**
 * Created by no-one on 11/17/16.
 */
public class MySQLController {

    public Connection con;

    public MySQLController(String user, String password, String host, String port) throws SQLException {

        String conUrl = "jdbc:mysql://"+host+":"+port+"/"+Consts.dbName;
        Properties props = new Properties();
        props.put("user", user);
        props.put("password", password);

        System.out.println("Łączenie się z bazą danych");
        DriverManager.setLoginTimeout(5);
        this.con = DriverManager.getConnection(conUrl, props);
        System.out.println("Pomyślnie połączono się z bazą danych");
    }
}
