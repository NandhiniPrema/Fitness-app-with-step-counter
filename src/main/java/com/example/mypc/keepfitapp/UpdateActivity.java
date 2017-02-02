package com.example.mypc.keepfitapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.SQLException;

public class UpdateActivity extends AppCompatActivity {
    DatabaseAdapter db;
    Context CT=this;
    EditText updatedgoal=null;
    EditText updatedsteps=null;
    EditText progress=null;
    String U_GOAL=null;
    String U_STEPS=null;
    String previousgoal,previousstep;
    int U_PROGRESS=0;
    String []Units;
    String unit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        Units=getResources().getStringArray(R.array.step_units);
        Spinner s1=(Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,Units);
        s1.setAdapter(arrayAdapter);
        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int index = parent.getSelectedItemPosition();
                // Toast.makeText(getBaseContext(),"You have selected item :"  + Units[index],
                // Toast.LENGTH_SHORT).show();
                unit = Units[index];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        updatedgoal=(EditText)findViewById(R.id.newgoal);
        updatedsteps=(EditText)findViewById(R.id.newsteps);
        //progress=(EditText)findViewById(R.id.progress);
        final int id=getIntent().getExtras().getInt("Position");
        Button Update=(Button)findViewById(R.id.update);
        db=new DatabaseAdapter(CT);
        try {
            db.open();
            Cursor cr=db.getsingle(id);
            if(cr.moveToFirst()){
                //String id=cr.getString(0);
                previousgoal=cr.getString(1);
                previousstep=cr.getString(2);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        updatedgoal.setText(previousgoal);
        updatedsteps.setText(previousstep);
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    U_GOAL=updatedgoal.getText().toString();
                    U_STEPS=updatedsteps.getText().toString();
                    //U_PROGRESS=Integer.parseInt(progress.getText().toString());
                    Toast.makeText(CT,"Your ID is"+id,Toast.LENGTH_LONG).show();
                    db.updategoal(id,U_GOAL,U_STEPS,unit);
                    Toast.makeText(CT,"Goal updated successfully",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getBaseContext(),AddGoalActivity.class));



            }
        });
    }
}
