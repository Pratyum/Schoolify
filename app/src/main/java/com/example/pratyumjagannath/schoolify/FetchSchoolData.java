package com.example.pratyumjagannath.schoolify;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by pratyumjagannath on 3/15/16.
 */
public class FetchSchoolData extends AsyncTask<Void, Void, ArrayList<School>>{
    private String LOG_TAG = "BOOBS";
    @Override
    protected ArrayList<School> doInBackground(Void... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String JsonStr = null;

        try {
            final String BASE_ADDR = "https://data.gov.sg/api/action/datastore_search?";
            final String KEY_PARAM = "resource_id";
            final String KEY_VALUE = "de6fbf16-9e05-495d-9371-8b706bba5be2";
            final String LIMIT_PARAM = "limit";
            final String LIMIT_VALUE = "60";

            Uri builtUri = Uri.parse(BASE_ADDR).buildUpon()
                    .appendQueryParameter(KEY_PARAM, KEY_VALUE)
                    .appendQueryParameter(LIMIT_PARAM, LIMIT_VALUE)
                    .build();

            URL url = new URL(builtUri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            JsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        try {
            Log.d(LOG_TAG,"Ready to parse!");
            return getSchoolDataFromJson(JsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        // This will only happen if there was an error getting or parsing the forecast.
        return null;
    }

    private ArrayList<School> getSchoolDataFromJson(String JsonStr)
            throws JSONException {
        Log.d(LOG_TAG, JsonStr);
        // These are the names of the JSON objects that need to be extracted.
        final String OWM_RESULTS = "result";
        final String OWM_RECORDS = "records";
        JSONObject resultJson = new JSONObject(JsonStr);
        JSONObject location = resultJson.getJSONObject(OWM_RESULTS);
        JSONArray  locationArray = location.getJSONArray(OWM_RECORDS);
        ArrayList<School> ListOfSchool = new ArrayList<>();
        Log.d(LOG_TAG,"Size of LocationArray is "+ locationArray.length());
        for(int i=0;i<locationArray.length();++i){
            JSONObject object = locationArray.getJSONObject(i);
            String name = object.getString("school");
            String address = name +" Singapore";
            SecondarySchool school = null;
            try {
                school = new SecondarySchool(name,address);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(object.getString("autonomous_school").contains("No")){
                school.setIsAutonomous(false);
            }
            else {
                school.setIsAutonomous(true);
            }
            if(object.getString("independent_school").contains("No")){
                school.setIsIndependent(false);
            }
            else{
                school.setIsIndependent(true);
            }
            if(object.getString("special_assistance_plan_school").contains("No")){
                school.setSpecial_assistance_plan_school(false);
            }
            else{
                school.setSpecial_assistance_plan_school(true);
            }
            if(object.getString("integrated_programme").contains("No")){
                school.setIsIntegrated(false);
            }
            if(object.getString("school_with_distinctive_programmes")!="na") {
                String all_programs = object.getString("school_with_distinctive_programmes");
                String[] program = all_programs.split(";");
                school.setDistinctionProg(program);
            }
            ListOfSchool.add(school);
            Log.d(LOG_TAG,"School "+i+" added");
        }
        return ListOfSchool;
    }

}
