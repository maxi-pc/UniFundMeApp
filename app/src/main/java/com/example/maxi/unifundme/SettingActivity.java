package com.example.maxi.unifundme;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatDelegate;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends BaseActivity {

    private Switch themeSwitch;
    private TextView gitVersion;
    private RadioButton lightModeRdBtn;
    private RadioButton nightModeRdBtn;
    private RadioButton splashEnable;
    private RadioButton splashDisable;


    @SuppressLint("MissingSuperCall")
    protected final void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState, R.layout.activity_setting);

        lightModeRdBtn = (RadioButton)findViewById(R.id.settingsRdBtnLight);
        nightModeRdBtn = (RadioButton)findViewById(R.id.settingsRdBtnDark);
        splashEnable = (RadioButton)findViewById(R.id.splashRdBtnEnabled);
        splashDisable = (RadioButton)findViewById(R.id.splashRdBtnDisabled);
        themeSwitch = findViewById(R.id.themeSwitch);

        // change setting defaults based off user preferences
        if(themePref.equals("Light"))
            lightModeRdBtn.setChecked(true);
        else if(themePref.equals("Dark"))
            nightModeRdBtn.setChecked(true);

        if(alertSounds.equals("ON"))
            themeSwitch.setChecked(true);
        else if(themePref.equals("OFF"))
            themeSwitch.setChecked(false);

        if(skipSplash.equals("enable"))
            splashEnable.setChecked(true);
        else if(skipSplash.equals("disable"))
            splashDisable.setChecked(true);



        gitVersion = (TextView)findViewById(R.id.versionNumberLabel);

        // set the build version to that off Android + Github hash code
        gitVersion.setText(Integer.toString(BuildConfig.VERSION_CODE) +"."+ BuildConfig.GitHash);

        // event listening for the switch widget of sound alerts
        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if(isChecked == true){
                alertSounds = "ON";
                Toast.makeText(SettingActivity.this, "Alert sounds enabled", Toast.LENGTH_SHORT).show();
                StoreSharedPrefs();
                AudioManager amanager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
                amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
            }
            else {
                alertSounds = "OFF";
                Toast.makeText(SettingActivity.this, "Alert sounds disabled", Toast.LENGTH_SHORT).show();
                StoreSharedPrefs();
                AudioManager amanager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
                amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
            }
        });

        // click listener for radio button Light
        lightModeRdBtn.setOnClickListener(view -> {
            Toast.makeText(SettingActivity.this, "Theme set to Light Mode", Toast.LENGTH_SHORT).show();
            themePref = "Light";
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            StoreSharedPrefs();
            recreate();
        });

        // click listener for radio button Light
        nightModeRdBtn.setOnClickListener(view -> {
            Toast.makeText(SettingActivity.this, "Theme set to Night Mode", Toast.LENGTH_SHORT).show();
            themePref = "Dark";
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            StoreSharedPrefs();
            recreate();
        });

        // click listener for radio button dark
        splashEnable.setOnClickListener(view -> {
            Toast.makeText(SettingActivity.this, "Splash set to enabled", Toast.LENGTH_SHORT).show();
            skipSplash = "enable";
            StoreSharedPrefs();
        });

        // click listener for radio button splash enable
        splashDisable.setOnClickListener(view -> {
            Toast.makeText(SettingActivity.this, "Splash set to disabled", Toast.LENGTH_SHORT).show();
            skipSplash = "disable";
            StoreSharedPrefs();
        });
    }

}
