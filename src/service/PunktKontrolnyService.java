package service;

import common.FilterParam;
import dataModels.DataModel;
import dataModels.Punkt_kontrolny;
import dataModels.Uzytkownik;
import databaseController.MySQLController;
import javafx.scene.control.Alert;
import main.Main;

import javax.sound.sampled.DataLine;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static common.CommonFunc.statSetVarPar;

/**
 * Created by no-one on 18.11.16.
 */
public class PunktKontrolnyService extends Service implements ServiceInterface {

    private static final String table = "Punkt_kontrolny";
    private static final String insertStr="nazwa,max_ilosc_dronow,obecna_ilosc_dronow,wspX,wspY,wspZ";
    private static final String selectStr="id,"+insertStr;
    private static final String updateStr="nazwa=?,max_ilosc_dronow=?";
    private static final String incStr="obecna_ilosc_dronow=obecna_ilosc_dronow+1";

    public PunktKontrolnyService(MySQLController con) {
        super(con);
    }

    @Override
    public Error insert(DataModel data) {
        Punkt_kontrolny p = (Punkt_kontrolny) data;
        StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO "+ table +" (");
        builder.append(insertStr);
        builder.append(") VALUES(?,?,?,?,?,?)");
        System.out.println(builder.toString());
        try (PreparedStatement pstmt = mysql.getCon().prepareStatement(builder.toString());){
            statSetVarPar(pstmt, 1, p.getNazwa());
            statSetVarPar(pstmt, 2, p.getMax_ilosc_dronow());
            statSetVarPar(pstmt, 3, p.getObecna_ilosc_dronow());
            statSetVarPar(pstmt, 4,p.getWspx());
            statSetVarPar(pstmt, 5, p.getWspy());
            statSetVarPar(pstmt, 6,p.getWspz());
            pstmt.executeUpdate();
            mysql.getCon().commit();
        } catch (SQLException e) {
            return new Error(e.getMessage());
        }
        return null;
    }



    @Override
    public Error update(DataModel data) {
        String sql = String.format("UPDATE %s SET %s WHERE id=?", table, updateStr);
        Punkt_kontrolny p = (Punkt_kontrolny) data;

        try (PreparedStatement pstmt = mysql.getCon().prepareStatement(sql);) {
            statSetVarPar(pstmt, 1,p.getNazwa());
            statSetVarPar(pstmt, 2,p.getMax_ilosc_dronow());
            statSetVarPar(pstmt, 3, p.getId());
            pstmt.executeUpdate();
            mysql.getCon().commit();
        } catch (SQLException e) {
            return new Error(e.getMessage());
        }
        return null;
    }

    public Error incCurrentDrones(int id){
        String sql = String.format("UPDATE %s SET %s WHERE id=? AND obecna_ilosc_dronow < max_ilosc_dronow", table, incStr);
        try (PreparedStatement pstmt = mysql.getCon().prepareStatement(sql);) {
            pstmt.setInt(1,id);
            int updated = pstmt.executeUpdate();
            if (updated!=1) return new Error("Failed to assign drone to drone point.");
        } catch (SQLException e) {
            return new Error(e.getMessage());
        }
        return null;
    }

    @Override
    public Error delete(Integer id) {
        return super.delete(id,table,true);
    }


    @Override
    public Error validate(DataModel data) {
        Punkt_kontrolny p =  (Punkt_kontrolny) data;
        if(p.getMax_ilosc_dronow()==null) return new Error("Drone places number is required");
        if(p.getWspx()==null) return new Error("X coordinate is required");
        if(p.getWspy()==null) return new Error("Y coordinate is required");
        if(p.getWspz()==null) return new Error("Z coordinate is required");
        if(p.getObecna_ilosc_dronow()==null) return new Error("Current drone number is required");
        if(p.getObecna_ilosc_dronow()>p.getMax_ilosc_dronow()) return new Error("Current drone number cannot be greater than max drones number");
        return null;
    }

    public List<DataModel> find(ArrayList<FilterParam> filterList) throws SQLException {
        try (PreparedStatement pstmt = super.find(filterList,selectStr,table);){
            ResultSet rs = pstmt.executeQuery();
            return  this.parseToModel(rs);
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public List<DataModel> parseToModel(ResultSet res) throws SQLException {
        ArrayList<DataModel> list = new ArrayList<>();
        while (res.next()){
            Punkt_kontrolny p = new Punkt_kontrolny();
            p.setId(res.getInt(1));
            p.setNazwa(res.getString(2));
            p.setMax_ilosc_dronow(res.getInt(3));
            p.setObecna_ilosc_dronow(res.getInt(4));
            p.setWspx(res.getFloat(5));
            p.setWspy(res.getFloat(6));
            p.setWspz(res.getFloat(7));
            list.add(p);
        }
        res.close();
        return list;
    }
}
