package com.wambly.iytem;

import android.content.Context;
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

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;


import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


import java.util.List;

public class ContactsActivity extends AppCompatActivity {

    private ContactsCustomAdapter adapter;
    private RecyclerView recyclerView;
    private List<Contact> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.contacts);
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


        final AppBarLayout appBarLayout = findViewById(R.id.appBarLay);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                    toolbar.setElevation(0);
                }else{
                    toolbar.setElevation(getResources().getDimension(R.dimen.toolbarElevation));
                }
            }
        });

        contacts = new ArrayList<>();
        syncContacts();
        adapter = new ContactsCustomAdapter(contacts);

        recyclerView = findViewById(R.id.RC);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, 0));
        recyclerView.setAdapter(adapter);

        final AutoCompleteTextView etSearch = findViewById(R.id.editText);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.departments_array));
        etSearch.setAdapter(arrayAdapter);

        etSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.getFilter().filter(adapterView.getItemAtPosition(i).toString());
                recyclerView.scrollToPosition(0);
                appBarLayout.setExpanded(false);
                hideKeyboard();
            }
        });

        etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    appBarLayout.setExpanded(false);

                }
            }
        });
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    adapter.getFilter().filter(v.getText().toString());
                    recyclerView.scrollToPosition(0);
                    hideKeyboard();
                    appBarLayout.setExpanded(false);
                    return true;
                }
                return false;
            }
        });

        // CLEAR BUTTON
        final View clearButton = findViewById(R.id.clear_text);
        clearButton.setVisibility(View.INVISIBLE);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etSearch.setText("");
                showKeyboard(etSearch);
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().equals("")){
                    clearButton.setVisibility(View.INVISIBLE);
                    adapter.getFilter().filter("");
                    recyclerView.scrollToPosition(0);
                }else{
                    clearButton.setVisibility(View.VISIBLE);
                }
            }
        });

        RecyclerTouchListener listener = new RecyclerTouchListener(getApplicationContext(),
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent reader = new Intent(getApplicationContext(), ContactsDetailsActivity.class);
                reader.putExtra("contact", adapter.getmDisplayedValues().get(position));
                startActivity(reader);
            }
            @Override
            public void onLongClick(View view, int position) { }
        });

        recyclerView.addOnItemTouchListener(listener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void syncContacts(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference().child("contacts").child("contactsList");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i = 0;
                for (DataSnapshot child : snapshot.getChildren()){
                    contacts.add(child.getValue(Contact.class));
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

    private void showKeyboard(View view){
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    }

    private void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

}
