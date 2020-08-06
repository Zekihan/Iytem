package com.wambly.iytem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LibraryHoursActivity extends AppCompatActivity {

    private String dayOut;
    private String endOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_hours);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.library_hours);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!= null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child("operatingHours").child("libraryHours");
        ref.keepSynced(true);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                DataSnapshot dayds = ds.child("weekday");
                dayOut = dayds.child("start").getValue(String.class) + " - "
                        + dayds.child("end").getValue(String.class);
                DataSnapshot endds = ds.child("weekend");
                endOut = endds.child("start").getValue(String.class) + " - "
                        + endds.child("end").getValue(String.class);

                TextView weekday = findViewById(R.id.weekday_hours);
                TextView weekend = findViewById(R.id.weekend_hours);

                weekday.setText(dayOut);
                weekend.setText(endOut);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        View shareButton = findViewById(R.id.share);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share();
            }
        });

    }

    private void share(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.contact));
        String shareMessage= getString(R.string.library_hours) + "\n"
                + getString(R.string.week_day) + ": " + dayOut + "\n"
                + getString(R.string.week_end) + ": " + endOut
                + "\n\n" + getString(R.string.download_iytem) + ": bit.ly/2lTdDpn";
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share)));
    }
}
