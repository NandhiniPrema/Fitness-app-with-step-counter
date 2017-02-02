package com.example.mypc.keepfitapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.widget.ToggleButton;

/**
 * Created by My PC on 26/02/2016.
 */
public class CursorListAdapter extends ArrayAdapter {
    Context context;
    DatabaseAdapter db;
    public SharedPreferences preferences;
    final String PREF_NAME="preferences";
    RadioButton selection;
    boolean status;
    public CursorListAdapter(Context context, ArrayList<String> List) {
        super(context, R.layout.displaylist, List);
        this.context=context;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View rowview = inflater.inflate(R.layout.displaylist, parent, false);
        final TextView name = (TextView) rowview.findViewById(R.id.id);
        final RadioButton choice=(RadioButton)rowview.findViewById(R.id.choice);
       for(int i=0;i< getCount();i++){
        if(choice.isChecked()){
            status=true;
            Toast.makeText(getContext(),"You have activated the goal",Toast.LENGTH_LONG).show();
        }
       }
            name.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(getContext(), "You clicked on an item", Toast.LENGTH_LONG).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("EDIT or DELETE");
                    // set dialog message
                    builder.setMessage("What do you want to do");
                    builder.setCancelable(true);
                    builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            Toast.makeText(getContext(), "You have selected update operation", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getContext(), UpdateActivity.class);
                            intent.putExtra("Position", position + 1);
                            getContext().startActivity(intent);

                        }
                    }).setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //Toast.makeText(getContext(),"You have selected delete operation",Toast.LENGTH_LONG).show();
                            AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());
                            builder2.setTitle("DELETE Operation");
                            // set dialog message
                            builder2.setMessage("Are you sure want to delete record");
                            builder2.setCancelable(true);
                            builder2.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    db = new DatabaseAdapter(context);
                                    try {
                                        db.open();
                                        db.deletegoal(position + 1);
                                        Intent intent = new Intent(getContext(), MainActivity.class);
                                        getContext().startActivity(intent);
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }


                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getContext(), "You canceled the operation", Toast.LENGTH_LONG).show();
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

                    return true;
                }

            });

            String single = (String) getItem(position);
            name.setText(single);
        return rowview;
    }



}
