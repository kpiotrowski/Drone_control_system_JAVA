package service;

import dataModels.DataModel;
import dataModels.Trasa;
import databaseController.MySQLController;

import java.sql.ResultSet;

/**
 * Created by no-one on 18.11.16.
 */
public class TrasaService extends Service implements ServiceInterface {

    public TrasaService(MySQLController con){
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
    public Trasa parseToModel(ResultSet res) {
        return null;
    }
}
