package com.wambly.iytem;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;

import androidx.fragment.app.FragmentPagerAdapter;
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
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class BusActivity extends AppCompatActivity implements BusFragment.OnFragmentInteractionListener {

    private boolean direction = false;
    private BusService busService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!= null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        FragmentPagerAdapter mFragmentPagerAdapter = new MyAdapter(getSupportFragmentManager());

        ViewPager mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mFragmentPagerAdapter);
        final TabLayout tabLayout = findViewById(R.id.tabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        busService = getIntent().getParcelableExtra("busServices");
        if(busService != null){
            toolbar.setTitle(busService.getNameStr());
        }else{
            toolbar.setTitle(getString(R.string.bus));
        }

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        final TextView directionView = findViewById(R.id.directionTxt);
        directionView.setText(wayTitle(busService.getWay0()));

        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("direction", direction);
        editor.apply();

        View changeDir = findViewById(R.id.direction);
        changeDir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = prefs.edit();
                String directionText;
                if(direction){
                    directionText = wayTitle(busService.getWay0());
                }else{
                    directionText = wayTitle(busService.getWay1());
                }
                directionView.setText(directionText);
                direction = !direction;
                editor.putBoolean("direction", direction);
                editor.apply();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void onFragmentInteraction(Uri uri) { }

    class MyAdapter extends FragmentPagerAdapter {
        MyAdapter(FragmentManager fm) {
            super(fm);
        }
        @NonNull
        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            Week week;
            boolean today = false;
            switch (position){
                case 0:
                    Calendar c = Calendar.getInstance();
                    if(c.get(Calendar.DAY_OF_WEEK)== Calendar.SUNDAY){
                        week = Week.sunday;

                    }else if(c.get(Calendar.DAY_OF_WEEK)== Calendar.SATURDAY){
                        week = Week.saturday;

                    }else{
                        week = Week.weekday;
                    }
                    today = true;
                    break;
                case 1:
                    week = Week.weekday;
                    break;
                case 2:
                    week = Week.saturday;
                    break;
                case 3:
                    week = Week.sunday;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + position);
            }

            fragment = BusFragment.newInstance(getTimeTable(week, 0),
                    getTimeTable(week, 1),today);
            return fragment;
        }
        @Override
        public int getCount() {
            return 4;
        }
    }

    private ArrayList<String> getTimeTable(Week week, int direction){
        final ArrayList<String> timeTable = new ArrayList<>();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        String direct;
        if(direction == 0){
            direct = busService.getDirection0();
        }else{
            direct = busService.getDirection1();
        }
        final DatabaseReference ref = database.getReference().child("transportation").
                child(busService.getName()).child(week.toString()).child(direct);

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

    private String wayTitle(String way){
        return way + "  " + getString(R.string.departure_hours);
    }

    public enum Week{
        weekday,saturday,sunday
    }

}
