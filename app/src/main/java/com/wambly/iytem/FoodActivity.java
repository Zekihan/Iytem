package com.wambly.iytem;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.view.View;
import android.widget.TextView;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FoodActivity extends AppCompatActivity {

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

    }

}
