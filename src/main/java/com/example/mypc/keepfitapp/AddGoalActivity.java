package com.example.mypc.keepfitapp;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Checkable;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class AddGoalActivity extends ListActivity {
    EditText goal;EditText steps;
    DatabaseAdapter db;
    Context CT=this;
    Button SAVE=null;
    Button Display=null;
    ArrayList<String> list;
    ArrayList<String> statuslist=new ArrayList<String>();
    int item=0;
    int initialprogress=0;
    int status=0;
    ListView listView;
    int activatedposition;
    int settest;
     TextView Status=null;
    TextView MODE=null;
    String todaydate;
    boolean testmode;
    String testmodedate;
    String mainmodedate;
    String mode=null;
    String history="Display main history";
    String []Units;
    String unit;
    String deactive="TEST MODE OFF";
    double stepwithunit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal);
        goal = (EditText) findViewById(R.id.goal);
        steps = (EditText) findViewById(R.id.steps);
        SAVE = (Button) findViewById(R.id.savegoal);
        Display = (Button) findViewById(R.id.viewgoal);
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
                 unit=Units[index];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mode=getIntent().getExtras().getString("TEST");
        MODE=(TextView)findViewById(R.id.testmode);
        MODE.setText(mode);
            if(mode.equals("TEST MODE ON")){
            Toast.makeText(CT,"You are in test mode",Toast.LENGTH_LONG).show();
                testmodedate=getIntent().getExtras().getString("DATE");
            testmode=true;
            }else {
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                Calendar cal = Calendar.getInstance();
                //cal.add(Calendar.DATE, -1);
                mainmodedate=dateFormat.format(cal.getTime());
                testmode=false;
            }
        db = new DatabaseAdapter(CT);
        try {
            db.open();
            SAVE.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String goals = goal.getText().toString();
                    final String step = steps.getText().toString();
                    if(testmode==true){
                    db.InsertGoal(goals,step,unit,testmodedate, initialprogress, status);
                        Intent intent=new Intent(CT,AddGoalActivity.class);
                        intent.putExtra("TEST",deactive);
                        startActivity(intent);
                    }else {
                        db.InsertGoal(goals,step,unit,mainmodedate,initialprogress,status);
                        Intent intent=new Intent(CT,AddGoalActivity.class);
                        intent.putExtra("TEST",deactive);
                        startActivity(intent);
                    }

                    Toast.makeText(v.getContext(), "Data saved successfully", Toast.LENGTH_LONG).show();

                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
       // final TextView content = (TextView) findViewById(R.id.item);
        db = new DatabaseAdapter(CT);
        list = new ArrayList<String>();
        try {
            db.openread();
            Cursor cursor = db.getAllgoals();
            if (cursor.moveToFirst()) {
                do {
                    //Toast.makeText(ct,"id: " + cursor.getString(0) + "\n" +"Goal: " + cursor.getString(1) + "\n"+"Steps: " + cursor.getString(2)+ "\n" + "Created date : " + cursor.getString(3), Toast.LENGTH_LONG).show();
                    String record1 = cursor.getString(0);
                    String record2 = cursor.getString(1);
                    String record3 = cursor.getString(2);
                    String record4 = cursor.getString(3);
                    String record5 = cursor.getString(4);
                    String record6 = cursor.getString(5);
                    String record7 = cursor.getString(6);
                    list.add(record1 + " " + record2 + " " + record3 + " " + record4 + " " + record5);
                    statuslist.add(record7);
                } while (cursor.moveToNext());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        Display.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Intent i = new Intent(CT, HistoryActivity.class);
                i.putExtra("HISTORY", history);
                startActivity(i);

            }
        });
         listView = getListView();
        listView.setChoiceMode(1);
       ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, list);
        setListAdapter(adapter);
       for (int i=0;i<statuslist.size();i++){
           String test=statuslist.get(i);
           settest=Integer.parseInt(test);
            if(settest==1){
                listView.setItemChecked(i,true);
                activatedposition=i;
                Intent intent=new Intent(CT,MainActivity.class);
                intent.putExtra("ACTIVE_ITEM",activatedposition);
                Toast.makeText(CT,"You have activated the "+i+" goal",Toast.LENGTH_LONG).show();
            }

        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                item = listView.getCheckedItemPosition();
                status = 1;
                try {
                    db.open();
                    db.updatestatus(status, item + 1);
                    db.updatestatus(0, activatedposition + 1);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(CT,"You have activated the"+item+"goal",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(CT,AddGoalActivity.class);
                intent.putExtra("TEST",deactive);
                startActivity(intent);

            }

            });
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()

            {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view,
                                               final int position, long id) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CT);
                    CheckedTextView check = (CheckedTextView) view;
                    if (check.isChecked()) {

                        Toast.makeText(CT, "Active goal can't edit", Toast.LENGTH_LONG).show();

                    } else {
                        builder.setTitle("EDIT or DELETE");
                        // set dialog message
                        builder.setMessage("What do you want to do");
                        builder.setCancelable(true);
                        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Toast.makeText(CT, "You have selected update operation", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(CT, UpdateActivity.class);
                                intent.putExtra("Position", position + 1);
                                startActivity(intent);

                            }
                        }).setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //Toast.makeText(getContext(),"You have selected delete operation",Toast.LENGTH_LONG).show();
                                AlertDialog.Builder builder2 = new AlertDialog.Builder(CT);
                                builder2.setTitle("DELETE Operation");
                                // set dialog message
                                builder2.setMessage("Are you sure want to delete record");
                                builder2.setCancelable(true);
                                builder2.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        db = new DatabaseAdapter(CT);
                                        try {
                                            db.open();
                                            db.deletegoal(position + 1);
                                            Intent intent = new Intent(CT, AddGoalActivity.class);
                                            CT.startActivity(intent);
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }


                                    }
                                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(CT, "You canceled the operation", Toast.LENGTH_LONG).show();
                                    }
                                });
                                // create alert dialog
                                AlertDialog alertDialog = builder2.create();

                                // show it
                                alertDialog.show();
                            }


                        });

                        // create alert dialog
                        AlertDialog alertDialog = builder.create();

                        // show it
                        alertDialog.show();
                    }

                    return true;

                }

            });


        }


    }
