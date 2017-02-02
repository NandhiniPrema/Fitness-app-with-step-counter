package com.example.mypc.keepfitapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

public class test_modeActivity extends AppCompatActivity {
    String testmode="TEST MODE ON";
    String history="display all data";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_mode);
        Button getdate=(Button)findViewById(R.id.date);
        getdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePicker testmodedate=(DatePicker)findViewById(R.id.datePicker);
                            int day=testmodedate.getDayOfMonth();
                            int month=testmodedate.getMonth()+1;
                            int year=testmodedate.getYear();
        String date=Integer.toString(day)+"-"+Integer.toString(month)+"-"+Integer.toString(year);
                Intent intent=new Intent(getApplicationContext(),AddGoalActivity.class);
                intent.putExtra("DATE",date);
                intent.putExtra("TEST",testmode);
                startActivity(intent);
            }
        });
        Button testhistory=(Button)findViewById(R.id.testhistory);
        testhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),HistoryActivity.class);
                intent.putExtra("HISTORY",history);
                startActivity(intent);
            }
        });

    }
}
