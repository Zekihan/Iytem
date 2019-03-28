package com.wambly.iytem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class TipsDetailsActivity extends AppCompatActivity {

    private Tip tip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips_details);

        tip = getIntent().getParcelableExtra("tip");

        TextView tv = findViewById(R.id.textView);
        TextView tv2 = findViewById(R.id.textView2);
        tv.setText(tip.getTitle());
        tv2.setText(tip.getBody());
    }
}
