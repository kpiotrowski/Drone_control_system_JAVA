package service;

import dataModels.DataModel;
import dataModels.Uzytkownik;
import databaseController.MySQLController;

/**
 * Created by no-one on 18.11.16.
 */
public class UserService extends Service implements ServiceInterface{

    public UserService(MySQLController con){
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
    public Uzytkownik parseToModel() {
        return null;
    }
}
