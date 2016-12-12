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

/**
 * Created by no-one on 18.11.16.
 */
public class PunktKontrolnyService extends Service implements ServiceInterface {

    private final String table = "Punkt_kontrolny";
    private final String insertStr="nazwa,max_ilosc_dronow,obecna_ilosc_dronow,wspX,wspY,wspZ";
    private final String selectStr="id,"+insertStr;
    private final String updateStr="nazwa=?,max_ilosc_dronow=?";

    public PunktKontrolnyService(MySQLController con) {
        super(con);
    }

    @Override
    public Error insert(DataModel data) {
        Punkt_kontrolny p = (Punkt_kontrolny) data;
        StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO "+ this.table +" (");
        builder.append(insertStr);
        builder.append(") VALUES(?,?,?,?,?,?)");
        System.out.println(builder.toString());
        try (PreparedStatement pstmt = mysql.getCon().prepareStatement(builder.toString());){
            pstmt.setString(1, p.getNazwa());
            pstmt.setInt(2, p.getMax_ilosc_dronow());
            pstmt.setInt(3, p.getObecna_ilosc_dronow());
            pstmt.setFloat(4,p.getWspx());
            pstmt.setFloat(5, p.getWspy());
            pstmt.setFloat(6,p.getWspz());
            pstmt.executeUpdate();
            mysql.getCon().commit();
        } catch (SQLException e) {
            return new Error(e.toString());
        }
        return null;
    }

    @Override
    public Error update(DataModel data) {
        String sql = String.format("UPDATE %s SET %s WHERE id=?", this.table, updateStr);
        Punkt_kontrolny p = (Punkt_kontrolny) data;

        try (PreparedStatement pstmt = mysql.getCon().prepareStatement(sql);) {
            pstmt.setString(1,p.getNazwa());
            pstmt.setLong(2,p.getMax_ilosc_dronow());
            pstmt.setInt(3, p.getId());
            pstmt.executeUpdate();
            mysql.getCon().commit();
        } catch (SQLException e) {
            return new Error(e.toString());
        }
        return null;
    }

    @Override
    public Error delete(Integer id) {
        return super.delete(id,this.table);
    }


    @Override
    public Error validate(DataModel data) {
        Punkt_kontrolny p =  (Punkt_kontrolny) data;
        if(p.getMax_ilosc_dronow()==null) return new Error("Ilość dronów jest wymagana");
        if(p.getWspx()==null) return new Error("Współrzędna X jest wymagana");
        if(p.getWspy()==null) return new Error("Współrzędna Y jest wymagana");
        if(p.getWspz()==null) return new Error("Współrzędna Z jest wymagana");
        if(p.getObecna_ilosc_dronow()==null) return new Error("Obenca ilość dronów jest wymagana");
        if(p.getObecna_ilosc_dronow()>p.getMax_ilosc_dronow()) return new Error("Obecna ilość dronów nie może być większa od maksymalnej");
        return null;
    }

    public List<DataModel> find(ArrayList<FilterParam> filterList) throws SQLException {
        try (PreparedStatement pstmt = super.find(filterList,this.selectStr,this.table);){
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
