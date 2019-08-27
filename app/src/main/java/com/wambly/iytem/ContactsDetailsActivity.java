package com.wambly.iytem;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class ContactsDetailsActivity extends AppCompatActivity {

    private Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_details);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.contact);
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
        final TextView phone = findViewById(R.id.phone);
        TextView department = findViewById(R.id.department);
        TextView title = findViewById(R.id.title);
        name.setText(contact.getName());
        email.setText(String.format("%s: \n%s", getString(R.string.email), contact.getEmail()));
        phone.setText(String.format("%s: \n%s", getString(R.string.phone), contact.getPhone()));
        title.setText(String.format("%s: \n%s", getString(R.string.title), contact.getTitle()));
        department.setText(String.format("%s: \n%s", getString(R.string.department), contact.getDepartment()));

        View emailView = findViewById(R.id.send_email);
        emailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailStr = contact.getEmail();
                if(emailStr.contains("@")){
                    sendEmail(contact.getEmail());
                }
            }
        });

        View phoneView = findViewById(R.id.call);
        phoneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneStr = contact.getPhone();
                phoneStr.replace("(" , " ");
                phoneStr.replace(")" , " ");
                if(phoneStr.replaceAll("\\D", "").length() >= 7) {
                    phoneStr = phoneStr.split(":")[1];
                    if ((!phoneStr.contains("232")) && (phoneStr.charAt(0) != '5') &&
                    (((phoneStr.charAt(0) != '0') && (phoneStr.charAt(1) != '5')))) {
                        dialNum("0232" + phoneStr);
                    }else if((phoneStr.charAt(0) != '0')){
                        dialNum("0" + phoneStr);
                    }
                    else{
                        dialNum(phoneStr);
                    }
                }
            }
        });

    }

    private void sendEmail(String adress) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.parse("mailto:"+ adress);
        intent.setData(data);
        startActivity(intent);
    }

    private void dialNum(String num){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        String uri = "tel:" + num ;
        intent.setData(Uri.parse(uri));
        startActivity(intent);

    }

}
