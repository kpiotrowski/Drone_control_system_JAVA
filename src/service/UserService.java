package service;

import common.CommonFunc;
import dataModels.DataModel;
import dataModels.Uzytkownik;
import databaseController.MySQLController;
import main.Main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by no-one on 18.11.16.
 */
public class UserService extends Service implements ServiceInterface{

    private final String table = "Uzytkownik";
    private final String insertStr="imie,nazwisko,data_urodzenia,login,telefon,haslo,poziom_uprawnien";
    private final String selectStr="id,"+insertStr;
    private final String updateStr="imie=?,nazwisko=?,data_urodzenia=?,telefon=?";

    public UserService(MySQLController con){
        super(con);
    }

    @Override
    public Error insert(DataModel data) {
        Uzytkownik uz = (Uzytkownik) data;
        StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO "+ this.table +"(");
        builder.append(insertStr);
        builder.append(") VALUES(?,?,?,?,?,?,?)");

        PreparedStatement pstmt = null;
        try {
            pstmt = mysql.con.prepareStatement(builder.toString());
            pstmt.setString(1, uz.getImie());
            pstmt.setString(2, uz.getNazwisko());
            pstmt.setDate(3, uz.getData_urodzenia());
            pstmt.setString(4,uz.getLogin());
            pstmt.setString(5, uz.getTelefon());
            pstmt.setString(6,uz.getHaslo());
            pstmt.setLong(7, uz.getPoziom_uprawnien());
            pstmt.executeUpdate();
            mysql.con.commit();
            pstmt.close();
            return null;
        } catch (SQLException e) {
            return new Error(e.toString());
        }
    }

    @Override
    public Error update(DataModel data) {
        Uzytkownik uz = (Uzytkownik) data;

        String sql = "UPDATE " + this.table + " SET " + updateStr + " WHERE ID=?";
        try (PreparedStatement pstmt = mysql.con.prepareStatement(sql);) {
            pstmt.setString(1,uz.getImie());
            pstmt.setString(2,uz.getNazwisko());
            pstmt.setDate(3,uz.getData_urodzenia());
            pstmt.setString(4,uz.getTelefon());
            pstmt.setInt(5, Main.authenticatedUser.getId());
            pstmt.executeUpdate();
            mysql.con.commit();
            Main.authenticatedUser = uz;
        } catch (SQLException e) {
            return new Error(e.toString());
        }
        return null;
    }

    @Override
    public void delete(DataModel data) { System.out.println("Nawet o tym nie myśl");}

    @Override
    public String validate(DataModel data) {
        Uzytkownik uz = (Uzytkownik) data;
        if(uz.getImie() == null || uz.getImie().length()==0) return "Imie jest wymagane";
        if(uz.getNazwisko() == null || uz.getNazwisko().length()==0) return "Nazwisko jest wymagane";
        if(uz.getLogin() == null || uz.getLogin().length()==0) return "Login jest wymagany";
        if(uz.getHaslo()==null || uz.getHaslo().length()==0) return "Haslo jest wymagane";
        if(uz.getPoziom_uprawnien() == null || uz.getPoziom_uprawnien()==null) return "Błędny poziom uprawnień";
        return "";
    }

    @Override
    public Uzytkownik parseToModel(ResultSet res) throws SQLException {
        Uzytkownik uz = new Uzytkownik();
        if( res.next() ){
            uz.setId(res.getInt(1));
            uz.setImie(res.getString(2));
            uz.setNazwisko(res.getString(3));
            uz.setData_urodzenia(res.getDate(4));
            uz.setLogin(res.getString(5));
            uz.setTelefon(res.getString(6));
            uz.setHaslo(res.getString(7));
            uz.setPoziom_uprawnien(res.getInt(8));
            res.close();
            return uz;
        }
        res.close();
        return null;
    }

    public Error changePassword(String newPass) {
        String newPassword = CommonFunc.hashPass(newPass);

        String sql = "UPDATE "+this.table+" SET haslo=? WHERE id=?";
        try ( PreparedStatement pstmt = mysql.con.prepareStatement(sql);){
            pstmt.setString(1, newPassword);
            pstmt.setInt(2, Main.authenticatedUser.getId());
            pstmt.executeUpdate();
            mysql.con.commit();
            Main.authenticatedUser.setHaslo(newPassword);
        } catch (SQLException e) {
            return new Error(e.toString());
        }
        return null;
    }

    public Uzytkownik authenticate(String login, String pass) throws SQLException {
        Uzytkownik uz = getUser(login);
        if(uz == null) return null;
        if(CommonFunc.comparePass(pass, uz.getHaslo())) return uz;
        return null;
    }

    public Uzytkownik authUserReload() throws SQLException {
        Uzytkownik uz = getUser(Main.authenticatedUser.getLogin());
        if(uz==null) throw new SQLException("Niepowodzenie podczas pobierania danych użytkownika");
        return uz;
    }

    private Uzytkownik getUser(String login) throws SQLException {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT "+selectStr+" FROM "+ this.table +" WHERE login=?");
        PreparedStatement pstmt = mysql.con.prepareStatement(builder.toString());
        pstmt.setString(1, login);
        ResultSet rs = pstmt.executeQuery();
        Uzytkownik uz = parseToModel(rs);
        pstmt.close();
        return uz;
    }

}
