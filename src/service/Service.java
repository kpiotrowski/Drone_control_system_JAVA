package service;

import common.FilterParam;
import dataModels.DataModel;
import databaseController.MySQLController;
import javafx.scene.control.Alert;
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
abstract class Service {

    MySQLController mysql;

    Service(MySQLController con){
        this.mysql = con;
    }

    /**
     *
     * @param id Row id to delete
     * @param table table name
     * @return Error when failed
     */
    public Error delete(Integer id, String table, boolean commit) {
        String sql = String.format("DELETE FROM %s WHERE id=?",table);
        try(PreparedStatement pstmt = mysql.getCon().prepareStatement(sql)){
            pstmt.setInt(1,id);
            pstmt.execute();
            if(commit)mysql.getCon().commit();
        } catch (SQLException e) {
            return new Error(e.getMessage());
        }
        return null;
    }

    /**
     *
     * @param filterList List with filter parameters
     * @param selectStr select SQL (ex. SELECT * FROM xxx WHERE id=?)
     * @param table table name
     * @return PreparedStatement to execute
     * @throws SQLException Exception when failed
     */
    PreparedStatement find(ArrayList<FilterParam> filterList, String selectStr, String table) throws SQLException {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("SELECT %s FROM %s", selectStr, table));

        for (int i =0; i < filterList.size(); i++) {
            FilterParam f = filterList.get(i);
            if(i==0) builder.append(" WHERE ");
            else builder.append(" AND ");
            builder.append(f.getName());
            builder.append(" "+f.getSign()+" ");
            builder.append("?");
        }
        PreparedStatement pstmt = mysql.getCon().prepareStatement(builder.toString());
        for (int i =0; i < filterList.size(); i++) {
            FilterParam f = filterList.get(i);
            Object val = f.getVal();
            if(val instanceof  String) statSetVarPar(pstmt, i+1,"%"+(String)val+"%");
            if(val instanceof  Integer) statSetVarPar(pstmt, i+1,(Integer) val);
            if(val instanceof  Float) statSetVarPar(pstmt, i+1,(Float)val);
        }
        return pstmt;
    }
}
