package com.wambly.iytem;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;

public class MainActivity extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 17300;
    AppUpdateManager appUpdateManager;
    SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        checkUpdate(this);

        JsonUpdater jsonUpdater = new JsonUpdater();
        jsonUpdater.updateContacts(this);
        jsonUpdater.updateTransportation(this);
        jsonUpdater.updateMonthlyMenu(this);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        View transportation = findViewById(R.id.transportation);
        transportation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TransportationActivity.class));
            }
        });
        View food = findViewById(R.id.food);
        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FoodActivity.class));
            }
        });

        View shortcuts = findViewById(R.id.shortcuts);
        shortcuts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ShortcutActivity.class));
            }
        });

        View contacts = findViewById(R.id.contacts);
        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ContactsActivity.class));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void checkUpdate(Context context) {

        appUpdateManager = AppUpdateManagerFactory.create(context);

        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {

                    Log.d("Support in-app-update", "UPDATE_AVAILABLE");
                    requestUpdate(appUpdateInfo, AppUpdateType.IMMEDIATE);
                } else {
                    Log.d("Support in-app-update", "UPDATE_NOT_AVAILABLE");
                }
            }

        });
    }

    private void requestUpdate(AppUpdateInfo appUpdateInfo, int flow_type) {
        try {
            appUpdateManager.startUpdateFlowForResult(appUpdateInfo, flow_type, this, MY_REQUEST_CODE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                Log.w("Update flow failed! ", "Result code: " + resultCode);
                checkUpdate(this);
            }
        }
    }

    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private void sendFeedback() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.parse("mailto:wamblywambly@gmail.com?subject=" + "Feedback for [Iytem] app" + "&body=" + System.lineSeparator()
                + System.lineSeparator() + System.lineSeparator() + System.lineSeparator() + System.lineSeparator()
                + "Don't touch the lines below,they are required for us to make faster and more accurate fixes" + System.lineSeparator()
                + "SDK :" + Build.VERSION.SDK_INT + System.lineSeparator()
                + "Device :" + android.os.Build.DEVICE + System.lineSeparator()
                + "Model :" + android.os.Build.MODEL + System.lineSeparator()
                + "BRAND :" + Build.BRAND + System.lineSeparator()
                + "Version Code :" + BuildConfig.VERSION_CODE + System.lineSeparator()
                + "Version Name :" + BuildConfig.VERSION_NAME + System.lineSeparator()
        );
        intent.setData(data);
        startActivity(intent);
    }

}