package service;

import common.FilterParam;
import dataModels.DataModel;
import dataModels.Punkt_na_trasie;
import dataModels.Trasa;
import dataModels.Uzytkownik;
import databaseController.MySQLController;
import main.Main;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by no-one on 18.11.16.
 */
public class TrasaService extends Service implements ServiceInterface {

    private static final String table = "Trasa";
    private static final String insertStr="nazwa,Uzytkownik_id";
    private static final String selectStr=insertStr;
    private static final String deleteSql="DELETE FROM"+table+" WHERE nazwa=? AND Uzytkownik_id=?";

    public TrasaService(MySQLController con){super(con);}

    @Override
    public Error insert(DataModel data) {
        return new Error("not to use!");
    }

    @Override
    public Error update(DataModel data) {
        return new Error("You can't modify routes");
    }

    @Override
    public Error delete(Integer id) {
        return new Error("not to use");
    }

    public Error delete(String name, Integer id) {
        String sql = String.format("DELETE FROM %s WHERE nazwa=? AND Uzytkownik_id=?",table);
        try(PreparedStatement pstmt = mysql.getCon().prepareStatement(sql)){
            pstmt.setString(1,name);
            pstmt.setInt(2,id);
            pstmt.execute();
            Main.polozenieService.delete(-1);
            mysql.getCon().commit();
        } catch (SQLException e) {
            return new Error(e.getMessage());
        }
        return null;
    }

    @Override
    public Error validate(DataModel data) {
        Trasa t = (Trasa)data;
        if(t.getNazwa()==null) return new Error("Route name is required");
        if(t.getUzytkownik_id()==null) return new Error("User id is required");
        return null;
    }

    public List<DataModel> find(Integer userId) throws SQLException {
        ArrayList<FilterParam> param = new ArrayList<>();
        if(userId!=null) param.add(FilterParam.newF("Uzytkownik_id", "=", userId));

        try (PreparedStatement pstmt = super.find(param, selectStr, table);){
            ResultSet rs = pstmt.executeQuery();
            return  this.parseToModel(rs);
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public List<DataModel> parseToModel(ResultSet res) throws SQLException {
        ArrayList<DataModel> list = new ArrayList<>();

        while(res.next() ){
            Trasa tr = new Trasa();
            tr.setNazwa(res.getString(1));
            tr.setUzytkownik_id(res.getInt(2));
            list.add(tr);
        }
        res.close();
        return list;
    }

    public Error insert(Trasa t, List<Punkt_na_trasie> list) {
        Savepoint s;
        try {
            s = mysql.getCon().setSavepoint();
        } catch (SQLException e) {
            return new Error(e.getMessage());
        }
        String sql = String.format("INSERT INTO %s (%s) VALUES (?,?)",table,insertStr);
        try(PreparedStatement pstmt = mysql.getCon().prepareStatement(sql)){
            pstmt.setString(1,t.getNazwa());
            pstmt.setInt(2,t.getUzytkownik_id());
            int inserted = pstmt.executeUpdate();
            if(inserted==0) throw new SQLException("Unable to create route");
        } catch (SQLException e) {
            return new Error(e.getMessage());
        }
        try {
            Error err = Main.polozenieService.insertPolozenieAndSetIds(list);
            if(err != null){
                this.rollback(s);
                return err;
            }
            err = Main.punktNaTrasieService.insertRoutePoints(list);
            if (err != null) {
                this.rollback(s);
                return err;
            } else
                mysql.getCon().commit();
        } catch (SQLException e) {
            return new Error(e.getMessage());
        }
        return null;
    }

    private void rollback(Savepoint s) throws SQLException {
        mysql.getCon().rollback(s);
        mysql.getCon().commit();
    }

}
