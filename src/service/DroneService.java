package service;

import common.FilterParam;
import dataModels.DataModel;
import dataModels.Dron;
import dataModels.Punkt_kontrolny;
import databaseController.MySQLController;
import main.Main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.List;

import static common.CommonFunc.statSetVarPar;

/**
 * Created by no-one on 18.11.16.
 */
public class DroneService extends Service implements ServiceInterface{
    private final String table = "Dron";
    private final String insertStr="nazwa,opis,masa,ilosc_wirnikow,max_predkosc,max_czas_lotu,poziom_baterii,wspX,wspY,wspZ,stan,Punkt_kontrolny_id";
    private final String selectStr="id,"+insertStr;
    private final String updateStr="nazwa=?,opis=?,poziom_baterii=?";

    public DroneService(MySQLController con) {
        super(con);
    }

    @Override
    public Error insert(DataModel data) {
        Savepoint s;
        try {
            s = mysql.getCon().setSavepoint();
        } catch (SQLException e) {
            return new Error(e.getMessage());
        }

        Dron d = (Dron) data;
        StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO "+ this.table +" (");
        builder.append(insertStr);
        builder.append(") VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
        try (PreparedStatement pstmt = mysql.getCon().prepareStatement(builder.toString());){
            statSetVarPar(pstmt,1, d.getNazwa());
            statSetVarPar(pstmt,2, d.getOpis());
            statSetVarPar(pstmt,3, d.getMasa());
            statSetVarPar(pstmt,4,d.getIlosc_wirnikow());
            statSetVarPar(pstmt,5, d.getMax_predkosc());
            statSetVarPar(pstmt,6,d.getMax_czas_lotu());
            statSetVarPar(pstmt,7,d.getPoziom_baterii());
            statSetVarPar(pstmt,8,d.getWspx());
            statSetVarPar(pstmt,9,d.getWspy());
            statSetVarPar(pstmt,10,d.getWspz());
            statSetVarPar(pstmt,11,d.getStan());
            statSetVarPar(pstmt,12,d.getPunkt_kontrolny_id());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            return new Error(e.toString());
        }
        Error error = Main.punktKontrolnyService.incCurrentDrones(d.getPunkt_kontrolny_id());
        try {
            if (error != null) {
                mysql.getCon().rollback(s);
                return error;
            } else {
                mysql.getCon().commit();
            }
        } catch (SQLException e) {
            return new Error(e.getMessage());
        }
        return null;
    }

    @Override
    public Error update(DataModel data) {
        String sql = String.format("UPDATE %s SET %s WHERE id=?", this.table, updateStr);
        Dron d = (Dron) data;
        try (PreparedStatement pstmt = mysql.getCon().prepareStatement(sql);) {
            statSetVarPar(pstmt,1,d.getNazwa());
            statSetVarPar(pstmt,2,d.getOpis());
            statSetVarPar(pstmt,3, d.getPoziom_baterii());
            statSetVarPar(pstmt,4,d.getId());
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
        Dron d = (Dron) data;
        if(d.getMasa()==null) return new Error("Waga drona jest wymagana");
        if(d.getIlosc_wirnikow()==null) return new Error("Ilość wirników jest wymagana");
        if(d.getMax_predkosc()==null) return new Error("Maksymalna prędkość jest wymagana");
        if(d.getMax_czas_lotu()==null) return new Error("Maksymalny czas lotu jest wymaganay");
        if(d.getPoziom_baterii()==null) return new Error("Poziom baterii jest wymagany");
        if(d.getStan()==null) return new Error("Stan jest wymaganay");
        if(d.getStan()<=0) {
            if (d.getPunkt_kontrolny_id() == null)
                return new Error("Przynalezność do punktu kontrolnego jest wymagana");
        } else {
            if(d.getWspx()==null) return new Error("Współrzędna X drona jest wymagana");
            if(d.getWspy()==null) return new Error("Wspołrzędna Y drona jest wymagana");
            if(d.getWspz()==null) return new Error("Wspołrzędna Z drona jest wymagana");
        }
        return null;
    }

    @Override
    public List<DataModel> parseToModel(ResultSet res) throws SQLException {
        ArrayList<DataModel> list = new ArrayList<>();
        while (res.next()){
            Dron d = new Dron();
            d.setId(res.getInt(1));
            d.setNazwa(res.getString(2));
            d.setOpis(res.getString(3));
            d.setMasa(res.getFloat(4));
            d.setIlosc_wirnikow(res.getInt(5));
            d.setMax_predkosc(res.getFloat(6));
            d.setMax_czas_lotu(res.getFloat(7));
            d.setPoziom_baterii(res.getFloat(8));
            d.setWspx(res.getFloat(9));
            d.setWspy(res.getFloat(10));
            d.setWspz(res.getFloat(11));
            d.setStan(res.getInt(12));
            d.setPunkt_kontrolny_id(res.getInt(13));
            list.add(d);
        }
        res.close();
        return list;
   }

    public List<DataModel> find(ArrayList<FilterParam> filterList) throws SQLException {
        try (PreparedStatement pstmt = super.find(filterList,this.selectStr,this.table);){
            ResultSet rs = pstmt.executeQuery();
            return  this.parseToModel(rs);
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }
}
