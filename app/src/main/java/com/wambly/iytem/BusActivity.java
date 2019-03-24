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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

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
                    Calendar c = Calendar.getInstance();
                    if(c.get(Calendar.DAY_OF_WEEK)==1){
                        fragment = BlankFragment.newInstance(getTimeTable(Week.sunday, Direction.iyte_izmir),getTimeTable(Week.sunday, Direction.izmir_iyte),true);
                    }else if(c.get(Calendar.DAY_OF_WEEK)==7){
                        fragment = BlankFragment.newInstance(getTimeTable(Week.saturday, Direction.iyte_izmir),getTimeTable(Week.saturday, Direction.izmir_iyte),true);
                    }else{
                        fragment = BlankFragment.newInstance(getTimeTable(Week.weekday, Direction.iyte_izmir),getTimeTable(Week.weekday, Direction.izmir_iyte),true);
                    }
                    break;
                case 1:
                    fragment = BlankFragment.newInstance(getTimeTable(Week.weekday, Direction.iyte_izmir),getTimeTable(Week.weekday, Direction.izmir_iyte),false);
                    break;
                case 2:
                    fragment = BlankFragment.newInstance(getTimeTable(Week.saturday, Direction.iyte_izmir),getTimeTable(Week.saturday, Direction.izmir_iyte),false);
                    break;
                case 3:
                    fragment = BlankFragment.newInstance(getTimeTable(Week.sunday, Direction.iyte_izmir),getTimeTable(Week.sunday, Direction.izmir_iyte),false);
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
        final ArrayList<String> timeTable = new ArrayList<>();
        try {
            Scanner scan = new Scanner(new File(getFilesDir(),"transportation.json"));
            scan.useDelimiter("\\Z");
            String content = scan.next();
            JSONObject reader = new JSONObject(content);
            JSONObject weekly  = reader.getJSONObject(week.toString());
            JSONArray table = weekly.getJSONArray(direction.toString());
            int i = 0;
            String item = table.getString(i);
            while (item != null){
                item = table.getString(i);
                timeTable.add(item);
                i++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return timeTable;
    }

    public enum Week{
        weekday,saturday,sunday
    }
    public enum Direction{
        iyte_izmir,izmir_iyte
    }

}
