package com.example.pratyumjagannath.schoolify.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.example.pratyumjagannath.schoolify.R;
import com.example.pratyumjagannath.schoolify.controller.FetchSchoolData;
import com.example.pratyumjagannath.schoolify.model.School;
import com.example.pratyumjagannath.schoolify.model.SecondarySchool;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ChooseCourses extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_courses);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Intent intent = getIntent();
        final String Level = intent.getStringExtra("SchoolLevel");
        final boolean isSpecialPlan = intent.getBooleanExtra("isSpecialPlan", false);
        final boolean isAutonomous = intent.getBooleanExtra("isAutonomous", false);
        final boolean isInegrated = intent.getBooleanExtra("isInegrated", false);
        final boolean isIndependent = intent.getBooleanExtra("isIndependent",false);
//        String json = intent.getStringExtra("ListOfSchools");
//        Log.d("BOOBs",json);
//        Gson gson  = new Gson();
//        Type School_list = new TypeToken<ArrayList<SecondarySchool>>() {
//        }.getType();
//        final ArrayList<SecondarySchool>  ListOfSchools = gson.fromJson(json, School_list);
//        Log.d("BOOBS",ListOfSchools.size()+" schools recieved");
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
                    if (ChoiceOfCourses.contains(checkBox.getText())) {
                        ChoiceOfCourses.remove(checkBox.getText());
                        Log.d("BOOBS", checkBox.getText() + "is removed");
                    } else {
                        ChoiceOfCourses.add(checkBox.getText().toString());
                        Log.d("BOOBS", checkBox.getText() + "is added");
                    }
                }
            });
            layout.addView(checkBox);
//            View view = new View(this);
//            ViewGroup.LayoutParams params = view.getLayoutParams();
//            params.height = 1;
//            view.setLayoutParams(params);

        }

        FloatingActionButton next_button = (FloatingActionButton) findViewById(R.id.to_step_5);
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("BOOBS", ChoiceOfCourses.size() + " courses are selected!");
                Intent i  = new Intent(getApplicationContext(),Results_navigation.class);
                i.putExtra("SchoolLevel",Level);
                i.putExtra("isSpecialPlan", isSpecialPlan);
                i.putExtra("isAutonomous",isAutonomous);
                i.putExtra("isIndependent",isIndependent);
                i.putExtra("isInegrated",isInegrated);
                i.putExtra("ListOfCourses",ChoiceOfCourses);
//                i.putExtra("ListOfSchools", (Serializable)ListOfSchools);
                i.putExtra("myLatitude", intent.getDoubleExtra("myLatitude", 0.0));
                i.putExtra("myLongitude", intent.getDoubleExtra("myLongitude", 0.0));

                startActivity(i);
            }
        });

    }

    class DrawView extends View {
        Paint paint = new Paint();

        public DrawView(Context context) {
            super(context);
            paint.setColor(Color.BLUE);
        }
        @Override
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawLine(10, 20, 30, 40, paint);
            canvas.drawLine(20, 10, 50, 20, paint);

        }
    }
}
