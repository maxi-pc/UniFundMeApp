package com.example.maxi.unifundme;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private User userAccount;
    private String userName;
    private TextView welcome;
    private DatabaseManager db;
    private TextView profileMessage;


    //File dbFile = getDatabasePath("unifundme");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profileMessage = (TextView)findViewById(R.id.profileAlertMessage);

        db = new DatabaseManager(this);

        welcome = (TextView)findViewById(R.id.welcome);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        userName = prefs.getString("LoggedUserName", "");

        userAccount = db.getUserInfo(new String[] {userName});
        welcome.append(" " + userName);

        if(userAccount.getProfileSet() == 0) {
            profileMessage.setText("Please update your profile");
        }

        profileMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileMessage.setText("");
            }
        });

        /*
        String restoredText = prefs.getString("text", null);
        if (restoredText != null) {
            String name = prefs.getString("name", "No name defined");//"No name defined" is the default value.
            int idName = prefs.getInt("idName", 0); //0 is the default value.


        }
        */
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
