package com.wambly.iytem;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

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

        JsonUpdater jsonUpdater = new JsonUpdater();
        jsonUpdater.updateContacts(this);
        jsonUpdater.updateTransportation(this);
        jsonUpdater.updateMonthlyMenu(this);

        try {
            Calendar c = Calendar.getInstance();
            Scanner scan = new Scanner(new File(getFilesDir(),"food.json"));
            scan.useDelimiter("\\Z");
            String content = scan.next();
            JSONObject reader = new JSONObject(content);
            JSONArray monthly  = reader.getJSONArray("refectory");
            for (int i = 1; i < c.getMaximum(Calendar.DAY_OF_MONTH); i++) {
                String menu = monthly.getString(i);
                if(menu.equals("No Menu\n")){
                    menuList.add(getString(R.string.menu_yok));
                }else{
                    menuList.add(menu);
                }

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
                finish();
            }
        });

    }

}
