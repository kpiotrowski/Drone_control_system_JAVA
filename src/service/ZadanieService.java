package service;

import dataModels.DataModel;
import dataModels.Zadanie;
import databaseController.MySQLController;

/**
 * Created by no-one on 18.11.16.
 */
public class ZadanieService extends Service implements ServiceInterface{

    public ZadanieService(MySQLController con){
        super(con);

    }

    @Override
    public void insert(DataModel data) {

    }

    @Override
    public void update(DataModel data) {

    }

    @Override
    public void delete(DataModel data) {

    }

    @Override
    public Zadanie parseToModel() {
        return null;
    }
}
