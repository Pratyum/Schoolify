package com.example.pratyumjagannath.schoolify.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pratyumjagannath.schoolify.R;
import com.example.pratyumjagannath.schoolify.controller.FetchSchoolData;
import com.example.pratyumjagannath.schoolify.model.School;
import com.example.pratyumjagannath.schoolify.model.SecondarySchool;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

public class Results extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap map;
    private ArrayList<School> ListOfSchools;
    private ArrayList<Marker> ListOfMarkers;
    private LatLng myLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String Level = intent.getStringExtra("SchoolLevel");
        boolean isSpecialPlan = intent.getBooleanExtra("isSpecialPlan", false);
        boolean isAutonomous = intent.getBooleanExtra("isAutonomous", false);
        boolean isInegrated = intent.getBooleanExtra("isInegrated", false);
        boolean isIndependent = intent.getBooleanExtra("isIndependent",false);
        myLocation = new LatLng(intent.getDoubleExtra("myLatitude",0.0),intent.getDoubleExtra("myLongitude",0.0));
        Log.d("BOOBS",""+Level+" is the Level");
        Log.d("BOOBS",""+isAutonomous+" is Autonomous");
        Log.d("BOOBS",""+isIndependent+" is Independent");
        Log.d("BOOBS",""+ isInegrated +" is Integrated");
        Log.d("BOOBS",""+ isSpecialPlan + " is Special");
        Log.d("BOOBS",""+ myLocation.latitude+","+myLocation.longitude+" is the location");

        ArrayList<String> ListOfCourses = (ArrayList<String>) intent.getSerializableExtra("ListOfCourses");
//        FetchResultData results = new FetchResultData();

//        ArrayList<School> ListOfSchool = (ArrayList<School>) intent.getSerializableExtra("ListOfSchools");
        FetchSchoolData fetchData = new FetchSchoolData();
        fetchData.execute();
        ListOfSchools = new ArrayList<>();
        try {
            ListOfSchools = fetchData.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Log.d("BOOBS","Size of Array is "+ ListOfSchools.size());



        Log.d("BOOBS",""+ListOfCourses.size()+" is the number of courses");
        Log.d("BOOBS", "" + ListOfSchools.size() + " is the number of schools");

        for (int i=0;i<ListOfSchools.size();++i){
            try {
                ListOfSchools.get(i).calc_distance_score(myLocation);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for(int i=0;i<ListOfSchools.size();++i){
            ((SecondarySchool)ListOfSchools.get(i)).calc_program_score(ListOfCourses);
        }

        Collections.sort(ListOfSchools);

        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i < ListOfSchools.size(); ++i) {
            names.add((i+1)+ ". "+ ListOfSchools.get(i).getSchool_name());
        }



        String[] names_list = new String[names.size()];
        names_list = names.toArray(names_list);
        ArrayAdapter<String> rest_list_adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, names_list) {
            @Override
            public View getView(int position, View convertView,
                                ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
            /*YOUR CHOICE OF COLOR*/
                textView.setTextColor(Color.BLACK);
                return view;
            }
        };
        ListView lw = (ListView) findViewById(R.id.results_schools);
        lw.setAdapter(rest_list_adapter);

        lw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListOfMarkers.get(position).showInfoWindow();
            }
        });



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map_location_results);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            LatLng sing = new LatLng(1.3521,103.8198);
            CameraPosition target = CameraPosition.builder().target(sing).zoom(10).build();
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(target));
            float alpha =1;
            MarkerOptions mylocation = new MarkerOptions()
                    .position(myLocation)
                    .title("My Location!");
            googleMap.addMarker(mylocation);
            Location location = new Location("MyLocation");
            location.setLongitude(myLocation.longitude);
            location.setLatitude(myLocation.latitude);
            ListOfMarkers = new ArrayList<>() ;
            if (googleMap !=null) {
                for (int i = 0; i < ListOfSchools.size(); ++i) {
                    try {
                        if (ListOfSchools.get(i).getSchool_location()!=null) {
                            Location schoolLocation = new Location("SchoolLocation");
                            schoolLocation.setLatitude(ListOfSchools.get(i).getSchool_location().latitude);
                            schoolLocation.setLongitude(ListOfSchools.get(i).getSchool_location().longitude);
                            MarkerOptions result_marker01 = new MarkerOptions()
                                    .position(ListOfSchools.get(i).getSchool_location())
                                    .title((i+1)+". "+ListOfSchools.get(i).getSchool_name())
                                    .alpha(alpha)
                                    .snippet("Distance from your Location: "+ (location.distanceTo(schoolLocation)/1000)+"km");
                            ListOfMarkers.add(googleMap.addMarker(result_marker01));
                            Log.d("BOOBS", i + "." + ListOfSchools.get(i).getSchool_location().latitude + "," + ListOfSchools.get(i).getSchool_location().longitude + "Added!");
                            alpha -= 0.02;
                        }
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }else{
                Log.d("BOOBS","MAP NULL ERROR!");
            }
        } else {

        }
        map = googleMap;
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.d("BOOBS",marker.getTitle()+"Clicked!");
                String[] title = marker.getTitle().split(".");
                Log.d("BOOBS",title.length+" is the number of splits");
                for(int i=0;i<title.length;++i){
                    Log.d("BOOBS",title[i]+" for i="+i);
                }
//                int pos = Integer.valueOf(title[0]);
                return false;
            }
        });

    }
}
