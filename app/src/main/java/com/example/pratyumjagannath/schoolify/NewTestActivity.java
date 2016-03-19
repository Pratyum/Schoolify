package com.example.pratyumjagannath.schoolify;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class NewTestActivity extends AppCompatActivity implements OnMapReadyCallback {

    private LatLng myLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map_location);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
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
                        i.putExtra("Coordinates", getMyLocation());
                        startActivity(i);
                    }
                    else{
                        Toast.makeText(getBaseContext(),"No Location set", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            ImageButton mylocation = (ImageButton) findViewById(R.id.my_location);

            mylocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Location mylocation = googleMap.getMyLocation();
                    LatLng myLocationCoordinates = new LatLng(mylocation.getLatitude(), mylocation.getLongitude());
                    Log.d("BOOBS", "Lat:" + mylocation.getLatitude() + "Long: " + mylocation.getLongitude());
                    setMyLocation(myLocationCoordinates);
                    EditText address = (EditText) findViewById(R.id.address);
                    address.setText("My Location");
                }
            });
        } else {

        }
    }

    public void updateLocationData() {
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d("BOOBS", "No Permission Allowed");
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                EditText address = (EditText) findViewById(R.id.address);
                address.setText("My Location");

                Toast.makeText(
                        getBaseContext(),
                        "Location changed: Lat: " + location.getLatitude() + " Lng: "
                                + location.getLongitude(), Toast.LENGTH_SHORT).show();
                String longitude = "Longitude: " + location.getLongitude();
                Log.v("BOOBS", longitude);
                String latitude = "Latitude: " + location.getLatitude();
                Log.v("BOOBS", latitude);
                setMyLocation(new LatLng(Double.valueOf(latitude),Double.valueOf(longitude)));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });
    }

    public LatLng getMyLocation() {
        return myLocation;
    }

    public void setMyLocation(LatLng myLocation) {
        this.myLocation = myLocation;
    }
}


