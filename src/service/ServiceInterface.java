package service;

import dataModels.DataModel;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by no-one on 18.11.16.
 */
public interface ServiceInterface {

    public void insert(DataModel data) throws SQLException;

    public Error update(DataModel data);

    public void delete(DataModel data);

    /*
        Validate and return error String
        empty string is returned if model is valid
     */
    public String validate(DataModel data);

    /*
        Parse select result
     */
    public DataModel parseToModel(ResultSet res) throws SQLException;

}
