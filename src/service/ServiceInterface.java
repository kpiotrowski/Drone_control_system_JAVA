package service;

import dataModels.DataModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
     * @param id Data model to delete
     */
    public Error delete(Integer id);

    /**
     *
     * @param data Datamodel to validation
     * @return empty string if valid
     */
    public Error validate(DataModel data);

    /**
     *
     * @param res ResultSet with SQL results
     * @return Parsed datamodel
     * @throws SQLException Exception if getting data from resultSet failed
     */
    public List<DataModel> parseToModel(ResultSet res) throws SQLException;

}
