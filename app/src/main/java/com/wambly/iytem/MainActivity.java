package com.wambly.iytem;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.util.JsonMapper;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        View announcements = findViewById(R.id.announcements);
        announcements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AnnouncementsActivity.class));
            }
        });
        View transportation = findViewById(R.id.transportation);
        transportation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TransportationActivity.class));
            }
        });
        View tips = findViewById(R.id.tips);
        tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TipsActivity.class));
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

        saveTransportation();
        saveMonthlyMenu();
        saveContacts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public static String getHtml(String fileURL)
            throws IOException {
        GetMethod get = new GetMethod(fileURL);
        HttpClient client = new HttpClient();
        HttpClientParams params = client.getParams();
        params.setSoTimeout(2000);
        params.setParameter(HttpMethodParams.USER_AGENT,
                "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2"
        );
        client.setParams(params);
        try {
            client.executeMethod(get);
        } catch(ConnectException e){
            // Add some context to the exception and rethrow
            throw new IOException("ConnectionException trying to GET " +
                    fileURL,e);
        }

        if(get.getStatusCode()!=200){
            throw new FileNotFoundException(
                    "Server returned " + get.getStatusCode());
        }
        return  IOUtils.toString(get.getResponseBodyAsStream());
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
                    final String s = "https://iytem-e266d.firebaseio.com/transportation/eshot.json";
                    final FileOutputStream[] outputStream = new FileOutputStream[1];
                    try {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String x = null;
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
                                String x = null;
                                try {
                                    x = getHtml(s);
                                    try {
                                        outputStream[0] = openFileOutput("contacts.json", Context.MODE_PRIVATE);
                                        outputStream[0].write(x.getBytes());
                                        outputStream[0].close();
                                        SharedPreferences.Editor editor = prefs.edit();
                                        editor.putInt("ContactsVCS", vcs);
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

    public void onBackPressed() {
        //  super.onBackPressed();
        moveTaskToBack(true);
    }

}
