package com.example.pratyumjagannath.schoolify.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by pratyumjagannath on 4/3/16.
 */
public class DataWrapper implements Serializable{
    private ArrayList<School> ListOfSchools;

    public DataWrapper(ArrayList<School> data) {
        this.ListOfSchools = data;
    }

    public ArrayList<School> getParliaments() {
        return this.ListOfSchools;
    }


}
