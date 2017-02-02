package com.example.mypc.keepfitapp;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class HistoryActivity extends ListActivity {
    DatabaseAdapter db;
    Context CT=this;
    String record1,record2,record3,record4,record5;
    TextView  id,goal,steps,date,progress;
    TextView yesterday=null;
    TextView testdata;
    TextView Pastdate;
    String [] History;
    String historydate;
    int sum=0;
    String pass;
  //  ListView listView;
    ArrayList<String> history=new ArrayList<String>();
    ArrayList<String> datewisegoal = new ArrayList<String>();
    ArrayList<String> datewisehitory = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        pass = getIntent().getExtras().getString("HISTORY");
        if(pass.equals("display all data")) {
            // testdata.setText("TEST MODE HISTORY");
            Toast.makeText(CT,"This is testdata history",Toast.LENGTH_LONG).show();
            ListView listView2=getListView();
            //listView.setChoiceMode(1);
            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(CT,android.R.layout.simple_list_item_1, history);
            setListAdapter(adapter2);
        }
        else {

            final TextView date = (TextView) findViewById(R.id.dateof);
            History = getResources().getStringArray(R.array.history_array);
            Spinner s1 = (Spinner) findViewById(R.id.historyspinner);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, History);
            s1.setAdapter(arrayAdapter);
            s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    int index = parent.getSelectedItemPosition();
                    Toast.makeText(getBaseContext(), "You have selected item :" + History[index],
                            Toast.LENGTH_SHORT).show();
                    historydate = History[index];
                    if (historydate.equals("Today")) {
                        Toast.makeText(CT, "This is today's history", Toast.LENGTH_LONG).show();
                        for (int i = 0; i < datewisegoal.size(); i++) {
                            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                            Calendar cal = Calendar.getInstance();
                            String todaydate = dateFormat.format(cal.getTime());
                            if (datewisegoal.get(i).compareTo(todaydate) < 0) {
                                //testdata.setText("MAIN MODE HISTORY");
                                // Toast.makeText(CT, "This is mainmode", Toast.LENGTH_LONG).show();
                                datewisehitory.add(history.get(i));
                                ListView listView1 = getListView();
                                // listView.setChoiceMode(1);
                                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(CT, android.R.layout.simple_list_item_1, datewisehitory);
                                setListAdapter(adapter1);

                            }
                        }
                        //date.setText(datewisegoal.toString());
                    } else if (historydate.equals("Yesterday")) {
                        Toast.makeText(CT, "This is yesterday history", Toast.LENGTH_LONG).show();
                        for (int i = 0; i < datewisegoal.size(); i++) {
                            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                            Calendar cal = Calendar.getInstance();
                            cal.add(Calendar.DATE, -1);
                            String yesterday = dateFormat.format(cal.getTime());
                            if (datewisegoal.get(i).compareTo(yesterday) < 0) {
                                //testdata.setText("MAIN MODE HISTORY");
                                Toast.makeText(CT, "This is mainmode", Toast.LENGTH_LONG).show();
                                datewisehitory.add(history.get(i));
                                ListView listView2 = getListView();
                                // listView.setChoiceMode(1);
                                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(CT, android.R.layout.simple_list_item_1, datewisehitory);
                                setListAdapter(adapter2);

                            }
                        }
                        //date.setText(datewisehitory.toString());
                    } else if (historydate.equals("Last-Week")) {
                        Toast.makeText(CT, "This is last week history", Toast.LENGTH_LONG).show();
                        for (int i = 0; i < datewisegoal.size(); i++) {
                            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                            Calendar cal = Calendar.getInstance();
                            cal.add(Calendar.DATE, -7);
                            String pastweek = dateFormat.format(cal.getTime());
                            if (datewisegoal.get(i).compareTo(pastweek) < 0) {
                                //testdata.setText("MAIN MODE HISTORY");
                                Toast.makeText(CT, "This is mainmode", Toast.LENGTH_LONG).show();
                                datewisehitory.add(history.get(i));
                                ListView listView3 = getListView();
                                // listView.setChoiceMode(1);
                                ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(CT, android.R.layout.simple_list_item_1, datewisehitory);
                                setListAdapter(adapter3);
                            }
                        }
                        //date.setText(datewisehitory.toString());

                    } else if (historydate.equals("Last-Month")) {
                        Toast.makeText(CT, "This is last month", Toast.LENGTH_LONG).show();
                        for (int i = 0; i < datewisegoal.size(); i++) {
                            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                            Calendar cal = Calendar.getInstance();
                            cal.add(Calendar.DATE, -30);
                            String lastmonth = dateFormat.format(cal.getTime());
                            if (datewisegoal.get(i).compareTo(lastmonth) < 0) {
                                //testdata.setText("MAIN MODE HISTORY");
                                Toast.makeText(CT, "This is mainmode", Toast.LENGTH_LONG).show();
                                datewisehitory.add(history.get(i));
                                ListView listView4 = getListView();
                                // listView.setChoiceMode(1);
                                ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(CT, android.R.layout.simple_list_item_1, datewisehitory);
                                setListAdapter(adapter4);
                            }
                        }
                        //date.setText(datewisehitory.toString());



                    } else if (historydate.equals("Past")) {
                        Toast.makeText(CT, "This is past history", Toast.LENGTH_LONG).show();
                        for (int i = 0; i < datewisegoal.size(); i++) {
                            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                            Calendar cal = Calendar.getInstance();
                            cal.add(Calendar.DATE, -32);
                            String past = dateFormat.format(cal.getTime());
                            if (datewisegoal.get(i).compareTo(past) < 0) {
                                testdata.setText("MAIN MODE HISTORY");
                                Toast.makeText(CT, "This is mainmode", Toast.LENGTH_LONG).show();
                                datewisehitory.add(history.get(i));
                                ListView listView5 = getListView();
                                // listView.setChoiceMode(1);
                                ArrayAdapter<String> adapter5 = new ArrayAdapter<String>(CT, android.R.layout.simple_list_item_1, datewisehitory);
                                setListAdapter(adapter5);
                            }
                        }
                        //date.setText(datewisehitory.toString());

                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
          db = new DatabaseAdapter(CT);
        try {
            db.open();
            Cursor cursor = db.getAllgoals();
            if (cursor.moveToFirst()) {
                do {
                    //Toast.makeText(ct,"id: " + cursor.getString(0) + "\n" +"Goal: " + cursor.getString(1) + "\n"+"Steps: " + cursor.getString(2)+ "\n" + "Created date : " + cursor.getString(3), Toast.LENGTH_LONG).show();
                    String record1 = cursor.getString(0);
                    //history.add(record1);
                    String record2 = cursor.getString(1);
                    //history.add(record2);
                    String record3 = cursor.getString(2);
                    //history.add(record3);
                    String record4 = cursor.getString(3);
                    //history.add(record4);
                    String record5 = cursor.getString(4);
                    datewisegoal.add(record5);
                    String record6 = cursor.getString(5);
                    history.add(record2 + "        " + record3 + record4 + "                   "+record6);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        db=new DatabaseAdapter(CT);
        Button clearhistory=(Button)findViewById(R.id.clearhistory);
        clearhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String history="Display main history";
                    db.open();
                    db.deleteallgoal();
                    Intent i=new Intent(CT,HistoryActivity.class);
                    i.putExtra("HISTORY", history);
                    startActivity(i);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });


    }

}
