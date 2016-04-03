package com.example.pratyumjagannath.schoolify.model;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.pratyumjagannath.schoolify.controller.FetchLocationData;
import com.google.android.gms.maps.model.LatLng;

import java.util.concurrent.ExecutionException;

/**
 * Created by pratyumjagannath on 3/15/16.
 */
public class School implements Parcelable,Comparable {
    private String school_name;
    private LatLng school_location;
    private String school_address;
    private double score;

    public School(String name, String address) {
        school_name = name;
        school_address = address;
    }

    public LatLng getSchool_location() throws ExecutionException, InterruptedException {
        if (school_location == null) {
            FetchLocationData locationData = new FetchLocationData();
            locationData.execute(school_address);
            double[] latlng = locationData.get();
            if (latlng != null) {
                this.school_location = new LatLng(latlng[0], latlng[1]);
                Log.d("BOOBS", latlng[0] + "," + latlng[1] + " for " + school_name);
                return this.school_location;
            } else {
                Log.d("BOOBs", "Nothing Returned: " + school_name);
                return null;
            }
        } else {
            return school_location;
        }
    }

    public void setSchool_location() throws ExecutionException, InterruptedException {
        FetchLocationData locationData = new FetchLocationData();
        locationData.execute(school_address);
        double[] latlng = locationData.get();
        if(latlng!=null)
            this.school_location = new LatLng(latlng[0], latlng[1]);
        else
            this.school_location = new LatLng(0,0);
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

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void calc_distance_score(LatLng myLocation) throws ExecutionException, InterruptedException {
        if(school_location==null){
            setSchool_location();
        }
        if(school_location.latitude!=0 || school_location.longitude!=0) {
            Location my_school_location = new Location(school_name);
            my_school_location.setLatitude(school_location.latitude);
            my_school_location.setLongitude(school_location.longitude);
            Location my_Location = new Location("Mylocation");
            my_Location.setLatitude(myLocation.latitude);
            my_Location.setLongitude(myLocation.longitude);
            double distance = my_school_location.distanceTo(my_Location);
            Log.d("BOOBS", distance + " is the distance between mylocation and " + school_name);
            score += distance;
        }else{
            score+=1000000000;
            Log.d("BOOBS", "Location is messed up for " + school_name);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Log.d("BOOBS","Parcelling"+ school_name);
        dest.writeString(school_address);
        dest.writeString(school_name);
        dest.writeDouble(score);
        if(school_location!=null) {
            dest.writeDouble(school_location.latitude);
            dest.writeDouble(school_location.longitude);
        }
        else{
            dest.writeDouble(0);
            dest.writeDouble(0);
        }
    }

    public static final Parcelable.Creator<School> CREATOR = new Parcelable.Creator<School>() {
        public School createFromParcel(Parcel in) {
            return new School(in);
        }

        public School[] newArray(int size) {
            return new School[size];
        }
    };

    private School(Parcel in) {

        school_address = in.readString();
        school_name = in.readString();
        score = in.readDouble();
        double lat = in.readDouble();
        double lon = in.readDouble();
        if(lat==0 && lon ==0 ){
            school_location = null;
        }else {
            school_location = new LatLng(lat, lon);
        }
        Log.d("BOOBS","UnParcelling"+ school_name);
    }

    @Override
    public int compareTo(Object another) {
        double another_score = ((School)another).getScore();

        return Integer.valueOf((int) (score-another_score));
    }
}

