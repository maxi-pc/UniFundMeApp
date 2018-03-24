package com.example.maxi.unifundme;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;

public class SplashActivity extends AppCompatActivity {

    private CheckBox dontShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_splash);
    }

    private void StoreSharedPrefs(){
        final SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("LoggedUserName", dontShow.toString());
        editor.commit();
    }
}
