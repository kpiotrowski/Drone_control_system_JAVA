package service;

import common.FilterParam;
import dataModels.DataModel;
import dataModels.Dron;
import dataModels.Zadanie;
import databaseController.MySQLController;
import main.Main;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static common.CommonFunc.statSetVarPar;

/**
 * Created by no-one on 18.11.16.
 */
public class ZadanieService extends Service implements ServiceInterface{

    private static final String table = "Zadanie";
    private static final String selectStr = "id,data_rozpoczenia,szacowana_dlugosc,typ,Trasa_nazwa,Trasa_Uzytkownik_id,Uzytkownik_id,Dron_id,Punkt_koncowy_id,stan";
    private static final String insertSql = "data_rozpoczenia,typ,Trasa_nazwa,Trasa_Uzytkownik_id,Uzytkownik_id,Dron_id,Punkt_koncowy_id,stan";

    public ZadanieService(MySQLController con){
        super(con);
    }

    @Override
    public Error insert(DataModel data) {
        Zadanie z = (Zadanie)data;
        String sql = String.format("INSERT INTO %s (%s) VALUES (?,?,?,?,?,?,?,?)",table,insertSql);
        try (PreparedStatement pstmt = mysql.getCon().prepareStatement(sql);) {
            statSetVarPar(pstmt,1,z.getData_rozpoczenia());
            statSetVarPar(pstmt,2,z.getTyp());
            statSetVarPar(pstmt,3,z.getTrasa_nazwa());
            statSetVarPar(pstmt,4,z.getTrasa_uzytkownik_id());
            statSetVarPar(pstmt,5,z.getUzytkownik_id());
            statSetVarPar(pstmt,6,z.getDron_id());
            statSetVarPar(pstmt,7,z.getPunkt_koncowy_id());
            statSetVarPar(pstmt,8,z.getStan());
            pstmt.executeUpdate();
            this.mysql.getCon().commit();
        }catch (SQLException e) {
            return new Error(e);
        }
        return null;
    }

    @Override
    public Error update(DataModel data) {
        Zadanie z = (Zadanie) data;
        String sql = String.format("UPDATE %s SET data_rozpoczenia = ? WHERE id=? AND stan <=?",table);
        try (PreparedStatement pstmt = mysql.getCon().prepareStatement(sql);) {
            statSetVarPar(pstmt,1,z.getData_rozpoczenia());
            statSetVarPar(pstmt,2,z.getId());
            statSetVarPar(pstmt,3,Zadanie.STATUS_NOWE_ZADANIE);
            if(pstmt.executeUpdate()==0) return new Error("Unable to change this job");
            this.mysql.getCon().commit();
        } catch (SQLException e) {
            return new Error(e);
        }
        return null;
    }

    @Override
    public Error delete(Integer id) {
        return new Error("You should not use this method");
    }

    public Error delete(Integer id, Integer droneId) {
        Savepoint s;
        try {
            s = mysql.getCon().setSavepoint();
        } catch (SQLException e) {
            return new Error(e.getMessage());
        }
        String sql = String.format("DELETE FROM %s WHERE id=? AND stan<=? AND Dron_id",table);
        if(droneId==null) sql+=" IS NULL";
        else sql+="=?";
        try(PreparedStatement pstmt = mysql.getCon().prepareStatement(sql)){
            pstmt.setInt(1,id);
            pstmt.setInt(2,Zadanie.STATUS_PRZYDZIELONO_DRONA);
            if(droneId!=null)statSetVarPar(pstmt,3,droneId);
            if(pstmt.executeUpdate() ==0) return new Error("It's to late to delete this job");
        } catch (SQLException e) {
            return new Error(e.getMessage());
        }

        try {
            if(droneId==null){
                mysql.getCon().commit();
                return null;
            }
            if(Main.droneService.setStatus(droneId, Dron.STATUS_WOLNY)==null) mysql.getCon().commit();
            else {
                mysql.getCon().rollback(s);
                return new Error("Unable to change drone status");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Error validate(DataModel data) {
        Zadanie z = (Zadanie) data;
        if(z.getStan()==null) return new Error("State cannot be empty");
        if(z.getData_rozpoczenia() == null) return new Error("Start date is required");
        if(z.getTyp()==null) return new Error("Type is required");
        if(z.getUzytkownik_id()==null) return new Error("User is required");
        if(z.getTyp() == Zadanie.TYPE_MOVE_TO_POINT){
            if(z.getDron_id()==null) return new Error("Drone id is required");
            if(z.getPunkt_koncowy_id()==null) return new Error("Finish point is required");
            if(z.getTrasa_nazwa()!=null || z.getTrasa_uzytkownik_id()!=null) return new Error("Route is required");
        } else{
            if(z.getDron_id()!=null) return new Error("Drone is not allowed");
            if(z.getPunkt_koncowy_id()!=null) return new Error("Finish point is not allowed");
            if(z.getTrasa_nazwa()==null || z.getTrasa_uzytkownik_id()==null) return new Error("Route is required");
        }
        return null;
    }

    @Override
    public List<DataModel> parseToModel(ResultSet res) throws SQLException {
        ArrayList<DataModel> list = new ArrayList<>();
        while (res.next()) {
            Zadanie z = new Zadanie();
            z.setId(res.getInt(1));
            z.setData_rozpoczenia((Timestamp) res.getObject(2));
            z.setSzacowana_dlugosc((Float) res.getObject(3));
            z.setTyp(res.getInt(4));
            z.setTrasa_nazwa(res.getString(5));
            z.setTrasa_uzytkownik_id((Integer) res.getObject(6));
            z.setUzytkownik_id(res.getInt(7));
            z.setDron_id((Integer) res.getObject(8));
            z.setPunkt_koncowy_id((Integer) res.getObject(9));
            z.setStan(res.getInt(10));
            list.add(z);
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
