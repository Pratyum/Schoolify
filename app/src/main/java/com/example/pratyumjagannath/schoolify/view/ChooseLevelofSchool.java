package com.example.pratyumjagannath.schoolify.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.pratyumjagannath.schoolify.R;

public class ChooseLevelofSchool extends AppCompatActivity {

    private RadioGroup radioGroup;
    private RadioButton radioButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_levelof_school);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        addListenerOnButton();

    }


    public void addListenerOnButton() {

        radioGroup = (RadioGroup) findViewById(R.id.level_of_school);
        FloatingActionButton next_button = (FloatingActionButton) findViewById(R.id.to_step_3);
        next_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // get selected radio button from radioGroup
                int selectedId = radioGroup.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                radioButton = (RadioButton) findViewById(selectedId);
                Toast.makeText(getBaseContext(),
                        radioButton.getText(), Toast.LENGTH_SHORT).show();
                Intent i = getIntent();
//                ArrayList<School> ListOfSchools = (ArrayList<School>) i.getSerializableExtra("ListOfSchools");
//                Log.d("BOOBS", ListOfSchools.size() + " is the size in Choose Level");
                Intent intent = new Intent(getApplicationContext(),ChooseTypeOfSchool.class);
                intent.putExtra("SchoolLevel",radioButton.getText());
                intent.putExtra("myLatitude",i.getDoubleExtra("myLatitude",0.0));
                intent.putExtra("myLongitude",i.getDoubleExtra("myLongitude",0.0));
                intent.putExtra("ListOfSchools",i.getStringExtra("ListOfSchools"));
//                intent.putExtra("ListOfSchools",(Serializable)ListOfSchools);
                startActivity(intent);

            }

        });

    }

}
