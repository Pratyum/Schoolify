package com.example.pratyumjagannath.schoolify.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

public class Results_navigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback{

    static final int READ_BLOCK_SIZE = 1000;
    private GoogleMap map;
    private ArrayList<School> ListOfSchools;
    private ArrayList<Marker> ListOfMarkers;
    private LatLng myLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        Intent intent = getIntent();
        String Level = intent.getStringExtra("SchoolLevel");
        boolean isSpecialPlan = intent.getBooleanExtra("isSpecialPlan", false);
        boolean isAutonomous = intent.getBooleanExtra("isAutonomous", false);
        boolean isInegrated = intent.getBooleanExtra("isInegrated", false);
        boolean isIndependent = intent.getBooleanExtra("isIndependent",false);
        myLocation = new LatLng(intent.getDoubleExtra("myLatitude",0.0),intent.getDoubleExtra("myLongitude",0.0));
        Log.d("BOOBS", "" + Level + " is the Level");
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
                String dates="";
                try {
                    FileInputStream fileIn=openFileInput("date.txt");
                    InputStreamReader InputRead= new InputStreamReader(fileIn);

                    char[] inputBuffer= new char[READ_BLOCK_SIZE];
                    int charRead;

                    while ((charRead=InputRead.read(inputBuffer))>0) {
                        // char to string conversion
                        String readstring=String.copyValueOf(inputBuffer,0,charRead);
                        dates +=readstring;
                    }
                    InputRead.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    String today = String.format(" %02d-%02d-%04d_%02d-%02d",Calendar.getInstance().get(Calendar.DAY_OF_MONTH),Calendar.getInstance().get(Calendar.MONTH)+1,Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.HOUR_OF_DAY),Calendar.getInstance().get(Calendar.MINUTE));
                    Log.d("BOOBs", today);
                    dates+=(" "+ today);
                    Log.d("BOOBs", dates);
                    FileOutputStream fileout1=openFileOutput("date.txt", MODE_PRIVATE);
                    OutputStreamWriter outputWriter=new OutputStreamWriter(fileout1);
                    outputWriter.write(dates);
                    outputWriter.close();

                    String json = new Gson().toJson(ListOfSchools);
                    Log.d("BOOBs",json);
                    FileOutputStream fileout = openFileOutput(today + ".txt", MODE_PRIVATE);
                    OutputStreamWriter outputWriter1=new OutputStreamWriter(fileout);
                    outputWriter1.write(json);
                    outputWriter1.close();
                    //display file saved message
//                    Toast.makeText(getBaseContext(), "Files saved successfully!+ Json also saved!",
//                            Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                Snackbar.make(view, "Content Saved ! Thanks for using Schoolify!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map_location_results);
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
        getMenuInflater().inflate(R.menu.results_navigation, menu);
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
            Intent i = new Intent(getApplication(),NewTestActivity.class);
            startActivity(i);
        } else if (id == R.id.saved_test) {
            Intent i = new Intent(getApplication(),SavedTest.class);
            Gson gson  = new Gson();
            String json = gson.toJson(ListOfSchools);
            i.putExtra("List",json);
            i.putExtra("MyLat",myLocation.latitude);
            i.putExtra("MyLng",myLocation.longitude);
            startActivity(i);
        } else if (id == R.id.about_us) {
            Intent i = new Intent(getApplicationContext(),AboutUs.class);
            startActivity(i);
        } else if (id == R.id.nav_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Have you tried schooliy! You Should!");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }else if (id == R.id.main_menu) {
            Intent i = new Intent(getApplicationContext(),Schoolify.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                LinearLayout info = new LinearLayout(getApplicationContext());
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(getBaseContext());
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(getApplicationContext());
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });
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
                                    .title((i + 1) + ". " + ListOfSchools.get(i).getSchool_name())
                                    .alpha(alpha)
                                    .snippet("Distance from your Location: " + (location.distanceTo(schoolLocation) / 1000) + "km" + "\n" + "Distinguished Programmes: " + ((SecondarySchool) ListOfSchools.get(i)).printDistProg());
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
                Log.d("BOOBS", marker.getTitle() + "Clicked!");
                String[] title = marker.getTitle().split(".");
                Log.d("BOOBS", title.length + " is the number of splits");
                for (int i = 0; i < title.length; ++i) {
                    Log.d("BOOBS", title[i] + " for i=" + i);
                }
//                int pos = Integer.valueOf(title[0]);
                return false;
            }
        });

    }


}
