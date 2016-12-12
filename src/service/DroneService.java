package service;

import common.FilterParam;
import dataModels.DataModel;
import dataModels.Dron;
import dataModels.Punkt_kontrolny;
import databaseController.MySQLController;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by no-one on 18.11.16.
 */
public class DroneService extends Service implements ServiceInterface{

    private final String table = "Dron d";
    private final String insertStr="d.nazwa,d.opis,d.masa,d.ilosc_wirnikow,d.max_predkosc,d.max_czas_lotu,d.poziom_baterii,d.wspX,d.wspY,d.wspZ,d.stan,d.Punkt_kontrolny_id";
    private final String selectStr="d.id,"+insertStr;
    private final String updateStr="d.nazwa=?,d.opis=?,d.poziom_baterii=?";

    public DroneService(MySQLController con) {
        super(con);
    }

    @Override
    public Error insert(DataModel data) {
        //TODO IMPLEMENT THIS
        return null;
    }

    @Override
    public Error update(DataModel data) {
        String sql = String.format("UPDATE %s SET %s WHERE id=?", this.table, updateStr);
        Dron d = (Dron) data;
        try (PreparedStatement pstmt = mysql.getCon().prepareStatement(sql);) {
            pstmt.setString(1,d.getNazwa());
            pstmt.setString(2,d.getOpis());
            pstmt.setFloat(3, d.getPoziom_baterii());
            pstmt.setInt(4,d.getId());
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


        //TODO IMPLEMENT THIS

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
