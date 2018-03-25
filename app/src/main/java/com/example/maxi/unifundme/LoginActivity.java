package com.example.maxi.unifundme;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    //private SQLiteDatabase db;
    private TextView registerTxt;
    private Button loginBtn;
    private EditText usernameEditText;
    private EditText passwordEditExit;
    private String username;
    private String password;
    private DatabaseManager db;
    private ImageView googleBtn;
    private ImageView facebookBtn;
    private String userName;
    private String themePref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        themePref = prefs.getString("ThemePrefs", "Light");
        userName = prefs.getString("LoggedUserName", "");

        if(themePref.equals("Light"))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        else if(themePref.equals("Dark"))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        setContentView(R.layout.activity_login);

        usernameEditText = (EditText)findViewById(R.id.usernameLoginEditText);
        passwordEditExit = (EditText)findViewById(R.id.passwordLoginEditText);

        usernameEditText.setText(userName);

        //DatabaseManager();
         db = new DatabaseManager(this);

        registerTxt = (TextView)findViewById(R.id.registerTextView);

        registerTxt.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));

        loginBtn = (Button) findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(view -> checkLogin());

        googleBtn = (ImageView)findViewById(R.id.googleLogBtn);
        facebookBtn = (ImageView)findViewById(R.id.facebookLogBtn);

        googleBtn.setOnClickListener(view -> Toast.makeText(LoginActivity.this, "Work in progress :)", Toast.LENGTH_SHORT).show());

        facebookBtn.setOnClickListener(view -> Toast.makeText(LoginActivity.this, "Work in progress :)", Toast.LENGTH_SHORT).show());
    }

    /*
    public void validationCheck()throws IOException {

        username = usernameEditText.getText().toString();
        password = passwordEditExit.getText().toString();

        String two, three, error;

        if (username.isEmpty() || username.length() <= 5) {
            two = "Username Check (char min 6)\n";
        } else {
            two = "";
        }
        if (password.isEmpty() || password.length() <= 5) {
            three = "Password  Check (char min 6)\n";
        } else {
            three = "";
        }

        error = two + three;
        if (username.isEmpty() ||username.length()  <= 5 ||
                password.isEmpty() || password.length() <= 5) {

            Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();

        }
        else {

            //   setValuesOfSearch();
            checkLogin();
            // Toast.makeText(RegisterActivity.this, "SUCCESS!", Toast.LENGTH_LONG).show();

        }
    }
    */

    void checkLogin()
    {

        String one, two, three, error;
        usernameEditText = (EditText)findViewById(R.id.usernameLoginEditText);
        passwordEditExit = (EditText)findViewById(R.id.passwordLoginEditText);
        username = usernameEditText.getText().toString();
        password = passwordEditExit.getText().toString();

        boolean checkUser = db.valueChecker("users", "username", new String[] {username});
     //   boolean checkPass = db.valueChecker("users", "email", new String[] {password});
        boolean checkMatch = db.checkMatch("users","username", "password", new String[] { username, password });

        if (checkUser == true) {
            one = "Username doesn't exists\n";
        } else {
            one = "";
        }
        if (checkMatch == true) {
            three = "Incorrect password\n";
        }
        else {
            three = "";
        }

        if(checkUser == true)
        {
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            findViewById(R.id.loginScreen).startAnimation(shake);

            Toast.makeText(LoginActivity.this, one, Toast.LENGTH_SHORT).show();
        }
        else if(checkUser == false && checkMatch == true)
        {
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            findViewById(R.id.loginScreen).startAnimation(shake);
            Toast.makeText(LoginActivity.this, three, Toast.LENGTH_SHORT).show();
        }
        else{
            StoreSharedPrefs();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
         //   passwordEditExit.getText().clear();
        }
    }

    private void StoreSharedPrefs(){
        final SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("LoggedUserName", username.toString());
        editor.commit();
    }

}
