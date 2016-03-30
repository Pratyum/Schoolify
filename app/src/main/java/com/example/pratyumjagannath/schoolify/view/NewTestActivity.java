package com.example.pratyumjagannath.schoolify.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.pratyumjagannath.schoolify.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class NewTestActivity extends AppCompatActivity implements OnMapReadyCallback {

    private LatLng myLocation;
//    EditText address_et = (EditText) findViewById(R.id.address);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent i = getIntent();
//        ArrayList<School> ListofSchools = (ArrayList<School>) i.getSerializableExtra("ListofSchools");
//        Log.d("BOOBs",ListofSchools.size()+" Is the size");
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map_location);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
            Location mylocation = googleMap.getMyLocation();
            while(mylocation==null){
                googleMap.getMyLocation();
            }
            LatLng myLocationCoordinates = new LatLng(mylocation.getLatitude(), mylocation.getLongitude());
            Log.d("BOOBS", "Lat:" + mylocation.getLatitude() + "Long: " + mylocation.getLongitude());
            setMyLocation(myLocationCoordinates);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            LatLng ntu = new LatLng(1.3447, 103.6813);
            CameraPosition target = CameraPosition.builder().target(ntu).zoom(14).build();
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(target));
            Button next_button = (Button) findViewById(R.id.to_Step_2);
            next_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getMyLocation()!=null) {
                        Toast.makeText(getBaseContext(), "Location changed: Lat: " + getMyLocation().latitude + " Lng: "
                                + getMyLocation().longitude, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), ChooseLevelofSchool.class);
                        i.putExtra("myLatitude",getMyLocation().latitude);
                        i.putExtra("myLongitude",getMyLocation().longitude);
                        startActivity(i);
                    }
                    else{
                        Toast.makeText(getBaseContext(),"No Location set", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            ImageButton search_button = (ImageButton) findViewById(R.id.submit_address);

            search_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        PlacePicker.IntentBuilder intentBuilder =
                                new PlacePicker.IntentBuilder();
                        Intent intent = intentBuilder.build(getBaseContext());
                        // Start the intent by requesting a result,
                        // identified by a request code.
                        startActivityForResult(intent, 1);

                    } catch (GooglePlayServicesRepairableException e) {
                        // ...
                    } catch (GooglePlayServicesNotAvailableException e) {
                        // ...
                    }
                }
            });
        } else {

        }
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {

        if (requestCode == 1
                && resultCode == Activity.RESULT_OK) {

            // The user has selected a place. Extract the name and address.
            final Place place = PlacePicker.getPlace(data, this);

            final CharSequence name = place.getName();
            final CharSequence address = place.getAddress();
            String attributions = PlacePicker.getAttributions(data);
            if (attributions == null) {
                attributions = "";
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public LatLng getMyLocation() {
        return myLocation;
    }

    public void setMyLocation(LatLng myLocation) {
        this.myLocation = myLocation;
    }
}


