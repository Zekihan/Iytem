package com.wambly.iytem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AnnouncementsDetailsActivity extends AppCompatActivity {

    Announcement announcement;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcements_details);

        announcement = getIntent().getParcelableExtra("announcement");

        TextView tv = findViewById(R.id.textView);
        TextView tv2 = findViewById(R.id.textView2);
        tv.setText(announcement.getTitle());
        tv.setText(announcement.getBody());
    }
}
