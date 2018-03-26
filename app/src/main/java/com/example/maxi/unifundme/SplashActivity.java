package com.example.maxi.unifundme;

import android.annotation.SuppressLint;
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

public class SplashActivity extends BaseActivity {

    private CheckBox dontShow;
    private ImageView continueImage;
 //   private String skipSplash;
  //  private String themePref;

    @SuppressLint("MissingSuperCall")
    protected final void onCreate(Bundle savedInstanceState) {
        showToolbar = false;

        super.onCreate(savedInstanceState, R.layout.activity_splash);

        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        dontShow = (CheckBox) findViewById(R.id.dontShowCheckBox);
        continueImage = (ImageView) findViewById(R.id.continueBtn);

        if (skipSplash.equals("enable") || userName.equals("") ) {

            continueImage.setOnClickListener(view -> {
                if(dontShow.isChecked())
                {
                    skipSplash = "disable";
                }
                StoreSharedPrefs();
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            });
        }
        else if(skipSplash.equals("disable")){
            //finishAffinity();
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }
    }
}
