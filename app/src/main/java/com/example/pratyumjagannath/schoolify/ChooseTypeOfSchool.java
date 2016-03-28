package com.example.pratyumjagannath.schoolify;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class ChooseTypeOfSchool extends AppCompatActivity {

    private CheckBox chkAutonomous, chkIndependent, chkIntegrated,chkSpecialPlan;
    private Button btnDisplay;

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
        btnDisplay = (Button) findViewById(R.id.to_Step_4);

        btnDisplay.setOnClickListener(new View.OnClickListener() {

            //Run when button is clicked
            @Override
            public void onClick(View v) {
                Log.d("BOOBS","Button Pressed!");
                StringBuffer result = new StringBuffer();
                Intent i = getIntent();
                Intent intent = new Intent(getApplicationContext(),ChooseCourses.class);
                intent.putExtra("Coordinates",i.getSerializableExtra("Coordinates"));
                intent.putExtra("SchoolLevel", i.getSerializableExtra("SchoolLevel"));
                intent.putExtra("myLatitude",i.getDoubleExtra("myLatitude",0.0));
                intent.putExtra("myLongitude",i.getDoubleExtra("myLongitude",0.0));

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
                Toast.makeText(getBaseContext(), result.toString(),
                        Toast.LENGTH_LONG).show();
                startActivity(intent);
            }
        });

    }

}