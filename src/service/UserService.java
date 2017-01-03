package service;

import common.CommonFunc;
import dataModels.DataModel;
import dataModels.Uzytkownik;
import databaseController.MySQLController;
import main.Main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static common.CommonFunc.statSetVarPar;

/**
 * Created by no-one on 18.11.16.
 */
public class UserService extends Service implements ServiceInterface{

    private static final String table = "Uzytkownik";
    private static final String insertStr="imie,nazwisko,data_urodzenia,login,telefon,haslo,poziom_uprawnien";
    private static final String selectStr="id,"+insertStr;
    private static final String updateStr="imie=?,nazwisko=?,data_urodzenia=?,telefon=?";

    public UserService(MySQLController con){
        super(con);
    }

    @Override
    public Error insert(DataModel data) {
        Uzytkownik uz = (Uzytkownik) data;
        StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO "+ table +"(");
        builder.append(insertStr);
        builder.append(") VALUES(?,?,?,?,?,?,?)");

        try (PreparedStatement pstmt = mysql.getCon().prepareStatement(builder.toString());){
            statSetVarPar(pstmt, 1, uz.getImie());
            statSetVarPar(pstmt, 2, uz.getNazwisko());
            statSetVarPar(pstmt, 3, uz.getData_urodzenia());
            statSetVarPar(pstmt, 4,uz.getLogin());
            statSetVarPar(pstmt, 5, uz.getTelefon());
            statSetVarPar(pstmt, 6,uz.getHaslo());
            statSetVarPar(pstmt, 7, uz.getPoziom_uprawnien());
            pstmt.executeUpdate();
            mysql.getCon().commit();
            pstmt.close();
            return null;
        } catch (SQLException e) {
            return new Error(e.getMessage());
        }
    }

    @Override
    public Error update(DataModel data) {
        Uzytkownik uz = (Uzytkownik) data;

        String sql = String.format("UPDATE %s SET %s WHERE ID=?", table, updateStr);
        try (PreparedStatement pstmt = mysql.getCon().prepareStatement(sql);) {
            statSetVarPar(pstmt, 1,uz.getImie());
            statSetVarPar(pstmt, 2,uz.getNazwisko());
            statSetVarPar(pstmt, 3,uz.getData_urodzenia());
            statSetVarPar(pstmt, 4,uz.getTelefon());
            statSetVarPar(pstmt, 5, Main.authenticatedUser.getId());
            pstmt.executeUpdate();
            mysql.getCon().commit();
            Main.authenticatedUser = uz;
        } catch (SQLException e) {
            return new Error(e.getMessage());
        }
        return null;
    }

    @Override
    public Error delete(Integer id) {
        return new Error("You can't delete user");
    }

    @Override
    public Error validate(DataModel data) {
        Uzytkownik uz = (Uzytkownik) data;
        if(uz.getImie() == null || uz.getImie().length()==0) return new Error("Imie jest wymagane");
        if(uz.getNazwisko() == null || uz.getNazwisko().length()==0) return new Error("Nazwisko jest wymagane");
        if(uz.getLogin() == null || uz.getLogin().length()==0) return new Error("Login jest wymagany");
        if(uz.getHaslo()==null || uz.getHaslo().length()==0) return new Error("Haslo jest wymagane");
        if(uz.getPoziom_uprawnien() == null || uz.getPoziom_uprawnien()==null) return new Error("Błędny poziom uprawnień");
        return null;
    }

    @Override
    public List<DataModel> parseToModel(ResultSet res) throws SQLException {
        Uzytkownik uz = new Uzytkownik();
        ArrayList<DataModel> list = new ArrayList<>();

        if( res.next() ){
            uz.setId(res.getInt(1));
            uz.setImie(res.getString(2));
            uz.setNazwisko(res.getString(3));
            uz.setData_urodzenia(res.getDate(4));
            uz.setLogin(res.getString(5));
            uz.setTelefon(res.getString(6));
            uz.setHaslo(res.getString(7));
            uz.setPoziom_uprawnien(res.getInt(8));
            list.add(uz);
        }
        res.close();
        return list;
    }

    public Error changePassword(String newPass) {
        String newPassword = CommonFunc.hashPass(newPass);
        String sql = String.format("UPDATE %s SET haslo=? WHERE id=?", table);
        try ( PreparedStatement pstmt = mysql.getCon().prepareStatement(sql);){
            statSetVarPar(pstmt, 1, newPassword);
            statSetVarPar(pstmt, 2, Main.authenticatedUser.getId());
            pstmt.executeUpdate();
            mysql.getCon().commit();
            Main.authenticatedUser.setHaslo(newPassword);
        } catch (SQLException e) {
            return new Error(e.getMessage());
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
        PreparedStatement pstmt = mysql.getCon().prepareStatement(String.format("SELECT %s FROM %s WHERE login=?", selectStr, table));
        pstmt.setString(1, login);
        ResultSet rs = pstmt.executeQuery();
        Uzytkownik uz = (Uzytkownik) parseToModel(rs).get(0);
        pstmt.close();
        return uz;
    }
}
