package service;

import dataModels.DataModel;
import dataModels.Punkt_na_trasie;
import databaseController.MySQLController;

import java.sql.ResultSet;

/**
 * Created by no-one on 18.11.16.
 */
public class PunktNaTrasieService extends Service implements ServiceInterface {

    public PunktNaTrasieService(MySQLController con){
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
    public Punkt_na_trasie parseToModel(ResultSet res) {
        return null;
    }
}
