package com.wambly.iytem;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class MonthlyMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.food);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        ListView lv = (ListView) findViewById(R.id.menuList);
        ArrayList<String> menuList = new ArrayList<>();
        menuList.add("MERHABALAR");
        menuList.addAll(getFoodTable());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menuList);
        lv.setAdapter(adapter);

    }

    private ArrayList<String> getFoodTable(){
        Calendar c = Calendar.getInstance();
        final int maxDayOfMonth = c.getMaximum(Calendar.DAY_OF_MONTH);
        final int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final ArrayList<String> foodTable = new ArrayList<>();

        for (int i = dayOfMonth; i<maxDayOfMonth+1; i++) {
            DatabaseReference databaseReference = database.getReference().child("food").child("refectory").child(""+i);
            readData(databaseReference, new OnGetDataListener() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    foodTable.add(dataSnapshot.getValue(String.class));
                }

                @Override
                public void onStart() {
                    //when starting
                    Log.d("ONSTART", "Started");
                }

                @Override
                public void onFailure() {
                    Log.d("onFailure", "Failed");
                }
            });
        }

        return foodTable;
    }

    public void readData(DatabaseReference ref, final OnGetDataListener listener) {
        listener.onStart();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onSuccess(dataSnapshot);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onFailure();
            }
        });
    }
    public interface OnGetDataListener {
        //this is for callbacks
        void onSuccess(DataSnapshot dataSnapshot);
        void onStart();
        void onFailure();
    }
}
