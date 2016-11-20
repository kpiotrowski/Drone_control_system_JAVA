package service;

import dataModels.DataModel;
import dataModels.Punkt_kontrolny;
import databaseController.MySQLController;

import java.sql.ResultSet;

/**
 * Created by no-one on 18.11.16.
 */
public class PunktKontrolnyService extends Service implements ServiceInterface {

    public PunktKontrolnyService(MySQLController con) {
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
    public Punkt_kontrolny parseToModel(ResultSet res) {
        return null;
    }
}
