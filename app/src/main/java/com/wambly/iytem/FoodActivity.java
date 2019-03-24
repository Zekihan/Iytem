package com.wambly.iytem;


import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.customtabs.CustomTabsCallback;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.view.View;
import android.widget.TextView;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FoodActivity extends AppCompatActivity {
    private CustomTabsIntent.Builder intentBuilder;

    private Toolbar toolbar;
    private CustomTabsSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.food);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        CustomTabsServiceConnection connection = new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName componentName, CustomTabsClient customTabsClient) {
                customTabsClient.warmup(1);
                CustomTabsCallback customTabsCallback = new CustomTabsCallback();
                session = customTabsClient.newSession(customTabsCallback);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        CustomTabsClient.bindCustomTabsService(this,"custom.tabs", connection);

        intentBuilder = new CustomTabsIntent.Builder();
        intentBuilder.setStartAnimations(this,R.anim.slide_in_right , R.anim.slide_out_left);
        intentBuilder.setExitAnimations(this, android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
        intentBuilder.setToolbarColor(Color.parseColor("#3949AB"));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView tv = findViewById(R.id.menu);

        try {
            Scanner scan = new Scanner(new File(getFilesDir(),"menu.txt"));
            scan.useDelimiter("\\Z");
            String content = scan.next();
            tv.setText(content);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        View food = findViewById(R.id.addMoney);
        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chromeTab("https://yks.iyte.edu.tr/Login.aspx");
            }
        });

        View monthly = findViewById(R.id.monthlyMenu);
        monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MonthlyMenuActivity.class));
            }
        });

    }
    private void chromeTab(String url){
        CustomTabsIntent customTabsIntent = intentBuilder.build();
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }

}
