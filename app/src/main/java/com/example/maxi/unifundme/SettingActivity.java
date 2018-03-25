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

public class SettingActivity extends BaseActivity {
    // CompoundButton.OnCheckedChangeListener

    Switch themeSwitch;
    private TextView gitVersion;
    private RadioButton lightModeRdBtn;
    private RadioButton nightModeRdBtn;

    //private Boolean isChecked;

    protected final void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState, R.layout.activity_setting);

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
            Toast.makeText(SettingActivity.this, "Theme set to Light Mode", Toast.LENGTH_SHORT).show();
            themePref = "Light";
            StoreSharedPrefs();
            recreate();
        });

        nightModeRdBtn.setOnClickListener(view -> {
            Toast.makeText(SettingActivity.this, "Theme set to Night Mode", Toast.LENGTH_SHORT).show();
            themePref = "Dark";
            StoreSharedPrefs();
            recreate();
        });
    }
}
