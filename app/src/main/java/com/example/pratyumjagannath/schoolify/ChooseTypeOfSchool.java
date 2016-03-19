package com.example.pratyumjagannath.schoolify;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
        chkIntegrated = (CheckBox) findViewById(R.id.special_assistance_plan_school);
        btnDisplay = (Button) findViewById(R.id.to_Step_4);

        btnDisplay.setOnClickListener(new View.OnClickListener() {

            //Run when button is clicked
            @Override
            public void onClick(View v) {

                StringBuffer result = new StringBuffer();
                result.append("Autonomous check : ").append(chkAutonomous.isChecked());
                result.append("\nIndependent check : ").append(chkIndependent.isChecked());
                result.append("\nIntegrated check :").append(chkIntegrated.isChecked());
                result.append("\nSpecial Assistance Plan check :").append(chkSpecialPlan.isChecked());
                Toast.makeText(getBaseContext(), result.toString(),
                        Toast.LENGTH_LONG).show();

            }
        });

    }

}
