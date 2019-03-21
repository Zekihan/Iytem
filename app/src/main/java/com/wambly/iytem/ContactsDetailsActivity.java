package com.wambly.iytem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ContactsDetailsActivity extends AppCompatActivity {

    Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_details);

        contact = getIntent().getParcelableExtra("contact");

        TextView name = findViewById(R.id.name);
        TextView email = findViewById(R.id.email);
        TextView phone = findViewById(R.id.phone);
        name.setText(contact.getName());
        email.setText(contact.getEmail());
        phone.setText(contact.getPhone());

    }
}
