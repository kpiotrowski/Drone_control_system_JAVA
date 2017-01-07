package service;

import common.FilterParam;
import dataModels.DataModel;
import dataModels.Zadanie;
import databaseController.MySQLController;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by no-one on 18.11.16.
 */
public class ZadanieService extends Service implements ServiceInterface{

    private static final String table = "Zadanie";
    private static final String selectStr = "id,data_rozpoczenia,szacowana_dlugosc,typ,Trasa_nazwa,Trasa_Uzytkownik_id,Uzytkownik_id,Dron_id,Punkt_koncowy_id,stan";

    public ZadanieService(MySQLController con){
        super(con);

    }

    @Override
    public Error insert(DataModel data) {
        return null;
    }

    @Override
    public Error update(DataModel data) {

        return null;
    }

    @Override
    public Error delete(Integer id) {
        return null;
    }

    @Override
    public Error validate(DataModel data) {
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
            z.setTrasa_uzytkownik_id(res.getInt(6));
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
