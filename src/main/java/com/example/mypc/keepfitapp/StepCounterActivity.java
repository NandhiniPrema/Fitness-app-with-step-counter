package com.example.mypc.keepfitapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;

public class StepCounterActivity extends AppCompatActivity implements SensorEventListener {


        private float mStepSinceBoot, mLastStepReport, mCurrSteps;
        private boolean mInitialized;
        private SensorManager mSensorManager;
        private Sensor mStepCounter;
        DatabaseAdapter db;
            int position;
        Context ct=this;
    int stepcountersteps=0;
    String respectiveunit;
    ArrayList<String> unit=new ArrayList<String>();

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_step_counter);
            position=getIntent().getExtras().getInt("POSITION");
            db=new DatabaseAdapter(ct);
            try {
                db.openread();
                Cursor CR=db.getAllgoals();
                if(CR.moveToFirst()){
                    do{
                        String stepunit=CR.getString(3);
                        unit.add(stepunit);
                    }while(CR.moveToNext());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            mInitialized = false;
            mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            mStepCounter = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            mSensorManager.registerListener(this, mStepCounter, SensorManager.SENSOR_DELAY_NORMAL);
            Button updatecurrent=(Button)findViewById(R.id.current);
            updatecurrent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        respectiveunit = unit.get(position - 1);
                        Toast.makeText(ct, "Unit is " + respectiveunit, Toast.LENGTH_LONG).show();

                        if (respectiveunit.equals("Steps")) {
                            stepcountersteps = (int) mCurrSteps;
                        } else if (respectiveunit.equals("Meters")) {
                            stepcountersteps = (int) (mCurrSteps / 3.20);
                        } else if (respectiveunit.equals("Yards")) {
                            stepcountersteps = (int) (mCurrSteps / 3);
                        } else if (respectiveunit.equals("Kilometers")) {
                            stepcountersteps = (int) (mCurrSteps / 3280);
                        } else if (respectiveunit.equals("Miles")) {
                            stepcountersteps = (int) (mCurrSteps / 5280);
                        }
                        db.open();
                        db.updateprogress(stepcountersteps, position);
                        Toast.makeText(StepCounterActivity.this, "Steps updated successfully", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
            Button updateall=(Button)findViewById(R.id.all);
            updateall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        respectiveunit=unit.get(position-1);
                        Toast.makeText(ct,"Unit is "+respectiveunit,Toast.LENGTH_LONG).show();

                        if(respectiveunit.equals("Steps")){
                            stepcountersteps= (int) mStepSinceBoot;
                        }
                        else if(respectiveunit.equals("Meters")){
                            stepcountersteps= (int) (mStepSinceBoot/3.20);
                        }
                        else if (respectiveunit.equals("Yards")){
                            stepcountersteps= (int) (mStepSinceBoot/3);
                        }
                        else if (respectiveunit.equals("Kilometers")){
                            stepcountersteps= (int) (mStepSinceBoot/3280);
                        }
                        else if (respectiveunit.equals("Miles")){
                            stepcountersteps= (int) (mStepSinceBoot/5280);
                        }

                        db.open();
                        db.updateprogress(stepcountersteps, position);
                        Toast.makeText(StepCounterActivity.this,"Steps updated successfully",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }
            });
        }

        @Override
        protected void onStart() {
            super.onStart();
        }

        @Override
        protected void onResume() {
            super.onResume();
        }

        @Override
        protected void onPause() {
            super.onPause();
        }

        @Override
        protected void onStop() {
            super.onStop();
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            mSensorManager.	unregisterListener(this);
        }

    @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // can be safely ignored for this demo
        }
 @Override
        public void onSensorChanged(SensorEvent event) {
            TextView startOffset = (TextView)findViewById(R.id.steps_since_boot);
            TextView lastReport = (TextView)findViewById(R.id.last_step_report);
            TextView currSteps = (TextView)findViewById(R.id.current_steps);

            mLastStepReport = event.values[0];
            if (!mInitialized) {
                mStepSinceBoot = mLastStepReport;
                mCurrSteps = 0;
                startOffset.setText(Float.toString(mStepSinceBoot));
                lastReport.setText(Float.toString(mLastStepReport));
                currSteps.setText("0.0");
                mInitialized = true;
            } else {
                mCurrSteps = (mLastStepReport - mStepSinceBoot);
                lastReport.setText(Float.toString(mLastStepReport));
                currSteps.setText(Float.toString(mCurrSteps));
                Intent intent=new Intent(this,MainActivity.class);
                intent.putExtra("STEPCOUNTERVALUE",mCurrSteps);
            }
        }

}
