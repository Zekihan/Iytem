package com.wambly.iytem;

import android.content.pm.ActivityInfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.WindowManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ShortcutActivity extends AppCompatActivity {

    private ArrayList<Shortcut> shortcuts;
    private ShortcutsCustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shortcut);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.shortcuts);
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

        shortcuts = new ArrayList<>();

        shortcuts.add(new Shortcut(getString(R.string.obs),"https://obs.iyte.edu.tr",R.drawable.ic_school));
        shortcuts.add(new Shortcut(getString(R.string.iztech),"https://iyte.edu.tr",R.drawable.ic_home));
        shortcuts.add(new Shortcut(getString(R.string.library),"http://library.iyte.edu.tr",R.drawable.ic_library));
        shortcuts.add(new Shortcut(getString(R.string.cms),"https://cms.iyte.edu.tr",R.drawable.ic_cms));
        shortcuts.add(new Shortcut(getString(R.string.ydyo),"http://ydyo.iyte.edu.tr",R.drawable.ic_language));
        shortcuts.add(new Shortcut(getString(R.string.webmail),"https://webmail.iyte.edu.tr",R.drawable.ic_email));
        shortcuts.add(new Shortcut(getString(R.string.academic_calendar),"https://iyte.edu.tr/akademik/akademik-takvim",R.drawable.ic_calendar_today));
        shortcuts.add(new Shortcut(getString(R.string.gk_dep),"https://gk.iyte.edu.tr",R.drawable.ic_language));
        addExtraShorcuts();

        RecyclerView recyclerView = findViewById(R.id.shortcuts);
        adapter = new ShortcutsCustomAdapter(shortcuts);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, 0));

        RecyclerTouchListener listener = new RecyclerTouchListener(getApplicationContext(),
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                CustomTabsHelper.chromeTab(ShortcutActivity.this, shortcuts.get(position).getUrl());
            }
            @Override
            public void onLongClick(View view, int position) {

            }
        });
        recyclerView.addOnItemTouchListener(listener);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void addExtraShorcuts(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference().child("shortcuts");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                int i = shortcuts.size();
                for(DataSnapshot childSnap : ds.getChildren()){
                    shortcuts.add(new Shortcut(childSnap.child("name").getValue(String.class), childSnap.child("url")
                            .getValue(String.class), R.drawable.ic_language));
                    adapter.notifyItemInserted(i);
                    i++;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

}
