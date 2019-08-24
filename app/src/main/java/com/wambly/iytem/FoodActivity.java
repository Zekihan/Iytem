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

import android.util.Log;
import android.view.View;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Scanner;

public class FoodActivity extends AppCompatActivity {
    private CustomTabsIntent.Builder intentBuilder;
    private CustomTabsServiceConnection tabsConnection;
    public CustomTabsSession tabsSession;

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
        intentBuilder.setExitAnimations(this, android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
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
        String menuOut = "";
        for (String m : menuList) {
            if (m.contains("(")) {
                menuOut += m.substring(0, m.indexOf("(")) + "\n";
            } else {
                menuOut += m;
            }
        }
        if(menuOut.equals("No Menu")){
            tv.setText(R.string.menu_yok);
        }else{
            tv.setText(menuOut);
        }
    }

    private void showMenu() {
        try {
            Calendar c = Calendar.getInstance();
            int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
            Scanner scan = new Scanner(new File(getFilesDir(),"food.json"));
            scan.useDelimiter("\\Z");
            String content = scan.next();
            JSONObject reader = new JSONObject(content);
            JSONArray monthly  = reader.getJSONArray("refectory");
            String menu = monthly.getString(dayOfMonth);

            setMenuText(menu);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JSONException e) {
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
