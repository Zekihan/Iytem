package com.wambly.iytem;

import android.content.pm.ActivityInfo;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MonthlyMenuActivity extends AppCompatActivity {

    private final List<String> menuList = new ArrayList<>();

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
        jsonUpdater.updateMonthlyMenu(this);

        Calendar calendar = Calendar.getInstance();

        try{
            File file = new File(getFilesDir(),"food.json");
            InputStreamReader instream = new InputStreamReader(new FileInputStream(file));
            BufferedReader buffer = new BufferedReader(instream);

            String content = "";
            StringBuilder stringBuilder = new StringBuilder(content);
            String line;
            while ((line = buffer.readLine()) != null) {
                stringBuilder.append(line);
            }
            buffer.close();
            content = stringBuilder.toString();
            Gson gson = new Gson();
            JsonObject reader = gson.fromJson(content, JsonObject.class);
            JsonArray monthly  = reader.getAsJsonArray("refectory");
            for (int i = 1; i <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                String menu = monthly.get(i).getAsString();
                if(menu.equals("No Menu")){
                    menuList.add(getString(R.string.no_menu));
                }else{
                    menuList.add(menu);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        RecyclerView recyclerView = findViewById(R.id.menulist);
        MonthlyMenuCustomAdapter adapter = new MonthlyMenuCustomAdapter(menuList);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, 0));
        recyclerView.smoothScrollToPosition(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
