package com.wambly.iytem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BusActivity extends AppCompatActivity implements BlankFragment.OnFragmentInteractionListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    public boolean[] direction = {false};//iyte-izmir

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.bus);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("direction", direction[0]);
        editor.apply();
        Button b = findViewById(R.id.direction);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> list = new ArrayList<String>();
                if(direction[0]){
                    direction[0] = false;
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("direction", direction[0]);
                    editor.apply();
                }else{
                    direction[0] = true;
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("direction", direction[0]);
                    editor.apply();
                }
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position){
                case 0:
                    fragment = BlankFragment.newInstance(getTimeTable(Week.weekday, Direction.iyte_izmir),getTimeTable(Week.weekday, Direction.izmir_iyte),direction);
                    break;
                case 1:
                    fragment = BlankFragment.newInstance(getTimeTable(Week.weekday, Direction.iyte_izmir),getTimeTable(Week.weekday, Direction.izmir_iyte),direction);
                    break;
                case 2:
                    fragment = BlankFragment.newInstance(getTimeTable(Week.saturday, Direction.iyte_izmir),getTimeTable(Week.saturday, Direction.izmir_iyte),direction);
                    break;
                case 3:
                    fragment = BlankFragment.newInstance(getTimeTable(Week.sunday, Direction.iyte_izmir),getTimeTable(Week.sunday, Direction.izmir_iyte),direction);
                    break;
            }
            return fragment;
        }
        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }
    }

    private ArrayList<String> getTimeTable( BusActivity.Week week, BusActivity.Direction direction){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final ArrayList<String> timeTable = new ArrayList<>();
        DatabaseReference databaseReference = database.getReference().child("transportation").child("eshot").child(week.toString()).child(direction.toString());
        readData(databaseReference, new BusActivity.OnGetDataListener() {
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

    public void readData(DatabaseReference ref, final BusActivity.OnGetDataListener listener) {
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

    public enum Week{
        weekday,saturday,sunday
    }
    public enum Direction{
        iyte_izmir,izmir_iyte
    }
    public interface OnGetDataListener {
        //this is for callbacks
        void onSuccess(DataSnapshot dataSnapshot);
        void onStart();
        void onFailure();
    }

}
