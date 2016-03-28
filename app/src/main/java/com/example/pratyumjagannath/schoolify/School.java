package com.example.pratyumjagannath.schoolify;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.concurrent.ExecutionException;

public class School{
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

/*
    // Using https://github.com/jasonwinn/haversine to calculate distance
    public void calc_distance_score(Haversine haversine, LatLng location, int distance_importance) {
        int dist_score = 0;
        int distance = haversine.distance(location[0], location[1], school_location[0], school_location[1])
        for (int i = 1; i<=4; i++) {
            if (distance < i) {
                dist_score == 4-i;
                break;
            }
        }
        score += dist_score*distance_importance;
    }

    public void calc_programmes_score(ArrayList<String> preferred_programmes, int programmes_importance) {
        int prog_score = 0;
        for (String program: preferred_programmes) {
            if (distinctionProg.contains(program)) {
                prog_score++;
            }
        }
        score += prog_score*programmes_importance;
    }

    public void calc_types_score(boolean auto, boolean special, boolean integrated, boolean independent, int types_importance) {
        int type_score = 0;
        if (auto == isAutonomous) { type_score++; }
        if (special == special_assistance_plan_school) { type_score++; }
        if (integrated == isIntegrated) { type_score++; }
        if (independent == isIndependent) { type_score++; }
        score += type_score*types_importance;
    }

    @Override
    public int compare(School o1, School o2) {
        return o1.getScore().compareTo(o2.getScore());
    }*/

}

