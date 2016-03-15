package com.example.pratyumjagannath.schoolify;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by pratyumjagannath on 3/15/16.
 */
public class SecondarySchool extends School {
    private boolean isAutonomous;
    private boolean isIndependent;
    private boolean isIntegrated;
    private ArrayList<String> distinctionProg;


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

    public ArrayList<String> getDistinctionProg() {
        return distinctionProg;
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

    public void setDistinctionProg(ArrayList<String> distinctionProg) {
        this.distinctionProg = distinctionProg;
    }

}
