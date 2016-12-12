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

/**
 * Created by no-one on 18.11.16.
 */
public abstract class Service {

    protected MySQLController mysql;

    public Service(MySQLController con){
        this.mysql = con;
    }

    public Error delete(Integer id, String table) {
        String sql = String.format("DELETE FROM %s WHERE id=?",table);
        try(PreparedStatement pstmt = mysql.getCon().prepareStatement(sql)){
            pstmt.setInt(1,id);
            pstmt.execute();
            mysql.getCon().commit();
        } catch (SQLException e) {
            return new Error(e.toString());
        }
        return null;
    }

    public PreparedStatement find(ArrayList<FilterParam> filterList, String selectStr, String table) throws SQLException {
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
            if(val instanceof  String) pstmt.setString(i+1,"%"+(String)val+"%");
            if(val instanceof  Integer) pstmt.setInt(i+1,(Integer) val);
            if(val instanceof  Float) pstmt.setFloat(i+1,(Float)val);
        }
        return pstmt;
    }
}
