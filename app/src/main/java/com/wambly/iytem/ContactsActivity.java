package com.wambly.iytem;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ContactsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText etSearch;
    private List<Contact> contacts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.contacts);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        etSearch = findViewById(R.id.editText);

        final RecyclerView recyclerView = findViewById(R.id.RC);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        contacts = getContacts();
        final ContactsCustomAdapter contactsCustomAdapter = new ContactsCustomAdapter(contacts);
        contactsCustomAdapter.setContacts(contacts);
        recyclerView.setAdapter(contactsCustomAdapter);
        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Call back the Adapter with current character to Filter
                contactsCustomAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent reader = new Intent(getApplicationContext(), ContactsDetailsActivity.class);
                reader.putExtra("contact", contacts.get(position));
                startActivity(reader);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        /*
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("contacts");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<Contact>> t = new GenericTypeIndicator<List<Contact>>() {};
                final List<Contact> contacts = dataSnapshot.getValue(t);
                final ContactsCustomAdapter contactsCustomAdapter = new ContactsCustomAdapter(contacts);
                contactsCustomAdapter.setContacts(contacts);
                recyclerView.setAdapter(contactsCustomAdapter);
                etSearch.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        // Call back the Adapter with current character to Filter
                        contactsCustomAdapter.getFilter().filter(s.toString());
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count,int after) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
                recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Intent reader = new Intent(getApplicationContext(), ContactsDetailsActivity.class);
                        reader.putExtra("contact", contacts.get(position));
                        startActivity(reader);
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

*/
    }
    private ArrayList<Contact> getContacts(){
        final ArrayList<Contact> contacts = new ArrayList<>();
        try {
            Scanner scan = new Scanner(new File(getFilesDir(),"contacts.json"));
            scan.useDelimiter("\\Z");
            String content = scan.next();
            JSONObject reader = new JSONObject(content);
            JSONArray contactList  = reader.getJSONArray("contactsList");
            int i = 0;
            JSONObject item = contactList.getJSONObject(i);
            while (item != null){
                item = contactList.getJSONObject(i);
                contacts.add(new Contact(item.getString("name"),item.getString("email"), item.getString("phone") , item.getString("department") , item.getString("title") ));
                i++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return contacts;
    }
}
