package com.wambly.iytem;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;

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

public class JsonUpdater {

    public void updateContacts(Context context){
        updateJson("https://iytem-e266d.firebaseio.com/contacts.json", "contacts",context);
    }
    public void updateMonthlyMenu(Context context){
        updateJson("https://iytem-e266d.firebaseio.com/food.json", "food", context);
    }
    public void updateTransportation(Context context){
        updateJson("https://iytem-e266d.firebaseio.com/transportation.json", "transportation",context);
    }

    private void updateJson(final String dbUrl, final String name, final Context context){
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = mDatabase.getReference().child(name).child("vcs");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Integer vcs = dataSnapshot.getValue(Integer.class);
                if (prefs.getInt(name + "Vcs",-1)< vcs){
                    final FileOutputStream[] outputStream = new FileOutputStream[1];
                    try {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String x;
                                try {
                                    x = getHtml(dbUrl);
                                    try {
                                        outputStream[0] = context.openFileOutput(name + ".json", Context.MODE_PRIVATE);
                                        outputStream[0].write(x.getBytes());
                                        outputStream[0].close();
                                        SharedPreferences.Editor editor = prefs.edit();
                                        editor.putInt(name + "Vcs", vcs);
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

    private String getHtml(String url) throws IOException {
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
}