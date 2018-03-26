package com.example.maxi.unifundme;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Method;

/**
 * Created by Maxi on 2018-03-25.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected DrawerLayout dLayout;
    protected String themePref;
    protected DatabaseManager db;
    protected String userName;
    protected String savedUsername;
    protected User userAccount;
    protected String skipSplash;
    protected String alertSounds;
    protected Toolbar mToolbar;
    protected boolean showToolbar = true;


    protected final void onCreate(Bundle savedInstanceState, int layoutId) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        themePref = prefs.getString("ThemePrefs", "Light");
        userName = prefs.getString("LoggedUserName", "");
        alertSounds = prefs.getString("AlertSounds","OFF");
        savedUsername = prefs.getString("savedUsername", "");
        skipSplash = prefs.getString("SkipSplash", "enabled");

        db = new DatabaseManager(this);



        userAccount = db.getUserInfo(new String[] {userName});

        if(showToolbar == true) {
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(mToolbar);

            assert mToolbar != null;
            mToolbar.setNavigationIcon(R.drawable.mobilebars);

            mToolbar.setOverflowIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.threedotsthin));

            mToolbar.setNavigationOnClickListener(view -> dLayout.openDrawer(Gravity.LEFT));
            setNavigationDrawer();
        }
    }

    public void setNavigationDrawer() {

        dLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView) findViewById(R.id.navigation);

        navView.setNavigationItemSelectedListener(menuItem -> {
            int itemId = menuItem.getItemId();

            if (itemId == R.id.left_news) {
                startActivity(new Intent(this, MainActivity.class));
            } else if (itemId == R.id.left_quick_search)
            {
                if(userAccount.getProfileSet() == 0)
                {
                    Toast.makeText(this, "Must setup profile for this feature", Toast.LENGTH_SHORT).show();
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

                Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                moveTaskToBack(true);
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        return true;

    }

    @SuppressLint("RestrictedApi")
    @Override
    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
        if (menu != null) {
            if (menu.getClass().equals(MenuBuilder.class)) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "onMenuOpened...can't add icons to menu items", e);
                }
            }
        }
        return super.onPrepareOptionsPanel(view, menu);
    }

    public void StoreSharedPrefs(){
        final SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("ThemePrefs", themePref);
        editor.putString("AlertSounds", alertSounds);
        editor.putString("SkipSplash", skipSplash);
        editor.commit();
    }
}
