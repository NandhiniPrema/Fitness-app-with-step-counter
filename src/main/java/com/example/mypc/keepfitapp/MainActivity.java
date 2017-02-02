package com.example.mypc.keepfitapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;
import android.widget.ListAdapter;

import java.sql.SQLException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DatabaseAdapter db;
    Context ct=this;
    int steps;
    private static final int PROGRESS = 0x1;
    private ProgressBar mProgress;
    ArrayList<String> statuslist;
    ArrayList<String> steplist;
    ArrayList<String> unitlist;
    ArrayList<String> defaultprogress;
    ProgressBar progressBar=null;
    EditText progres;
    int newsteps;
    int dbsteps;
    String unit="Steps";
    String []Units;
    String testmode="TEST MODE ON";
    String deactive="TEST MODE OFF";
    String history="Display main history";
    Switch Stepcounter=null;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        Stepcounter=(Switch)findViewById(R.id.stepcounter);
        Stepcounter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent intent1 = new Intent(ct, StepCounterActivity.class);
                intent1.putExtra("POSITION", position);
                startActivity(intent1);
            }
        });

            Toast.makeText(ct,"You have chosen manual update",Toast.LENGTH_LONG).show();


        final Intent intent=new Intent(ct,HistoryActivity.class);
        intent.putExtra("HISTORY", history);
        TextView selected=(TextView)findViewById(R.id.itemid);
        mProgress=(ProgressBar)findViewById(R.id.progressBar);
        statuslist=new ArrayList<String>();
        steplist=new ArrayList<String>();
        unitlist=new ArrayList<String>();
        defaultprogress=new ArrayList<String>();
    // id=getIntent().getExtras().getInt("ACTIVE_ITEM");
        db=new DatabaseAdapter(ct);
        try {
            db.openread();
            Cursor cursor=db.getAllgoals();
            if(cursor.moveToFirst()){
                do {
                    String total = cursor.getString(2);
                    steplist.add(total);
                    String goalunit=cursor.getString(3);
                    unitlist.add(goalunit);
                    String previousprogress=cursor.getString(5);
                    defaultprogress.add(previousprogress);
                    String status = cursor.getString(6);
                    statuslist.add(status);
                }while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ArrayList<String> onoff=new ArrayList<String>();
        //selected.setText(statuslist.toString());
        for (int i=0;i<statuslist.size();i++){
            if((statuslist.get(i)).equals("1")) {
                onoff.add("ON");
            }else{
                onoff.add("OFF");

            }
        }
        selected.setText(onoff.toString());
        //---do some work in background thread---
        for(int i=0;i<statuslist.size();i++) {
            String status = statuslist.get(i);
            int status1 = Integer.parseInt(status);
            if (status1 == 1) {
                position = i + 1;
                String tempstep = steplist.get(i);
                steps = Integer.parseInt(tempstep);
                mProgress.setMax(steps);
                String goalunitagain = unitlist.get(i);
                Toast.makeText(ct, "Respective goal unit is" + goalunitagain, Toast.LENGTH_LONG).show();
                String previousstep = defaultprogress.get(i);
                dbsteps = Integer.parseInt(previousstep);
                mProgress.setProgress(dbsteps);


            }

        }
        FloatingActionButton progress=(FloatingActionButton)findViewById(R.id.updatebutton);
            progres = (EditText) findViewById(R.id.update);
        progress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String current = progres.getText().toString();
                    int current1=Integer.parseInt(current);
                    for (int i = 0; i < statuslist.size(); i++) {
                        String status = statuslist.get(i);
                        String goalunit=unitlist.get(i);
                        if(goalunit.equals(unit)){
                            newsteps=dbsteps+current1;
                        }
                        else{
                             if((goalunit.equals("Steps"))&&unit.equals("Meters")){
                                Double tempnewsteps=current1*3.20;
                                 newsteps=dbsteps+tempnewsteps.intValue();
                             }
                            else if(goalunit.equals("Steps")&&unit.equals("Yards")){
                                 newsteps=dbsteps+current1*3;
                             }
                            else if(goalunit.equals("Steps")&&unit.equals("Kilometers")){
                                 newsteps=dbsteps+current1*3280;
                             }
                            else if(goalunit.equals("Steps")&&unit.equals("Miles")){
                                 newsteps=dbsteps+current1*5280;
                             }
                            else if(goalunit.equals("Meters")&&unit.equals("Steps")){
                                 Double tempnewstep=current1/3.20;
                                 newsteps=dbsteps+tempnewstep.intValue();
                             }
                            else if(goalunit.equals("Meters")&&unit.equals("Yards")){
                                 newsteps=dbsteps+current1;
                             }
                            else if(goalunit.equals("Meters")&&unit.equals("Kilometers")){
                                 newsteps=dbsteps+current1*100;
                             }
                            else if(goalunit.equals("Meters")&&unit.equals("Miles")){
                                 newsteps=dbsteps+current1*1609;
                             }
                            else if(goalunit.equals("Yards")&&unit.equals("Steps")){
                                 Double tempnewsteps=current1/3.01;
                                 newsteps=dbsteps+tempnewsteps.intValue();
                             }
                            else if(goalunit.equals("Yards")&&unit.equals("Meters")){
                                 newsteps=dbsteps+current1;
                             }
                            else if(goalunit.equals("Yards")&&unit.equals("Kilometers")){
                                 newsteps=dbsteps+current1*100;
                             }
                            else if(goalunit.equals("Yards")&&unit.equals("Miles")){
                                 newsteps=dbsteps+current1*1760;
                             }
                            else if(goalunit.equals("Kilometers")&&unit.equals("Steps")){
                                 Double tempkilometer=current1/3280.84;
                                 newsteps=dbsteps+tempkilometer.intValue();
                             }
                            else if(goalunit.equals("Kilometers")&&unit.equals("Meters")){
                                Double tmpnewsteps=current1/100.00;
                                 newsteps=dbsteps+tmpnewsteps.intValue();
                             }
                            else if (goalunit.equals("Kilometers")&&unit.equals("Yards")){
                                Double tmpkilo=current1/100.00;
                                 newsteps=dbsteps+tmpkilo.intValue();
                             }
                            else if (goalunit.equals("Kilometers")&&unit.equals("Miles")){
                                 Double tmp=current1*0.6213;
                                 newsteps=dbsteps+tmp.intValue();
                             }
                            else if (goalunit.equals("Miles")&&unit.equals("Steps")){
                                 Double tmp=current1/5280.0;
                                 newsteps=dbsteps+tmp.intValue();
                             }
                            else if(goalunit.equals("Miles")&&unit.equals("Meters")){
                                 Double tmp=current1/1609.34;
                                 newsteps=dbsteps+tmp.intValue();
                             }
                            else if (goalunit.equals("Miles")&&unit.equals("Yards")){
                                 Double tmp=current1/1760.0;
                                 newsteps=dbsteps+tmp.intValue();
                             }
                            else if (goalunit.equals("Miles")&&unit.equals("Kilometers")){
                                 Double tmp=current1*1.60;
                                 newsteps=dbsteps+tmp.intValue();
                             }
                        }
                        int savedstatus = Integer.parseInt(status);
                        if (savedstatus == 1) {
                            db.open();
                            db.updateprogress(newsteps, i + 1);
                            Toast.makeText(ct,"New steps updated successfully",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(ct,MainActivity.class));
                        } else {
                            Toast.makeText(ct, "Currently there are 0 active goals", Toast.LENGTH_LONG).show();
                        }
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.GOAL_SETTING) {
            Intent i=new Intent(ct,HistoryActivity.class);
            i.putExtra("HISTORY",history);
            Intent intent=new Intent(ct,AddGoalActivity.class);
            intent.putExtra("TEST",deactive);
            startActivity(intent);
        }
        else if(id== R.id.Test_mode){
            Intent i=new Intent(ct,HistoryActivity.class);
            i.putExtra("HISTORY",history);
            Intent intent=new Intent(ct,test_modeActivity.class);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent i=new Intent(ct,HistoryActivity.class);
            i.putExtra("HISTORY",history);
            startActivity(i);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
