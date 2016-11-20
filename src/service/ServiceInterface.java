package service;

import dataModels.DataModel;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by no-one on 18.11.16.
 */
public interface ServiceInterface {

    /**
     *
     * @param data Data model to insert
     * @return Error if inserting failed
     */
    public Error insert(DataModel data);

    /**
     *
     * @param data Datamodel to update
     * @return Error when updating failed
     */
    public Error update(DataModel data);

    /**
     *
     * @param data Data model to delete
     */
    public void delete(DataModel data);

    /**
     *
     * @param data Datamodel to validation
     * @return empty string if valid
     */
    public String validate(DataModel data);

    /**
     *
     * @param res ResultSet with SQL results
     * @return Parsed datamodel
     * @throws SQLException Exception if getting data from resultSet failed
     */
    public DataModel parseToModel(ResultSet res) throws SQLException;

}
