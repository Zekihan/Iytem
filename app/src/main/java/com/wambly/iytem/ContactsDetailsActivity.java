package com.wambly.iytem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class ContactsDetailsActivity extends AppCompatActivity {

    private Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_details);

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

        contact = getIntent().getParcelableExtra("contact");

        TextView name = findViewById(R.id.name);
        TextView email = findViewById(R.id.email);
        TextView phone = findViewById(R.id.phone);
        TextView department = findViewById(R.id.department);
        TextView title = findViewById(R.id.title);
        name.setText(contact.getName());
        email.setText(String.format("%s: \n%s", getString(R.string.email), contact.getEmail()));
        phone.setText(String.format("%s: \n%s", getString(R.string.phone), contact.getPhone()));
        title.setText(String.format("%s: \n%s", getString(R.string.title), contact.getTitle()));
        department.setText(String.format("%s: \n%s", getString(R.string.department), contact.getDepartment()));

    }
}
