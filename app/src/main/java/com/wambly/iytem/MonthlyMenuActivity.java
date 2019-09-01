package com.wambly.iytem;

import android.content.Intent;
import android.content.pm.ActivityInfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MonthlyMenuActivity extends AppCompatActivity {

    final List<String> menuList = new ArrayList<>();
    MonthlyMenuCustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dbCheck();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_menu);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.monthly_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        RecyclerView recyclerView = findViewById(R.id.menulist);
        adapter = new MonthlyMenuCustomAdapter(menuList);
        recyclerView.setAdapter(adapter);


        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, 0));
        recyclerView.smoothScrollToPosition(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

    }
    private void dbCheck(){
        final Calendar c = Calendar.getInstance();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference().child("food").child("refectory");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (int i = 1; i <= c.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                    String menu = dataSnapshot.child(Integer.toString(i)).getValue(String.class);
                    if(menu.equals("No Menu")){
                        menuList.add(getString(R.string.no_menu));
                    }else{
                        menuList.add(menu);
                    }

                    adapter.notifyItemInserted(i);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }




}