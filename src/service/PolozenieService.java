package service;

import dataModels.DataModel;
import dataModels.Polozenie;
import databaseController.MySQLController;

/**
 * Created by no-one on 18.11.16.
 */
public class PolozenieService extends Service implements ServiceInterface {

    public PolozenieService(MySQLController con) {
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
    public Polozenie parseToModel() {
        return null;
    }
}
