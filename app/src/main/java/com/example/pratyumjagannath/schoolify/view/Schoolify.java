package com.example.pratyumjagannath.schoolify.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.pratyumjagannath.schoolify.R;
import com.example.pratyumjagannath.schoolify.controller.FetchSchoolData;
import com.example.pratyumjagannath.schoolify.model.School;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Schoolify extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback {

    ArrayList<School> ListOfSchools;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.new_test) {
            Gson gson = new Gson();
            Intent i = new Intent(getApplication(),NewTestActivity.class);
            String list_schools_test = gson.toJson(ListOfSchools);
            i.putExtra("ListOfSchools",list_schools_test);
            startActivity(i);
        } else if (id == R.id.saved_test) {
            Intent i = new Intent(getApplicationContext(),SavedTest.class);
            startActivity(i);
        } else if (id == R.id.about_us) {
            Intent i = new Intent(getApplicationContext(),AboutUs.class);
            startActivity(i);

        } else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            LatLng ntu = new LatLng(1.3447, 103.6813);
            CameraPosition target = CameraPosition.builder().target(ntu).zoom(10).build();
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(target));

            FetchSchoolData schools = new FetchSchoolData();
            schools.execute();
            try {
                setListOfSchools(schools.get());
                Log.d("BOOBS", "List of Schools got!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            ArrayList<LatLng> listofMarkers = new ArrayList<>();
            try {
                for (int i = 0; i < getListOfSchools().size(); ++i) {
                    listofMarkers.add(getListOfSchools().get(i).getSchool_location());
                }

            }catch (Exception e){
                e.printStackTrace();
            }
            Log.d("BOOBS",listofMarkers.size()+"is the size of the latlng");
            if (googleMap !=null) {
                for (int i = 0; i < listofMarkers.size(); ++i) {
                    if (listofMarkers.get(i)!=null) {
                        MarkerOptions result_marker01 = new MarkerOptions()
                                .position(listofMarkers.get(i))
                                .title(getListOfSchools().get(i).getSchool_name());
                        googleMap.addMarker(result_marker01);
                        Log.d("BOOBS", i + "." + listofMarkers.get(i).latitude + "," + listofMarkers.get(i).longitude + "Added!");
                    }
                }
            }else{
                Log.d("BOOBS","MAP NULL ERROR!");
            }

        } else {
            // Show rationale and request permission.
        }
    }

    public ArrayList<School> getListOfSchools() {
        return ListOfSchools;
    }

    public void setListOfSchools(ArrayList<School> listOfSchools) {
        ListOfSchools = listOfSchools;
    }
}
