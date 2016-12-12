package service;

import dataModels.DataModel;
import dataModels.Zadanie;
import databaseController.MySQLController;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by no-one on 18.11.16.
 */
public class ZadanieService extends Service implements ServiceInterface{

    public ZadanieService(MySQLController con){
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
    public Error delete(Integer id) {
        return null;
    }

    @Override
    public Error validate(DataModel data) {
        return null;
    }

    @Override
    public List<DataModel> parseToModel(ResultSet res) {
        return new ArrayList<>();
    }
}
