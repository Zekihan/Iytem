package com.wambly.iytem;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ContactsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = findViewById(R.id.toolbar);
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

        EditText etSearch = findViewById(R.id.editText);

        final RecyclerView recyclerView = findViewById(R.id.RC);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        List<Contact> contacts = getContacts();
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
                reader.putExtra("contact", contactsCustomAdapter.getmDisplayedValues().get(position));
                startActivity(reader);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
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
