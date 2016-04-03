package com.example.pratyumjagannath.schoolify.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.pratyumjagannath.schoolify.R;
import com.example.pratyumjagannath.schoolify.model.School;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class NewTestActivity extends AppCompatActivity implements OnMapReadyCallback,NavigationView.OnNavigationItemSelectedListener{
    private LatLng myLocation;
    private GoogleMap map;
    private ArrayList<School> ListofSchools;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_new_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);

//
//        Intent i = getIntent();
//        ListofSchools = (ArrayList<School>) i.getParcelableExtra("ListOfSchools");
//        Log.d("BOOBS", ListofSchools.size()+" ");


        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map_location);
        mapFragment.getMapAsync(this);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i("BOOBS", "Place: " + place.getName());
                String placeDetailsStr = place.getName() + "\n"
                        + place.getId() + "\n"
                        + place.getLatLng().toString() + "\n"
                        + place.getAddress() + "\n"
                        + place.getAttributions();
                Log.d("BOOBS", placeDetailsStr + " place");
                Marker postition = map.addMarker(new MarkerOptions()
                        .position(place.getLatLng())
                        .title(place.getName()+""));
                setMyLocation(place.getLatLng());

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("BOOBS", "An error occurred: " + status);
            }
        });

    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
            googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    Location mylocation = googleMap.getMyLocation();
                    LatLng myLocationCoordinates = new LatLng(mylocation.getLatitude(), mylocation.getLongitude());
                    Log.d("BOOBS", "Lat:" + mylocation.getLatitude() + "Long: " + mylocation.getLongitude());
                    PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                            getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
                    autocompleteFragment.setText("My Location");
                    setMyLocation(myLocationCoordinates);

                    return false;
                }
            });
            googleMap.getUiSettings().setZoomControlsEnabled(false);
            LatLng ntu = new LatLng(1.3447, 103.6813);
            CameraPosition target = CameraPosition.builder().target(ntu).zoom(14).build();
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(target));
            FloatingActionButton next_button = (FloatingActionButton) findViewById(R.id.to_step_2);
            next_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getMyLocation() != null) {
                        Toast.makeText(getBaseContext(), "Location changed: Lat: " + getMyLocation().latitude + " Lng: "
                                + getMyLocation().longitude, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), ChooseLevelofSchool.class);
                        i.putExtra("myLatitude", getMyLocation().latitude);
                        i.putExtra("myLongitude", getMyLocation().longitude);
//                        i.putExtra("ListOfSchools", (Serializable)ListofSchools);
                                startActivity(i);
                    } else {
                        Toast.makeText(getBaseContext(), "No Location set", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            FloatingActionButton back = (FloatingActionButton) findViewById(R.id.to_start);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getBaseContext(),Schoolify.class);
                    startActivity(i);
                }
            });
        } else {

        }
        map = googleMap;
    }

    public LatLng getMyLocation() {
        return myLocation;
    }

    public void setMyLocation(LatLng myLocation) {
        this.myLocation = myLocation;
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.new_test) {
            Intent i = new Intent(getApplication(),NewTestActivity.class);
            startActivity(i);
        } else if (id == R.id.saved_test) {

        } else if (id == R.id.about_us) {

        } else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}


