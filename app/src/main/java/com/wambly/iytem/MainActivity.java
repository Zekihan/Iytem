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
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    SharedPreferences prefs;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeFade);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean darkTheme = prefs.getBoolean("darkTheme",true);
        if(darkTheme){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        MenuItem nav_theme = menu.findItem(R.id.theme);

        if(darkTheme){
            nav_theme.setTitle(getString(R.string.light_theme));
        }else{
            nav_theme.setTitle(getString(R.string.dark_theme));
        }

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.keepSynced(true);

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

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.feedback:
                sendFeedback();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void sendFeedback() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.parse("mailto:wamblywambly@gmail.com?subject=" + "Feedback for [Iytem] app" + "&body="
                + "\n"+ "\n"+ "\n"+ "\n"+ "\n"+ "\n"+ "\n"+ "\n"
                + "--------------------------------------" + "\n"
                + "Android API Level: " + Build.VERSION.SDK_INT + "\n"
                + "Brand and Model: " + Build.BRAND + " " + android.os.Build.MODEL + "\n"
                + "App Version and Code: " + BuildConfig.VERSION_NAME + " / " + BuildConfig.VERSION_CODE + "\n"
                + "--------------------------------------"
        );
        intent.setData(data);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        boolean closeDrawer = true;
        switch (item.getItemId()) {
            case R.id.nav_share:
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                    String shareMessage= "\nİYTE'de hayat artık daha kolay!\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=com.wambly.iytem";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, getString(R.string.share)));
                } catch(Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.theme:
                boolean darkTheme = prefs.getBoolean("darkTheme",true);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("darkTheme",!darkTheme);
                editor.apply();
                recreate();
                closeDrawer = false;
                break;
            case R.id.nav_location:
                Uri gmmIntentUri = Uri.parse("geo:38.320356, 26.638675");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
                break;

            case R.id.nav_emergency:
                dialNum("02327506222");
                break;
            case R.id.nav_rectorate:
                dialNum("02327506000");
                break;
            case R.id.nav_student_affairs:
                dialNum("02327506300");
                break;
            default:
                break;
        }
        if(closeDrawer){
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    private void dialNum(String num){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        String uri = "tel:" + num ;
        intent.setData(Uri.parse(uri));
        startActivity(intent);
    }

}