package com.example.maxi.unifundme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

    private CheckBox dontShow;
    private ImageView continueImage;
    private String skipSplash;
    private String themePref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        themePref = prefs.getString("ThemePrefs", "Light");
        skipSplash = prefs.getString("SkipSplash", "NO");

        if(themePref.equals("Light"))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        else if(themePref.equals("Dark"))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        setContentView(R.layout.activity_splash);

        dontShow = (CheckBox) findViewById(R.id.dontShowCheckBox);
        continueImage = (ImageView) findViewById(R.id.continueBtn);

        if (skipSplash.equals("NO")) {

            continueImage.setOnClickListener(view -> {
                if(dontShow.isChecked())
                {
                    skipSplash = "YES";
                }
                StoreSharedPrefs();
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            });
        }
        else if(skipSplash.equals("YES")){
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }
    }

    private void StoreSharedPrefs(){
        final SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("SkipSplash", skipSplash.toString());
        editor.commit();
    }
}
