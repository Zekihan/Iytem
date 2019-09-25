package com.wambly.iytem;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MonthlyMenuActivity extends AppCompatActivity {

    private final List<String> menuList = new ArrayList<>();
    private MonthlyMenuCustomAdapter adapter;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_menu);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.monthly_menu);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!= null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        syncMenuList();

        RecyclerView recyclerView = findViewById(R.id.menulist);
        adapter = new MonthlyMenuCustomAdapter(menuList);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, 0));
        int itemPosition = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        recyclerView.smoothScrollToPosition(itemPosition);

        RecyclerTouchListener listener = new RecyclerTouchListener(getApplicationContext(),
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) { }
            @Override
            public void onLongClick(View view, int position) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.contact));
                String shareMessage = getDate(position) + "\n" + getString(R.string.food) + "\n\n";
                shareMessage += menuList.get(position);
                shareMessage += "\n\n" + getString(R.string.download_iytem) + ": bit.ly/2lTdDpn";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, getString(R.string.share)));
            }
        });

        recyclerView.addOnItemTouchListener(listener);


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getBoolean("monthlyMenuTut", true)){
            LinearLayout rootLayout = findViewById(R.id.root_layout);
            Snackbar snackbar = Snackbar.make(rootLayout, getString(R.string.monthly_menu_tut), 3500);
            snackbar.show();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("monthlyMenuTut", false);
            editor.apply();
        }

    }

    private String getDate(int day){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("d MMMM EEEE");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH,day);
        return format.format(c.getTime());
    }


    private void syncMenuList(){
        final Calendar c = Calendar.getInstance();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference().child("food").child("refectory");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (int i = 1; i <= c.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                    String menu = dataSnapshot.child(Integer.toString(i)).getValue(String.class);
                    if(menu == null){
                        Log.e("DBE", "onDataChange: DB error");
                    }else if(menu.equals("No Menu")){
                        menuList.add(getString(R.string.no_menu));
                    }else{
                        menuList.add(menu);
                    }
                    adapter.notifyItemInserted(i);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

}