package com.wambly.iytem;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ShortCutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_short_cut);

        TextView cms = findViewById(R.id.cms);
        cms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String theurl = "https://cms.iyte.edu.tr/";
                Uri urlstr = Uri.parse(theurl);
                Intent urlintent = new Intent();
                urlintent.setData(urlstr);
                urlintent.setAction(Intent.ACTION_VIEW);
                startActivity(urlintent);
            }
        });
        TextView library = findViewById(R.id.library);
        library.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String theurl = "http://library.iyte.edu.tr/";
                Uri urlstr = Uri.parse(theurl);
                Intent urlintent = new Intent();
                urlintent.setData(urlstr);
                urlintent.setAction(Intent.ACTION_VIEW);
                startActivity(urlintent);
            }
        });
        TextView obs = findViewById(R.id.obs);
        obs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String theurl = "https://obs.iyte.edu.tr/oibs/ogrenci/login.aspx";
                Uri urlstr = Uri.parse(theurl);
                Intent urlintent = new Intent();
                urlintent.setData(urlstr);
                urlintent.setAction(Intent.ACTION_VIEW);
                startActivity(urlintent);
            }
        });
        TextView iyte = findViewById(R.id.iyte);
        iyte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String theurl = "http://iyte.edu.tr/";
                Uri urlstr = Uri.parse(theurl);
                Intent urlintent = new Intent();
                urlintent.setData(urlstr);
                urlintent.setAction(Intent.ACTION_VIEW);
                startActivity(urlintent);
            }
        });

    }
}
