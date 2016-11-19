package service;

import dataModels.DataModel;
import dataModels.Dron;
import databaseController.MySQLController;

/**
 * Created by no-one on 18.11.16.
 */
public class DroneService extends Service implements ServiceInterface{

    public DroneService(MySQLController con) {
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
    public Dron parseToModel() {
        return null;
    }
}
