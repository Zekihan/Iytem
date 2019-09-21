package com.wambly.iytem;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
        if(getSupportActionBar()!= null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        contact = getIntent().getParcelableExtra("contact");

        TextView name = findViewById(R.id.name);
        TextView email = findViewById(R.id.emailTxt);
        TextView phone = findViewById(R.id.phoneTxt);
        TextView department = findViewById(R.id.departmentTxt);
        TextView title = findViewById(R.id.titleTxt);
        name.setText(contact.getName());
        email.setText(contact.getEmail());
        phone.setText(prettyPhone(contact.getPhone()));
        department.setText(contact.getDepartment());
        title.setText(contact.getTitle());




        View emailView = findViewById(R.id.send_email);
        emailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailStr = contact.getEmail();
                if(emailStr.contains("@")){
                    sendEmail(contact.getEmail());
                }else{
                    Toast.makeText(getApplicationContext(), getString(R.string.not_available),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        View phoneView = findViewById(R.id.call);
        phoneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneStr = contact.getPhone();
                if(phoneStr.replaceAll("\\D", "").length() >= 7){
                    if(!validateDial(phoneStr).equals("")){
                        dialNum(validateDial(phoneStr));
                    }
                }else{
                    Toast.makeText(getApplicationContext(), getString(R.string.not_available),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        View shareButton = findViewById(R.id.share);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareContact();
            }
        });
    }

    private void shareContact(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.contact));
        String shareMessage= contact.getName() ;
        if(contact.getEmail().contains("@")){
            shareMessage += "\n" + contact.getEmail();
        }
        if(contact.getPhone().replaceAll("\\D", "").length() >= 7){
            shareMessage += "\n" + validateDial(contact.getPhone());
        }
        shareMessage += "\n\n" + getString(R.string.download_iytem) + ": bit.ly/2lTdDpn";
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_contact)));
    }

    private String prettyPhone(String phone){
        if(phone.contains(":")){
            String[] arr = phone.split(":");
            return arr[1] + "  (" + arr[0] + ")";
        }
        return phone;
    }



    private String validateDial(String phoneStr){
        phoneStr = phoneStr.replace("(" , " ").replace(")" , " ");
        if(phoneStr.replaceAll("\\D", "").length() >= 7) {
            phoneStr = phoneStr.split(":")[1];
            if ((!phoneStr.contains("232")) && (phoneStr.charAt(0) != '5') &&
                    (!((phoneStr.charAt(0) == '0') && (phoneStr.charAt(1) == '5')))) {
                return "0232 " + phoneStr;
            }else if((phoneStr.charAt(0) != '0')){
                return "0" + phoneStr;
            }else{
                return phoneStr;
            }
        }else{
            return "";
        }
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
