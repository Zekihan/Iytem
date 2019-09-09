package com.wambly.iytem;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import androidx.preference.PreferenceManager;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class BusActivity extends AppCompatActivity implements BusFragment.OnFragmentInteractionListener {

    private boolean direction = false;
    private TransportationType type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        type = (TransportationType) getIntent().getSerializableExtra("type");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(type.getTitleVal());
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

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        ViewPager mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("direction", direction);
        editor.apply();

        final TextView directionView = findViewById(R.id.directionTxt);
        directionView.setText(prettyDirection(type.getDirection0()));

        View changeDir = findViewById(R.id.direction);
        changeDir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(direction){
                    direction = false;
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("direction", direction);
                    editor.apply();
                    directionView.setText(prettyDirection(type.getDirection0()));
                }else{
                    direction = true;
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("direction", direction);
                    editor.apply();
                    directionView.setText(prettyDirection(type.getDirection1()));
                }
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position){
                case 0:
                    Calendar c = Calendar.getInstance();
                    if(c.get(Calendar.DAY_OF_WEEK)== Calendar.SUNDAY){
                        fragment = BusFragment.newInstance(getTimeTable(Week.sunday, 0),
                                getTimeTable(Week.sunday, 1),true);
                    }else if(c.get(Calendar.DAY_OF_WEEK)== Calendar.SATURDAY){
                        fragment = BusFragment.newInstance(getTimeTable(Week.saturday, 0),
                                getTimeTable(Week.saturday, 1),true);

                    }else{
                        fragment = BusFragment.newInstance(getTimeTable(Week.weekday, 0),
                                getTimeTable(Week.weekday, 1),true);
                    }
                    break;
                case 1:
                    fragment = BusFragment.newInstance(getTimeTable(Week.weekday, 0),
                            getTimeTable(Week.weekday, 1),false);
                    break;
                case 2:
                    fragment = BusFragment.newInstance(getTimeTable(Week.saturday, 0),
                            getTimeTable(Week.saturday, 1),false);
                    break;
                case 3:
                    fragment = BusFragment.newInstance(getTimeTable(Week.sunday, 0),
                            getTimeTable(Week.sunday, 1),false);
                    break;
                default:
                    fragment = BusFragment.newInstance(getTimeTable(Week.weekday, 0),
                            getTimeTable(Week.weekday, 1),false);
                    Log.e("F", "getItem: err");
                    break;
            }
            return fragment;
        }
        @Override
        public int getCount() {
            return 4;
        }
    }

    private String prettyDirection(String dir){
        String[] dirArr = dir.split("_");
        return dirArr[0].toUpperCase() + "  -->  " + dirArr[1].toUpperCase();
    }

    private ArrayList<String> getTimeTable(Week week, int direction){
        final ArrayList<String> timeTable = new ArrayList<>();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        String direct;
        if(direction == 0){
            direct = type.getDirection0();
        }else{
            direct = type.getDirection1();
        }

        final DatabaseReference ref = database.getReference().child("transportation").
                child(type.toString()).child(week.toString()).child(direct);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postSnap: snapshot.getChildren()){
                    timeTable.add(postSnap.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        return timeTable;
    }

    public enum Week{
        weekday,saturday,sunday
    }

}
