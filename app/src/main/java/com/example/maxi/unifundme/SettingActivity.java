package com.example.maxi.unifundme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {
    // CompoundButton.OnCheckedChangeListener

    Switch themeSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        themeSwitch = findViewById(R.id.themeSwitch);

      //  themeSwitch.setOnCheckedChangeListener(this);
    }

    /*
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if(themeSwitch.isChecked())
        {
            setTheme(android.R.style.Theme_Light);
         //   setContentView(R.layout.activity_setting);
            recreate();


            Toast.makeText(SettingActivity.this, "yes", Toast.LENGTH_LONG).show();

        }
        else{
            setTheme(android.R.style.Theme_Material);
        //    setContentView(R.layout.activity_setting);
            recreate();
            Toast.makeText(SettingActivity.this, "no", Toast.LENGTH_LONG).show();
        }
    }
    */

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

    // Switch themeSwtch = (Switch)findViewById(R.id.themeSwitch);
}
