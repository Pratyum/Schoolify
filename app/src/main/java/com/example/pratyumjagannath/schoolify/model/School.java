package com.example.pratyumjagannath.schoolify.model;

import android.util.Log;

import com.example.pratyumjagannath.schoolify.controller.FetchLocationData;
import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.concurrent.ExecutionException;

/**
 * Created by pratyumjagannath on 3/15/16.
 */
public class School implements Serializable{
    private String school_name;
    private LatLng school_location;
    private String school_address;

    public School(String name, String address) {
        school_name = name;
        school_address = address;
    }

    public LatLng getSchool_location() throws ExecutionException, InterruptedException {
        FetchLocationData locationData = new FetchLocationData();
        locationData.execute(school_address);
        double[] latlng = locationData.get();
        this.school_location = new LatLng(latlng[0], latlng[1]);
        Log.d("BOOBS", latlng[0] + "," + latlng[1] + " for " + school_name);
        return this.school_location;
    }

    public void setSchool_location() throws ExecutionException, InterruptedException {
        FetchLocationData locationData = new FetchLocationData();
        locationData.execute(school_address);
        double[] latlng = locationData.get();
        this.school_location = new LatLng(latlng[0], latlng[1]);
    }

    public String getSchool_address() {
        return school_address;
    }

    public String getSchool_name() {
        return school_name;
    }

    public void setSchool_address(String school_address) {
        this.school_address = school_address;
    }

    public void setSchool_name(String school_name) {
        this.school_name = school_name;
    }

}

