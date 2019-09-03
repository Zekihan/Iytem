package com.wambly.iytem;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

public class ShortcutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shortcut);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.shortcuts);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        View obs = findViewById(R.id.obs);
        obs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chromeTab("https://obs.iyte.edu.tr");
            }
        });

        View iyte = findViewById(R.id.mainpage);
        iyte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chromeTab("http://iyte.edu.tr/");
            }
        });
        View library = findViewById(R.id.library);
        library.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chromeTab("http://library.iyte.edu.tr/");
            }
        });
        View cms = findViewById(R.id.cms);
        cms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chromeTab("https://cms.iyte.edu.tr/login/index.php");
            }
        });
        View ydyo = findViewById(R.id.ydyo);
        ydyo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chromeTab("http://ydyo.iyte.edu.tr/");
            }
        });
        View mail = findViewById(R.id.webmail);
        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chromeTab("https://std.iyte.edu.tr/");
            }
        });
    }

    private void chromeTab(String url){
        CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
        intentBuilder.setStartAnimations(this,R.anim.slide_in_right , R.anim.slide_out_left);
        intentBuilder.setExitAnimations(this, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        intentBuilder.setToolbarColor(getResources().getColor(R.color.bgColor));
        CustomTabsIntent customTabsIntent = intentBuilder.build();
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }

}
