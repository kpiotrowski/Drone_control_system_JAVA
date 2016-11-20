package service;

import dataModels.DataModel;
import dataModels.Dron;
import databaseController.MySQLController;

import java.sql.ResultSet;

/**
 * Created by no-one on 18.11.16.
 */
public class DroneService extends Service implements ServiceInterface{

    public DroneService(MySQLController con) {
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
    public void delete(DataModel data) {

    }

    @Override
    public String validate(DataModel data) {

        return "";
    }

    @Override
    public Dron parseToModel(ResultSet res) {
        return null;
    }
}
