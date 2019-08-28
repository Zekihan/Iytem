package com.wambly.iytem;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;

public class BusActivity extends AppCompatActivity implements BlankFragment.OnFragmentInteractionListener {

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        JsonUpdater jsonUpdater = new JsonUpdater();
        jsonUpdater.updateTransportation(this);

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ViewPager mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("direction", direction);
        editor.apply();
        Button b = findViewById(R.id.direction);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(direction){
                    direction = false;
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("direction", direction);
                    editor.apply();
                }else{
                    direction = true;
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("direction", direction);
                    editor.apply();
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

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position){
                case 0:
                    Calendar c = Calendar.getInstance();
                    if(c.get(Calendar.DAY_OF_WEEK)== Calendar.SUNDAY){
                        fragment = BlankFragment.newInstance(getTimeTable(Week.sunday, 0)
                                , getTimeTable(Week.sunday, 1),true,type);
                    }else if(c.get(Calendar.DAY_OF_WEEK)== Calendar.SATURDAY){
                        fragment = BlankFragment.newInstance(getTimeTable(Week.saturday, 0)
                                ,getTimeTable(Week.saturday, 1),true,type);

                    }else{
                        fragment = BlankFragment.newInstance(getTimeTable(Week.weekday, 0)
                                ,getTimeTable(Week.weekday, 1),true,type);
                    }
                    break;
                case 1:
                    fragment = BlankFragment.newInstance(getTimeTable(Week.weekday, 0)
                            ,getTimeTable(Week.weekday, 1),false,type);
                    break;
                case 2:
                    fragment = BlankFragment.newInstance(getTimeTable(Week.saturday, 0)
                            ,getTimeTable(Week.saturday, 1),false,type);
                    break;
                case 3:
                    fragment = BlankFragment.newInstance(getTimeTable(Week.sunday, 0)
                            ,getTimeTable(Week.sunday, 1),false,type);
                    break;
            }
            return fragment;
        }
        @Override
        public int getCount() {
            return 4;
        }
    }

    private ArrayList<String> getTimeTable(Week week, int direction){
        final ArrayList<String> timeTable = new ArrayList<>();
        try{
            File file = new File(getFilesDir(),"transportation.json");
            InputStreamReader instream = new InputStreamReader(new FileInputStream(file));
            BufferedReader buffer = new BufferedReader(instream);

            StringBuilder content = new StringBuilder();
            String line;
            while ((line = buffer.readLine()) != null) {
                content.append(line);
            }
            buffer.close();
            Gson gson = new Gson();
            JsonObject reader = gson.fromJson(content.toString(), JsonObject.class);
            JsonObject bus  = reader.get(type.toString()).getAsJsonObject();
            JsonObject weekly  = bus.get(week.toString()).getAsJsonObject();
            JsonArray table;

            if(direction == 0){
                table = weekly.getAsJsonArray(type.getDirection1());
            }else{
                table = weekly.getAsJsonArray(type.getDirection0());
            }

            int i = 0;
            String item;
            while (i < table.size()){
                item = table.get(i).getAsString();
                timeTable.add(item);
                i++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return timeTable;
    }

    public enum Week{
        weekday,saturday,sunday
    }

}
