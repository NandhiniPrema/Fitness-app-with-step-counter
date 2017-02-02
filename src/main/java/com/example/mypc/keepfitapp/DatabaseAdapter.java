package com.example.mypc.keepfitapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.nfc.Tag;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.util.Log;
import android.widget.Toast;

import java.sql.SQLException;

/**
 * Created by My PC on 25/02/2016.
 */
public class DatabaseAdapter {
    public static final String R_ID = "ID";
    public static final String DATABASE_NAME = "DATABASE";
    public static final String TABLE_NAME = "GOALDATA";
    public static final String GOAL = "DBGOAL";
    public static final String STEPS = "DBSTEPS";
    public static final String UNIT = "STEPUNIT";
    public static final String DATE = "CREATEDATE";
    public static final String PROGRESS ="PROGRESS";
    public static final String STATUS="status";
    public static final String CREATEQUERY = "create table GOALDATA(ID integer primary key autoincrement,DBGOAL text,DBSTEPS text,STEPUNIT text,CREATEDATE text,PROGRESS integer,STATUS integer);";
    public static final int DATABASEVERSION=1;
    private DatabaseHelper Helper;
    private final Context ct;
    private SQLiteDatabase db;

    public DatabaseAdapter(Context ct) {
        this.ct = ct;
        Helper = new DatabaseHelper(ct);
    }
    private  class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context,DATABASE_NAME , null, DATABASEVERSION);
            System.out.println("Database created");
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try{
            db.execSQL(CREATEQUERY);
                System.out.println("Table created");

            }catch (SQLiteException e){
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS contacts");
            onCreate(db);
        }

    }
    public DatabaseAdapter open()throws SQLException{
        db= Helper.getWritableDatabase();
        return this;
    }
    public DatabaseAdapter openread()throws SQLException{
        db=Helper.getReadableDatabase();
        return this;
    }
   // public void close(){
     //   Helper.close();
    //}
    //---insert a contact into the database---
    public void InsertGoal(String u_goal, String u_steps,String u_unit,String u_date,int u_progress,int u_status)
    {
        ContentValues CV = new ContentValues();
        CV.put(GOAL, u_goal);
        CV.put(STEPS, u_steps);
        CV.put(UNIT,u_unit);
        CV.put(DATE,u_date);
        CV.put(PROGRESS,u_progress);
        CV.put(STATUS,u_status);
        db.insert(TABLE_NAME, null, CV);
        Toast.makeText(ct,"One row inserted",Toast.LENGTH_LONG).show();

    }
    //---delete all records
    public void deleteallgoal(){
        db.execSQL("delete  from "+TABLE_NAME);
        Toast.makeText(ct,"All records are deleted",Toast.LENGTH_LONG).show();
    }
    //---deletes a particular contact---
    public void deletegoal(int position)
    {
        db.delete(TABLE_NAME, R_ID + "=" + position, null);
        Toast.makeText(ct,"Data deleted successfully",Toast.LENGTH_LONG).show();
    }
    //---retrieves all the contacts---
    public Cursor getAllgoals()
    {
        return db.query(TABLE_NAME, new String[]{R_ID, GOAL, STEPS,UNIT,DATE,PROGRESS,STATUS}, null, null, null, null, null);

    }
    //--retrieves history data
    public Cursor History(String date){
       //final String historyquery="SELECT *FROM GOALDATA WHERE CREATEDATE="+date;
        //return db.rawQuery(historyquery,null);
        return db.query(TABLE_NAME, new String[]{R_ID, GOAL, STEPS,UNIT, DATE,PROGRESS},DATE + "=" +date,null,null, null, null, null);

        }

    //---retrieves a particular contact---
    public Cursor getsingle(int ID) throws SQLException
    {
        Cursor mCursor = db.query(true, TABLE_NAME, new String[] {R_ID,GOAL, STEPS,DATE}, R_ID + "="+ ID, null,null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    //---updates a contact---
    public void updategoal(int position, String goal, String steps,String unit) {
        ContentValues cv = new ContentValues();
        cv.put(GOAL, goal);
        cv.put(STEPS, steps);
        cv.put(UNIT,unit);
        //cv.put(PROGRESS,progress);
        db.update(TABLE_NAME, cv, R_ID + "=" + position, null);
        Toast.makeText(ct, "Data Updated Successfully", Toast.LENGTH_SHORT).show();
    }
    public void updateprogress(int currentsteps,int position){
        ContentValues cv=new ContentValues();
        cv.put(PROGRESS, currentsteps);
        db.update(TABLE_NAME, cv, R_ID + "=" + position, null);

    }
    public Cursor getstatus(){
        return db.query(TABLE_NAME, new String[]{STATUS},null,null,null,null,null);
    }
    public void updatestatus(int status,int position){
        ContentValues CV=new ContentValues();
        CV.put(STATUS,status);
        db.update(TABLE_NAME,CV,R_ID+"="+position,null);
    }



}