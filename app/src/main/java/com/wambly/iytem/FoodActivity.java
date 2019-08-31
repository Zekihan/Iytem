package com.wambly.iytem;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import androidx.browser.customtabs.CustomTabsCallback;
import androidx.browser.customtabs.CustomTabsClient;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.browser.customtabs.CustomTabsServiceConnection;
import androidx.browser.customtabs.CustomTabsSession;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;

import java.io.InputStreamReader;
import java.util.Calendar;


public class FoodActivity extends AppCompatActivity {
    private CustomTabsIntent.Builder intentBuilder;
    private CustomTabsServiceConnection tabsConnection;
    private CustomTabsSession tabsSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.food);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        JsonUpdater jsonUpdater = new JsonUpdater();
        jsonUpdater.updateMonthlyMenu(this);

        showMenu();

        tabsConnection = new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName componentName, CustomTabsClient customTabsClient) {
                customTabsClient.warmup(1);
                CustomTabsCallback customTabsCallback = new CustomTabsCallback();
                tabsSession = customTabsClient.newSession(customTabsCallback);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        };

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        CustomTabsClient.bindCustomTabsService(this,"custom.tabs", tabsConnection);
        intentBuilder = new CustomTabsIntent.Builder();
        intentBuilder.setStartAnimations(this,R.anim.slide_in_right , R.anim.slide_out_left);
        intentBuilder.setExitAnimations(this, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        intentBuilder.setToolbarColor(getResources().getColor(R.color.bgColor));

        View food = findViewById(R.id.addMoney);
        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chromeTab();
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

    private void setMenuText(String str){
        TextView tv = findViewById(R.id.menu);
        String[] menuList = str.split("\n");
        StringBuilder menuOut = new StringBuilder();
        for (String m : menuList) {
            if (m.contains("(")) {
                menuOut.append(m.substring(0, m.indexOf("("))).append("\n");
            } else {
                menuOut.append(m);
            }
        }
        if(menuOut.toString().equals("No Menu")){
            tv.setText(R.string.no_menu);
        }else{
            tv.setText(menuOut.toString());
        }
    }

    private void showMenu() {
        Calendar calendar = Calendar.getInstance();
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        try{
            File file = new File(getFilesDir(),"food.json");
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
            JsonArray monthly  = reader.getAsJsonArray("refectory");
            String menu = monthly.get(dayOfMonth).getAsString();

            setMenuText(menu);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void chromeTab(){
        CustomTabsIntent customTabsIntent = intentBuilder.build();
        customTabsIntent.launchUrl(this, Uri.parse("https://yks.iyte.edu.tr/Login.aspx"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(tabsConnection);
    }
}
