package service;

import common.CommonFunc;
import common.Consts;
import dataModels.DataModel;
import dataModels.Uzytkownik;
import databaseController.MySQLController;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by no-one on 18.11.16.
 */
public class UserService extends Service implements ServiceInterface{

    private final String table = "Uzytkownik";
    private final String insertStr="imie,nazwisko,data_urodzenia,login,telefon,haslo,poziom_uprawnien";
    private final String selectStr="id,imie,nazwisko,data_urodzenia,login,telefon,haslo,poziom_uprawnien";


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
        pstmt.close();
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
    public Uzytkownik parseToModel(ResultSet res) throws SQLException {
        Uzytkownik uz = new Uzytkownik();
        if( res.next() ){
            uz.setId((long) res.getInt(1));
            uz.setImie(res.getString(2));
            uz.setNazwisko(res.getString(3));
            uz.setData_urodzenia(res.getDate(4));
            uz.setLogin(res.getString(5));
            uz.setTelefon(res.getString(6));
            uz.setHaslo(res.getString(7));
            uz.setPoziom_uprawnien((long) res.getInt(8));
            return uz;
        } else return null;
    }

    public Uzytkownik authenticate(String login, String pass) throws SQLException {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT "+selectStr+" FROM "+ this.table +" WHERE login=?");
        PreparedStatement pstmt = mysql.con.prepareStatement(builder.toString());
        pstmt.setString(1, login);
        ResultSet rs = pstmt.executeQuery();
        Uzytkownik uz = parseToModel(rs);
        if(uz == null) return null;
        if(CommonFunc.comparePass(pass, uz.getHaslo())) return uz;
        return null;
    }
}
