package service;

import dataModels.DataModel;

import java.sql.SQLException;

/**
 * Created by no-one on 18.11.16.
 */
public interface ServiceInterface {

    public void insert(DataModel data) throws SQLException;

    public void update(DataModel data);

    public void delete(DataModel data);

    public String validate(DataModel data);

    public DataModel parseToModel();

}
