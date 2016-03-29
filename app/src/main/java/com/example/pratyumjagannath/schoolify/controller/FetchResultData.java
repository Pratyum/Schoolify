package com.example.pratyumjagannath.schoolify.controller;

import android.location.Location;
import android.util.Log;

import com.example.pratyumjagannath.schoolify.model.School;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by pratyumjagannath on 3/26/16.
 */
public class FetchResultData {
    ArrayList<School> ListOfSchools;
    LatLng MyPosition;
    ArrayList<Float> Score;
     public FetchResultData(ArrayList<School> listOfSchools , LatLng myPosition){
           ListOfSchools= listOfSchools;
         MyPosition = myPosition;
         Score = new ArrayList<>();
     }
    public void calculateDistScore(){
        for(int i=0;i<ListOfSchools.size();++i){
            float[] distances = new float[0];
            LatLng schoolLocation;
            try {
                schoolLocation = ListOfSchools.get(i).getSchool_location();
                Location.distanceBetween(MyPosition.latitude, MyPosition.longitude, schoolLocation.latitude, schoolLocation.longitude, distances);
                if(distances.length > 0){
                    if(distances.length>2){
                        Log.d("BOOBS","Distnce from "+ListOfSchools.get(i).getSchool_name()+" and My location is : "+distances[0]+" and length is " + distances.length+" and shortest distance is "+ distances[1]);
                        Score.add(distances[0]);
                    }
                    else{
                        Score.add(distances[0]);
                        Log.d("BOOBS","Distnce from "+ListOfSchools.get(i).getSchool_name()+" and My location is : "+distances[0]);

                    }
                }
                else{
                    Log.d("BOOBS","Length is "+distances.length);
                    Score.add((float) -1);
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
