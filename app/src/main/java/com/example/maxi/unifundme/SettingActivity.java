package com.example.maxi.unifundme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {
    // CompoundButton.OnCheckedChangeListener

    Switch themeSwitch;
    private TextView gitVersion;
    private String themePref;
    private RadioButton lightModeRdBtn;
    private RadioButton nightModeRdBtn;

    //private Boolean isChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        themePref = prefs.getString("ThemePrefs", "Light");

        if(themePref.equals("Light"))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        else if(themePref.equals("Dark"))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        setContentView(R.layout.activity_setting);

        lightModeRdBtn = (RadioButton)findViewById(R.id.settingsRdBtnLight);
        nightModeRdBtn = (RadioButton)findViewById(R.id.settingsRdBtnDark);
        themeSwitch = findViewById(R.id.themeSwitch);

        if(themePref.equals("Light"))
            lightModeRdBtn.setChecked(true);
        else if(themePref.equals("Dark"))
            nightModeRdBtn.setChecked(true);





        gitVersion = (TextView)findViewById(R.id.versionNumberLabel);

        gitVersion.setText(Integer.toString(BuildConfig.VERSION_CODE) +"."+ BuildConfig.GitHash);

        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if(isChecked == true){
                Toast.makeText(SettingActivity.this, "Alert sounds enabled", Toast.LENGTH_SHORT).show();
                // enable sounds
            }
            else {
                Toast.makeText(SettingActivity.this, "Alert sounds disabled", Toast.LENGTH_SHORT).show();
                // disable sounds
            }
        });

        lightModeRdBtn.setOnClickListener(view -> {
            themePref = "Light";
            StoreSharedPrefs();
            recreate();
        });

        nightModeRdBtn.setOnClickListener(view -> {
            themePref = "Dark";
            StoreSharedPrefs();
            recreate();
        });
    }

    private void StoreSharedPrefs(){
        final SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("ThemePrefs", themePref);
        editor.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the main_menu; this adds items to the action bar if it is present.
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
            case R.id.searchItem:
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
                break;
            case R.id.searchManuallyItem:
                startActivity(new Intent(this, ManualSearchActivity.class));
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
}
