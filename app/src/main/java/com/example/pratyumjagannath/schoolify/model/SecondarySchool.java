package com.example.pratyumjagannath.schoolify.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by pratyumjagannath on 3/15/16.
 */
public class SecondarySchool extends School {
    private boolean isAutonomous;
    private boolean isIndependent;
    private boolean isIntegrated;
    private boolean special_assistance_plan_school;
    private String[] distinctionProg;


    public SecondarySchool(String name, String address) throws ExecutionException, InterruptedException {
        super(name, address);
    }

    public boolean isAutonomous() {
        return isAutonomous;
    }

    public boolean isIndependent() {
        return isIndependent;
    }

    public boolean isIntegrated() {
        return isIntegrated;
    }

    public boolean isSpecial_assistance_plan_school() {
        return special_assistance_plan_school;
    }

    public String[] getDistinctionProg() {
        return distinctionProg;
    }

    public String printDistProg(){
        String result = "";
        for (String program: distinctionProg) {
            result += program + ", ";
        }
        result = result.substring(0, result.length()-2);
        return result;
    }

    public void setIsAutonomous(boolean isAutonomous) {
        this.isAutonomous = isAutonomous;
    }

    public void setIsIndependent(boolean isIndependent) {
        this.isIndependent = isIndependent;
    }

    public void setIsIntegrated(boolean isIntegrated) {
        this.isIntegrated = isIntegrated;
    }

    public void setSpecial_assistance_plan_school(boolean special_assistance_plan_school) {
        this.special_assistance_plan_school = special_assistance_plan_school;
    }

    public void setDistinctionProg(String[] distinctionProg) {
        this.distinctionProg = distinctionProg;
    }

    public void calc_program_score(ArrayList<String> listOfCourses){
        for(int i=0;i<distinctionProg.length;++i){
            if(listOfCourses.contains(distinctionProg[i])){
                Log.d("BOOBS","Match found in "+ getSchool_name()+" with "+ distinctionProg[i]);
                setScore(getScore() * 0.8);
            }
        }
    }

}
