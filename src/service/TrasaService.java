package service;

import dataModels.DataModel;
import dataModels.Trasa;
import databaseController.MySQLController;

/**
 * Created by no-one on 18.11.16.
 */
public class TrasaService extends Service implements ServiceInterface {

    public TrasaService(MySQLController con){
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
    public Trasa parseToModel() {
        return null;
    }
}
