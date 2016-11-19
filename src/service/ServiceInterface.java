package service;

import dataModels.DataModel;

/**
 * Created by no-one on 18.11.16.
 */
public interface ServiceInterface {

    public void insert(DataModel data);

    public void update(DataModel data);

    public void delete(DataModel data);

    public DataModel parseToModel();

}
