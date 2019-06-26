package com.wambly.iytem;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 17300;
    AppUpdateManager appUpdateManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        checkUpdate(this);

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

        saveContacts();
        saveTransportation();
        saveMonthlyMenu();
    }


/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
*/
    public static String getHtml(String url) throws IOException {
        // Build and set timeout values for the request.
        URLConnection connection = (new URL(url)).openConnection();
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        connection.connect();

        // Read and store the result line by line then return the entire string.
        InputStream in = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder html = new StringBuilder();
        for (String line; (line = reader.readLine()) != null; ) {
            html.append(line);
        }
        in.close();
        return html.toString();
    }


    private void saveContacts(){
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = mDatabase.getReference().child("contacts").child("vcs");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Integer vcs = dataSnapshot.getValue(Integer.class);
                if (prefs.getInt("ContactsVCS",-1)< vcs){
                    final String s = "https://iytem-e266d.firebaseio.com/contacts.json";
                    final FileOutputStream[] outputStream = new FileOutputStream[1];
                    try {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String x;
                                SharedPreferences.Editor editor = prefs.edit();
                                try {
                                    x = getHtml(s);
                                    try {
                                        outputStream[0] = openFileOutput("contacts.json", Context.MODE_PRIVATE);
                                        outputStream[0].write(x.getBytes());
                                        outputStream[0].close();

                                        editor.putInt("ContactsVCS", vcs);
                                        editor.apply();
                                    } catch (FileNotFoundException e) {
                                        editor.putInt("MonthlyMenuVCS", -1);
                                        editor.apply();
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        editor.putInt("MonthlyMenuVCS", -1);
                                        editor.apply();
                                        e.printStackTrace();
                                    }
                                } catch (IOException e) {
                                    editor.putInt("MonthlyMenuVCS", -1);
                                    editor.apply();
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }catch (Exception e) {
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putInt("MonthlyMenuVCS", -1);
                        editor.apply();
                        Log.e("Main",""+e.toString());
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("MonthlyMenuVCS", -1);
                editor.apply();
            }
        });
    }

    private void saveMonthlyMenu(){

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = mDatabase.getReference().child("food").child("vcs");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                final Integer vcs = dataSnapshot.getValue(Integer.class);
                if (prefs.getInt("MonthlyMenuVCS",-1)< vcs){
                    final String s = "https://iytem-e266d.firebaseio.com/food.json";
                    final FileOutputStream[] outputStream = new FileOutputStream[1];
                    try {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String x;
                                try {
                                    x = getHtml(s);
                                    try {
                                        outputStream[0] = openFileOutput("monthlyMenu.json", Context.MODE_PRIVATE);
                                        outputStream[0].write(x.getBytes());
                                        outputStream[0].close();
                                        SharedPreferences.Editor editor = prefs.edit();
                                        editor.putInt("MonthlyMenuVCS", vcs);
                                        editor.apply();
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }catch (Exception e) {
                        Log.e("Main",""+e.toString());
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void saveTransportation(){
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = mDatabase.getReference().child("transportation").child("vcs");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Integer vcs = dataSnapshot.getValue(Integer.class);
                if (prefs.getInt("TransportationVCS",-1)< vcs){
                    final String s = "https://iytem-e266d.firebaseio.com/transportation.json";
                    final FileOutputStream[] outputStream = new FileOutputStream[1];
                    try {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String x;
                                try {
                                    x = getHtml(s);
                                    try {
                                        outputStream[0] = openFileOutput("transportation.json", Context.MODE_PRIVATE);
                                        outputStream[0].write(x.getBytes());
                                        outputStream[0].close();
                                        SharedPreferences.Editor editor = prefs.edit();
                                        editor.putInt("TransportationVCS", vcs);
                                        editor.apply();
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }catch (Exception e) {
                        Log.e("Main",""+e.toString());
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void checkUpdate(Context context) {
        // Creates instance of the manager.
        appUpdateManager = AppUpdateManagerFactory.create(context);

        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {

                    Log.d("Support in-app-update", "UPDATE_AVAILABLE");

                    if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                        requestUpdate(appUpdateInfo, AppUpdateType.IMMEDIATE);
                    } else { //AppUpdateType.FLEXIBLE
                        requestUpdate(appUpdateInfo, AppUpdateType.FLEXIBLE);
                    }

                } else {
                    Log.d("Support in-app-update", "UPDATE_NOT_AVAILABLE");
                }
            }
        });
    }

    private void requestUpdate(AppUpdateInfo appUpdateInfo, int flow_type) {
        try {
            appUpdateManager.startUpdateFlowForResult(appUpdateInfo,
                    flow_type,
                    this,
                    MY_REQUEST_CODE);

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
                // If the update is cancelled or fails,
                // you can request to start the update again.
            }
        }
    }

    public void onBackPressed() {
        moveTaskToBack(true);
    }

}
