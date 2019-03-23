package com.wambly.iytem;


import android.content.Intent;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.food);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        intentBuilder = new CustomTabsIntent.Builder();
        intentBuilder.setStartAnimations(this,R.anim.slide_in_right , R.anim.slide_out_left);
        intentBuilder.setExitAnimations(this, android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);

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
