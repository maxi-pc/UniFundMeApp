package com.example.maxi.unifundme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Maxi on 2018-03-25.
 */

public abstract class BaseActivity extends AppCompatActivity {

    public DrawerLayout dLayout;
    public String themePref;
    public DatabaseManager db;
    public String userName;
    public User userAccount;
    public Toolbar mToolbar;


    protected final void onCreate(Bundle savedInstanceState, int layoutId) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        themePref = prefs.getString("ThemePrefs", "Light");

        if(themePref.equals("Light"))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        else if(themePref.equals("Dark"))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        db = new DatabaseManager(this);


        userName = prefs.getString("LoggedUserName", "");
        userAccount = db.getUserInfo(new String[] {userName});
        mToolbar = (Toolbar)findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);

        assert mToolbar != null;
        mToolbar.setNavigationIcon(R.drawable.mobilebars);

        mToolbar.setOverflowIcon(ContextCompat.getDrawable(getApplicationContext(),R.drawable.threedots));

        mToolbar.setNavigationOnClickListener(view -> dLayout.openDrawer(Gravity.LEFT));
        setNavigationDrawer();
    }

    public void setNavigationDrawer() {

        DatabaseManager db = new DatabaseManager(this);
        User currentUser;
        String userName;

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        userName = prefs.getString("LoggedUserName", "");

        currentUser = db.getUserInfo(new String[] {userName});

        dLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView) findViewById(R.id.navigation);

        navView.setNavigationItemSelectedListener(menuItem -> {
            int itemId = menuItem.getItemId();

            if (itemId == R.id.left_news) {
                startActivity(new Intent(this, MainActivity.class));
            } else if (itemId == R.id.left_quick_search)
            {
                if(currentUser.getProfileSet() == 0)
                {
                    Toast.makeText(this, "Must setup account for this feature", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent myIntent = new Intent(this, ViewDataActivity.class);
                    myIntent.putExtra("searchType", "auto");
                    startActivity(myIntent);
                }

            } else if (itemId == R.id.left_manual_search) {
                startActivity(new Intent(this, ManualSearchActivity.class));
            }

            return false;
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        DatabaseManager db = new DatabaseManager(this);
        User currentUser;
        String userName;

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        userName = prefs.getString("LoggedUserName", "");

        currentUser = db.getUserInfo(new String[] {userName});

        switch(item.getItemId()) {
            case R.id.profileItem:
                startActivity(new Intent(this, ProfileActivity.class));
                break;
            case R.id.savedAwardItem:
                startActivity(new Intent(this, SavedAwardsActivity.class));
                break;
            case R.id.settingsItem:
                startActivity(new Intent(this, SettingActivity.class));
                break;
            case R.id.exitItem:
                finish();
                moveTaskToBack(true);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;

    }

    public void StoreSharedPrefs(){
        final SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("ThemePrefs", themePref);
        editor.commit();
    }
}
