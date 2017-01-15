package service;

import dataModels.DataModel;
import dataModels.Polozenie;
import dataModels.Punkt_na_trasie;
import databaseController.MySQLController;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by no-one on 18.11.16.
 */
public class PolozenieService extends Service implements ServiceInterface {

    private static final String table = "Polozenie";
    private static final String insertStr = "wspX,wspY,wspZ";

    public PolozenieService(MySQLController con) {
        super(con);
    }

    @Override
    public Error insert(DataModel data) {return new Error("Not to user");}

    @Override
    public Error update(DataModel data) {return new Error("You can't modify polozenie");}

    @Override
    public Error delete(Integer id) {
        String sql = String.format("DELETE FROM %s WHERE NOT EXISTS(SELECT NULL From Punkt_na_trasie pp WHERE id=pp.Polozenie_id)",table);
        try(PreparedStatement pstmt = mysql.getCon().prepareStatement(sql)){
            pstmt.execute();
        } catch (SQLException e) {
            return new Error("Unable to delete unused Polozenie");
        }
        return null;
    }
    @Override
    public Error validate(DataModel data) {return new Error("Not to user");}

    @Override
    public List<DataModel> parseToModel(ResultSet res) throws SQLException {
        throw new SQLException("not implemented");
    }

    Error insertPolozenieAndSetIds(List<Punkt_na_trasie> list){
        String values = "";
        int loop=0;
        for (Punkt_na_trasie p: list)
            if(p.getPunkt_kontrolny_id()==null){
                if(loop>0) values+=",";
                values+="(?,?,?)";
                ++loop;
            }
        String sql = String.format("INSERT INTO %s (%s) VALUES %s",table,insertStr,values);
        try(PreparedStatement pstmt = mysql.getCon().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            loop=0;
            for (Punkt_na_trasie p :list) if(p.getPunkt_kontrolny_id()==null) {
                pstmt.setFloat(loop * 3 + 1, p.getWspX());
                pstmt.setFloat(loop * 3 + 2, p.getWspY());
                pstmt.setFloat(loop * 3 + 3, p.getWspZ());
                ++loop;
            }
            if(loop==0) return null;
            pstmt.executeUpdate();
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            loop=0;
            for (Punkt_na_trasie p :list) if(p.getPunkt_kontrolny_id()==null) {
                if(generatedKeys.next())
                    p.setPolozenie_id(generatedKeys.getInt(1));
                else
                    throw new SQLException("Unable to get generated keys");
            }
            generatedKeys.close();
        } catch (SQLException e) {
            return new Error(e.getMessage());
        }
        return null;
    }
}
