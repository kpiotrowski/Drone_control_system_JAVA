package service;

import databaseController.MySQLController;

/**
 * Created by no-one on 18.11.16.
 */
public class Service {

    protected MySQLController mysql;

    public Service(MySQLController con){
        this.mysql = con;
    }
}
