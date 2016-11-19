package service;

import databaseController.MySQLController;

/**
 * Created by no-one on 18.11.16.
 */
public class Service {

    private MySQLController mysql;

    public Service(MySQLController con){
        this.mysql = con;
    }
}
