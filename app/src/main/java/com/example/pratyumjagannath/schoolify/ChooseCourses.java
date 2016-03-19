package com.example.pratyumjagannath.schoolify;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ChooseCourses extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_courses);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        String Level = intent.getStringExtra("SchoolLevel");
        boolean isSpecialPlan = intent.getBooleanExtra("isSpecialPlan", false);
        boolean isAutonomous = intent.getBooleanExtra("isAutonomous", false);
        boolean isInegrated = intent.getBooleanExtra("isInegrated", false);
        boolean isIndependent = intent.getBooleanExtra("isIndependent",false);
        Log.d("BOOBS", Level);
        Log.d("BOOBS",isAutonomous+" ");
        Log.d("BOOBS",isSpecialPlan+"");
        Log.d("BOOBS",isInegrated+"");
        Log.d("BOOBS",isIndependent+"");
        Log.d("BOOBS",intent.getDoubleExtra("myLatitude",0.0)+"");
        Log.d("BOOBS", intent.getDoubleExtra("myLongitude", 0.0) + "");

        FetchSchoolData fetchData = new FetchSchoolData();
        fetchData.execute();
        ArrayList<School> ListOfSchools = new ArrayList<>();
        try {
            ListOfSchools = fetchData.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Log.d("BOOBS","Size of Array is "+ ListOfSchools.size());
        ArrayList<String> ListOfCourses = new ArrayList<>();

        for (int i=0;i<ListOfSchools.size();++i){
            SecondarySchool school = (SecondarySchool) ListOfSchools.get(i);

            if(school.getDistinctionProg().length>0){
                Log.d("BOOBS",school.getDistinctionProg().length+" is the size for i="+i);
                for(int j=0;j<school.getDistinctionProg().length;++j){
                    if(!ListOfCourses.contains(school.getDistinctionProg()[j])){
                        ListOfCourses.add(school.getDistinctionProg()[j]);
                        Log.d("BOOBS",school.getDistinctionProg()[j]+" is added");
                    }
                }
            }
        }
        Log.d("BOOBS",ListOfCourses.size()+" courses available");
        LinearLayout layout = (LinearLayout) findViewById(R.id.Courses);
        final ArrayList<String> ChoiceOfCourses = new ArrayList<>();
        for(int i=0;i<ListOfCourses.size();++i){
            final CheckBox checkBox = new CheckBox(this);
            checkBox.setText(ListOfCourses.get(i));
            checkBox.setId(i);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(ChoiceOfCourses.contains(checkBox.getText())){
                        ChoiceOfCourses.remove(checkBox.getText());
                        Log.d("BOOBS",checkBox.getText()+"is removed");
                    }else {
                        ChoiceOfCourses.add(checkBox.getText().toString());
                        Log.d("BOOBS", checkBox.getText() + "is added");
                    }
                }
            });
            layout.addView(checkBox);
        }

        Button next_button = (Button) findViewById(R.id.to_Step_5);
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("BOOBS",ChoiceOfCourses.size()+" courses are selected!");
            }
        });

    }

}
