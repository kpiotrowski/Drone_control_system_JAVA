package service;

import common.Consts;
import dataModels.DataModel;
import dataModels.Uzytkownik;
import databaseController.MySQLController;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by no-one on 18.11.16.
 */
public class UserService extends Service implements ServiceInterface{

    private final String table = "Uzytkownik";
    private final String insertStr="imie,nazwisko,data_urodzenia,login,telefon,haslo,poziom_uprawnien";

    public UserService(MySQLController con){
        super(con);

    }

    @Override
    public void insert(DataModel data) throws SQLException {
        Uzytkownik uz = (Uzytkownik) data;
        StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO "+ this.table +"(");
        builder.append(insertStr);
        builder.append(") VALUES(?,?,?,?,?,?,?)");

        PreparedStatement pstmt = mysql.con.prepareStatement(builder.toString());
        pstmt.setString(1, uz.getImie());
        pstmt.setString(2, uz.getNazwisko());
        pstmt.setDate(3, uz.getData_urodzenia());
        pstmt.setString(4,uz.getLogin());
        pstmt.setString(5, uz.getTelefon());
        pstmt.setString(6,uz.getHaslo());
        pstmt.setLong(7, uz.getPoziom_uprawnien());
        pstmt.executeUpdate();
    }

    @Override
    public void update(DataModel data) {

    }

    @Override
    public void delete(DataModel data) {

    }

    @Override
    public String validate(DataModel data) {
        Uzytkownik uz = (Uzytkownik) data;
        if(uz.getImie().length()==0) return "Imie jest wymagane";
        if(uz.getNazwisko().length()==0) return "Nazwisko jest wymagane";
        if(uz.getLogin().length()==0) return "Login jest wymagany";
        if(uz.getHaslo().length()==0) return "Haslo jest wymagane";
        if(uz.getPoziom_uprawnien()==null) return "Błędny poziom uprawnień";
        return "";
    }

    @Override
    public Uzytkownik parseToModel() {
        return null;
    }
}
