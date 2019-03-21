package com.wambly.iytem;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class FoodActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.food);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView tv2 = findViewById(R.id.textView2);

        Calendar c = Calendar.getInstance();
        long m = (c.get(Calendar.HOUR)*60*60*1000)
                +(c.get(Calendar.MINUTE)*60*1000)
                +(c.get(Calendar.SECOND)*1000)
                +(c.get(Calendar.MILLISECOND));
        long time = c.getTimeInMillis();
        final long day = time-m+30681000-43200000;
        final String d = String.valueOf(day);
        Log.e("Food",""+(1553189481-1553146281));
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = mDatabase.getReference().child("food").child("refectory").child(d.substring(0,10));

        getMenu(databaseReference,tv2);

    }
    public void getMenu(DatabaseReference databaseReference, final TextView tv){

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String menu = dataSnapshot.getValue(String.class);
                tv.setText(menu);
                //Log.e("Food",menu);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
