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
    private static final String table = "Dron";
    private static final String insertStr="nazwa,opis,masa,ilosc_wirnikow,max_predkosc,max_czas_lotu,poziom_baterii,wspX,wspY,wspZ,stan,Punkt_kontrolny_id";
    private static final String selectStr="id,"+insertStr;
    private static final String updateStr="nazwa=?,opis=?,poziom_baterii=?";
    private PunktKontrolnyService punktKonService;

    public DroneService(MySQLController con, PunktKontrolnyService ser) {
        super(con);
        this.punktKonService = ser;
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
        String builder = ("INSERT INTO " + table + " (") +
                insertStr +
                ") VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement pstmt = mysql.getCon().prepareStatement(builder);){
            statSetVarPar(pstmt,1,d.getNazwa());
            statSetVarPar(pstmt,2,d.getOpis());
            statSetVarPar(pstmt,3,d.getMasa());
            statSetVarPar(pstmt,4,d.getIlosc_wirnikow());
            statSetVarPar(pstmt,5,d.getMax_predkosc());
            statSetVarPar(pstmt,6,d.getMax_czas_lotu());
            statSetVarPar(pstmt,7,d.getPoziom_baterii());
            statSetVarPar(pstmt,8,d.getWspx());
            statSetVarPar(pstmt,9,d.getWspy());
            statSetVarPar(pstmt,10,d.getWspz());
            statSetVarPar(pstmt,11,d.getStan());
            statSetVarPar(pstmt,12,d.getPunkt_kontrolny_id());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            return new Error(e.getMessage());
        }
        Error error = this.punktKonService.incCurrentDrones(d.getPunkt_kontrolny_id());
        try {
            if (error != null) {
                mysql.getCon().rollback(s);
                return error;
            } else mysql.getCon().commit();
        } catch (SQLException e) {
            return new Error(e.getMessage());
        }
        return null;
    }

    @Override
    public Error update(DataModel data) {
        String sql = String.format("UPDATE %s SET %s WHERE id=?", table, updateStr);
        Dron d = (Dron) data;
        try (PreparedStatement pstmt = mysql.getCon().prepareStatement(sql);) {
            statSetVarPar(pstmt,1,d.getNazwa());
            statSetVarPar(pstmt,2,d.getOpis());
            statSetVarPar(pstmt,3, d.getPoziom_baterii());
            statSetVarPar(pstmt,4,d.getId());
            pstmt.executeUpdate();
            mysql.getCon().commit();
        } catch (SQLException e) {
            return new Error(e.getMessage());
        }
        return null;
    }


    Error setStatus(Integer id, Integer status) {
        String sql = String.format("UPDATE %s SET stan = ? WHERE id=? AND stan <=?",table);
        try (PreparedStatement pstmt = mysql.getCon().prepareStatement(sql);) {
            statSetVarPar(pstmt,1,status);
            statSetVarPar(pstmt,2,id);
            statSetVarPar(pstmt,3,Dron.STATUS_PRZYDZIELONY_DO_ZADANIA);
            if(pstmt.executeUpdate()==0) return new Error("Unable to change drone status");
        } catch (SQLException e) {
            return new Error(e);
        }
        return null;
    }

    @Override
    public Error delete(Integer id) {
        return super.delete(id,table,true);
    }

    @Override
    public Error validate(DataModel data) {
        Dron d = (Dron) data;
        if(d.getMasa()==null) return new Error("Weight is required");
        if(d.getIlosc_wirnikow()==null) return new Error("Rotors number is required");
        if(d.getMax_predkosc()==null) return new Error("Max speed is required");
        if(d.getMax_czas_lotu()==null) return new Error("Max flight time is required");
        if(d.getPoziom_baterii()==null) return new Error("Battery level is required");
        if(d.getStan()==null) return new Error("State is required");
        if(d.getStan()<=0) {
            if (d.getPunkt_kontrolny_id() == null)
                return new Error("Drone point is required");
        } else {
            if(d.getWspx()==null) return new Error("X coordinate is required");
            if(d.getWspy()==null) return new Error("Y coordinate is required");
            if(d.getWspz()==null) return new Error("Z coordinate is required");
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
            d.setWspx((Float)res.getObject(9));
            d.setWspy((Float)res.getObject(10));
            d.setWspz((Float)res.getObject(11));
            d.setStan(res.getInt(12));
            d.setPunkt_kontrolny_id((Integer) res.getObject(13));
            list.add(d);
        }
        res.close();
        return list;
   }

    public List<DataModel> find(ArrayList<FilterParam> filterList) throws SQLException {
        try (PreparedStatement pstmt = super.find(filterList,selectStr,table);){
            ResultSet rs = pstmt.executeQuery();
            return  this.parseToModel(rs);
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }
}
