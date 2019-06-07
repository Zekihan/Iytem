package com.wambly.iytem;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
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

public class MonthlyMenuActivity extends AppCompatActivity {

    final List<String> menuList = new ArrayList<>();
    MonthlyMenuCustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_menu);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.monthly_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        try {
            Calendar c = Calendar.getInstance();
            Scanner scan = new Scanner(new File(getFilesDir(),"monthlyMenu.json"));
            scan.useDelimiter("\\Z");
            String content = scan.next();
            JSONObject reader = new JSONObject(content);
            JSONArray monthly  = reader.getJSONArray("refectory");
            for (int i = 1; i < c.getMaximum(Calendar.DAY_OF_MONTH); i++) {
                String menu = monthly.getString(i);
                menuList.add(menu);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RecyclerView recyclerView = findViewById(R.id.menulist);
        adapter = new MonthlyMenuCustomAdapter(menuList,getApplicationContext());
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.smoothScrollToPosition(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FoodActivity.class));
            }
        });

    }

}
