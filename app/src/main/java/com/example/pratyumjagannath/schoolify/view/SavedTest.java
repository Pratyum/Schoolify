package com.example.pratyumjagannath.schoolify.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pratyumjagannath.schoolify.R;
import com.example.pratyumjagannath.schoolify.model.School;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class SavedTest extends AppCompatActivity {
    ArrayList<School> ListofSchools;
    static final int READ_BLOCK_SIZE = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left_bold);
        setSupportActionBar(toolbar);
        Gson gson = new Gson();
        Type School_list = new TypeToken<ArrayList<School>>() {
        }.getType();
        final Intent intent = getIntent();
        final String json = intent.getStringExtra("List");
        ListofSchools = gson.fromJson(json, School_list);
        Log.d("BOOBS", json);
        Log.d("BOOBS", ListofSchools.size()+" ");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        try {
            FileInputStream fileIn = openFileInput("date.txt");
            InputStreamReader InputRead = new InputStreamReader(fileIn);

            char[] inputBuffer = new char[READ_BLOCK_SIZE];
            String s = "";
            int charRead;

            while ((charRead = InputRead.read(inputBuffer)) > 0) {
                // char to string conversion
                String readstring = String.copyValueOf(inputBuffer, 0, charRead);
                s += readstring;
            }
            InputRead.close();
            final String[] list = s.split(" ");
          ArrayAdapter<String> rest_list_adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, list) {
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

            ListView lw = (ListView) findViewById(R.id.list_saved_test);
            lw.setAdapter(rest_list_adapter);

            lw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d("BOOBS", list[position]);
                    Intent i = new Intent(getBaseContext(),Saved_test_detail.class);
                    i.putExtra("json",json);
                    i.putExtra("MyLat",intent.getDoubleExtra("MyLat",0));
                    i.putExtra("MyLng",intent.getDoubleExtra("MyLng",0));
                    startActivity(i);
                }
            });

            Toast.makeText(getBaseContext(), s, Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
