package service;

import dataModels.DataModel;
import dataModels.Punkt_kontrolny;
import databaseController.MySQLController;

/**
 * Created by no-one on 18.11.16.
 */
public class PunktKontrolnyService extends Service implements ServiceInterface {

    public PunktKontrolnyService(MySQLController con) {
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
    public String validate(DataModel data) {

        return "";
    }

    @Override
    public Punkt_kontrolny parseToModel() {
        return null;
    }
}
