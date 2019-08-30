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
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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

        JsonUpdater jsonUpdater = new JsonUpdater();
        jsonUpdater.updateContacts(this);

        EditText etSearch = findViewById(R.id.editText);

        final RecyclerView recyclerView = findViewById(R.id.RC);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, 0));

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

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                recyclerView, new RecyclerTouchListener.ClickListener() {
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
        try{
            File file = new File(getFilesDir(),"contacts.json");
            InputStreamReader instream = new InputStreamReader(new FileInputStream(file));
            BufferedReader buffer = new BufferedReader(instream);

            StringBuilder content = new StringBuilder();
            String line;
            while ((line = buffer.readLine()) != null) {
                content.append(line);
            }
            buffer.close();

            Gson gson = new Gson();
            JsonObject reader = gson.fromJson(content.toString(), JsonObject.class);
            JsonArray contactList  = reader.getAsJsonArray("contactsList");
            int i = 0;
            JsonObject item;
            while (i < contactList.size()){
                item = contactList.get(i).getAsJsonObject();
                contacts.add(new Contact(item.get("name").getAsString(), item.get("email").getAsString(),
                        item.get("phone").getAsString(), item.get("department").getAsString() ,
                        item.get("title").getAsString() ));
                i++;
            }
            Log.d("Contacts num", "getContacts: " + contacts.size());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return contacts;
    }
}
