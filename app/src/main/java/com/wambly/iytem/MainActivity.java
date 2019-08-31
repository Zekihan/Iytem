package com.wambly.iytem;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 17300;
    private AppUpdateManager appUpdateManager;
    private boolean dbStat = false;
    private boolean dbLoaded = false;
    Handler handler = new Handler();
    JsonUpdater jsonUpdater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);



        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        checkUpdate(this);

        jsonUpdater = new JsonUpdater();
        jsonUpdater.updateContacts(this);
        jsonUpdater.updateTransportation(this);
        jsonUpdater.updateMonthlyMenu(this);

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

        dbCheck();

        if(!dbStat){
            Toast.makeText(this, "İnternetinizin açık olduğundan emin olun",
                    Toast.LENGTH_LONG).show();
            handler.post(runnableCode);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @SuppressWarnings("SwitchStatementWithTooFewBranches")
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.feedback:
                sendFeedback();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            jsonUpdater.updateContacts(getApplicationContext());
            jsonUpdater.updateTransportation(getApplicationContext());
            jsonUpdater.updateMonthlyMenu(getApplicationContext());
            dbCheck();
            Log.d("loop", "run: tryin");
            if(dbStat){
                handler.removeCallbacks(runnableCode);
                Toast.makeText(getApplicationContext(), "Kullanıma Hazır",
                        Toast.LENGTH_LONG).show();
                finish();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }else{
                handler.postDelayed(this, 500);
            }

        }
    };


    private void dbCheck(){
        try {
            File temp0 = new File(getFilesDir(),"food.json");
            File temp1 = new File(getFilesDir(),"contacts.json");
            File temp2 = new File(getFilesDir(),"transportation.json");
            dbStat = temp0.exists() && temp1.exists() && temp2.exists();

        } catch (Exception e) {
            dbStat = false;
        }
    }

    private void checkUpdate(Context context) {

        appUpdateManager = AppUpdateManagerFactory.create(context);

        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {

                    Log.d("Support in-app-update", "UPDATE_AVAILABLE");
                    requestUpdate(appUpdateInfo, AppUpdateType.FLEXIBLE);
                } else {
                    Log.d("Support in-app-update", "UPDATE_NOT_AVAILABLE");
                }
            }

        });
    }

    private void requestUpdate(AppUpdateInfo appUpdateInfo, @SuppressWarnings("SameParameterValue") int flow_type) {
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
        Uri data = Uri.parse("mailto:wamblywambly@gmail.com?subject=" + "Feedback for [Iytem] app" + "&body="
                + "\n"+ "\n"+ "\n"+ "\n"+ "\n"+ "\n"+ "\n"+ "\n"
                + "--------------------------------------" + System.lineSeparator()
                + "Android API Level: " + Build.VERSION.SDK_INT + System.lineSeparator()
                + "Brand and Model: " + Build.BRAND + " " + android.os.Build.MODEL +  System.lineSeparator()
                + "App Version and Code: " + BuildConfig.VERSION_NAME + " / " + BuildConfig.VERSION_CODE + System.lineSeparator()
                + "--------------------------------------"
        );
        intent.setData(data);
        startActivity(intent);
    }

}