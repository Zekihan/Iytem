package com.wambly.iytem;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;


public class FoodActivity extends AppCompatActivity {

    private ListView listView;
    ArrayAdapter<String> adapter;
    ArrayList<String> menu;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.food);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!= null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listView =  findViewById(R.id.menu);

        menu = new ArrayList<>();
        menu.add(" ");
        menu.add(" ");
        menu.add(" ");
        menu.add(" ");

        adapter = new ArrayAdapter<>(this,
                R.layout.centered_row_item, R.id.text1,menu);
        listView.setAdapter(adapter);

        showMenu();

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

        View shareButton = findViewById(R.id.share);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.contact));
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(getString(R.string.food));
                stringBuilder.append(" ");
                stringBuilder.append(getString(R.string.menuTitle));
                stringBuilder.append("\n\n");
                for(int i=0; i < adapter.getCount(); i++){
                    stringBuilder.append(adapter.getItem(i));
                    stringBuilder.append("\n");
                }
                String shareMessage = stringBuilder.toString();
                shareMessage += "\n" + getString(R.string.download_iytem) + ": bit.ly/2lTdDpn";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, getString(R.string.share)));
            }
        });
    }

    private String prettyMenu(String menuStr){
        String[] menuList = menuStr.split("\n");
        StringBuilder menuOut = new StringBuilder();
        for (String m : menuList) {
            if (m.contains("(")) {
                menuOut.append(m.substring(0, m.indexOf("("))).append("\n");
            }else {
                menuOut.append(m);
            }
        }
        String menu = menuOut.toString();
        if(menu.equals("No Menu")){
            menu = " " + "\n" + getString(R.string.no_menu) + "\n" + " " + "\n" + " ";
        }
        return menu;
    }

    private void showMenu() {
        Calendar c = Calendar.getInstance();
        final String dayOfMonth = Integer.toString(c.get(Calendar.DAY_OF_MONTH));
        System.out.println(dayOfMonth);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference().child("food").child("refectory").child(dayOfMonth);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                String menuStr = prettyMenu(ds.getValue(String.class));
                menu.clear();
                menu.addAll(Arrays.asList(menuStr.split("(\n)")));
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    @SuppressWarnings("SameParameterValue")
    private void chromeTab(String url){
        CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
        intentBuilder.setStartAnimations(this,R.anim.slide_in_right , R.anim.slide_out_left);
        intentBuilder.setExitAnimations(this, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        intentBuilder.setToolbarColor(getResources().getColor(R.color.bgColor));
        CustomTabsIntent customTabsIntent = intentBuilder.build();
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }

}
