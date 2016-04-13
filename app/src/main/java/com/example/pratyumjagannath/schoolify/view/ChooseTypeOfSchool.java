package com.example.pratyumjagannath.schoolify.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.pratyumjagannath.schoolify.R;

public class ChooseTypeOfSchool extends AppCompatActivity {

    private CheckBox chkAutonomous, chkIndependent, chkIntegrated,chkSpecialPlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_type_of_school);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        addListenerOnButton();
    }

    public void addListenerOnButton() {

        chkAutonomous = (CheckBox) findViewById(R.id.autonomous_school);
        chkIndependent = (CheckBox) findViewById(R.id.independent_school);
        chkIntegrated = (CheckBox) findViewById(R.id.integrated_school);
        chkSpecialPlan = (CheckBox) findViewById(R.id.special_assistance_plan_school);
        FloatingActionButton next_button = (FloatingActionButton) findViewById(R.id.to_step_4);

        next_button.setOnClickListener(new View.OnClickListener() {

            //Run when button is clicked
            @Override
            public void onClick(View v) {
                Log.d("BOOBS","Button Pressed!");
                StringBuffer result = new StringBuffer();
                Intent i = getIntent();
//                ArrayList<School> ListOfSchools = (ArrayList<School>) i.getSerializableExtra("ListOfSchools");
//                Log.d("BOOBS",ListOfSchools.size()+" is the size in Choose Type");
                Intent intent = new Intent(getApplicationContext(),ChooseCourses.class);
                intent.putExtra("Coordinates",i.getSerializableExtra("Coordinates"));
                intent.putExtra("SchoolLevel", i.getSerializableExtra("SchoolLevel"));
                intent.putExtra("myLatitude",i.getDoubleExtra("myLatitude", 0.0));
                intent.putExtra("myLongitude",i.getDoubleExtra("myLongitude",0.0));
                intent.putExtra("ListOfSchools",i.getStringExtra("ListOfSchools"));

                if(chkSpecialPlan!=null) {
                    result.append("Special Plan check : ").append(chkSpecialPlan.isChecked() + "\n");
                    intent.putExtra("isSpecialPlan", chkSpecialPlan.isChecked());
                }

                if(chkAutonomous!=null) {
                    result.append("Autonomous check : ").append(chkAutonomous.isChecked() + "\n");
                    intent.putExtra("isAutonomous", chkAutonomous.isChecked());
                }

                if(chkIntegrated!=null) {
                    result.append("Integrated check : ").append(chkIntegrated.isChecked() + "\n");
                    intent.putExtra("isInegrated", chkIntegrated.isChecked());
                }
                if (chkIndependent != null) {
                    result.append("Independent check : ").append(chkIndependent.isChecked() + "\n");
                    intent.putExtra("isIndependent", chkIndependent.isChecked());
                }
//                Toast.makeText(getBaseContext(), result.toString(),
//                        Toast.LENGTH_LONG).show();
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_others, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            Intent i= new Intent(this,Schoolify.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
