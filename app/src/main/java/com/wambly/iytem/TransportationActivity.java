package com.wambly.iytem;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TransportationActivity extends AppCompatActivity {

    private Toolbar toolbar;

    public enum Week{
        weekday,saturday,sunday
    }
    public enum Direction{
        iyte_izmir,izmir_iyte
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transportation);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.transportation);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        final List<String> timeTable = getWeekEndIyte2Izmir(mDatabase,Week.weekday,Direction.iyte_izmir);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("Trans",timeTable.toString());
            }
        },5000);

    }
    private List<String> getWeekEndIyte2Izmir(FirebaseDatabase database,Week week, Direction direction){
        final ArrayList<String> timeTable = new ArrayList<>();
        DatabaseReference databaseReference = database.getReference().child("transportation").child("eshot").child(week.toString()).child(direction.toString());
        readData(databaseReference, new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                for (final DataSnapshot ds : dataSnapshot.getChildren()) {
                    timeTable.add(ds.getValue(String.class));
                }
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
        return timeTable;
    }
    public interface OnGetDataListener {
        //this is for callbacks
        void onSuccess(DataSnapshot dataSnapshot);
        void onStart();
        void onFailure();
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

}
