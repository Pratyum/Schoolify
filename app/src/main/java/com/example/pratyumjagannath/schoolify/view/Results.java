package com.example.pratyumjagannath.schoolify.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.pratyumjagannath.schoolify.R;

import java.util.ArrayList;

public class Results extends AppCompatActivity {

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


        ArrayList<String> ListOfCourses = (ArrayList<String>) intent.getSerializableExtra("ListOfCourses");
//        FetchResultData results = new FetchResultData();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
