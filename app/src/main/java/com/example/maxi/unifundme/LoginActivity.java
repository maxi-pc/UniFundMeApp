package com.example.maxi.unifundme;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;

public class LoginActivity extends BaseActivity {

    private TextView registerTxt;
    private Button loginBtn;
    private EditText usernameEditText;
    private EditText passwordEditExit;
  //  private String userName;
    private String password;
    private ImageView googleBtn;
    private ImageView facebookBtn;
  //  private String savedUsername;
    private CheckBox rememberMe;


 //   @Override
    @SuppressLint("MissingSuperCall")
    protected final void onCreate(Bundle savedInstanceState) {
        showToolbar = false;

        super.onCreate(savedInstanceState, R.layout.activity_login);

    //    final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    //    savedUsername = prefs.getString("savedUsername", "");

        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        rememberMe = (CheckBox)findViewById(R.id.remeberMeChkBox);
        usernameEditText = (EditText)findViewById(R.id.usernameLoginEditText);
        passwordEditExit = (EditText)findViewById(R.id.passwordLoginEditText);

        if(!savedUsername.equals(""))
            rememberMe.setChecked(true);

        usernameEditText.setText(savedUsername);

        registerTxt = (TextView)findViewById(R.id.registerTextView);

        registerTxt.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));

        loginBtn = (Button) findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(view -> {
            checkLogin();
            loginBtn.setEnabled(false);
            new Handler().postDelayed(() -> loginBtn.setEnabled(true),5000);
        });

        googleBtn = (ImageView)findViewById(R.id.googleLogBtn);
        facebookBtn = (ImageView)findViewById(R.id.facebookLogBtn);

        googleBtn.setOnClickListener(view -> {
            Toast.makeText(LoginActivity.this, "Work in progress :)", Toast.LENGTH_SHORT).show();
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            findViewById(R.id.loginScreen).startAnimation(shake);
        });

        facebookBtn.setOnClickListener(view -> {
            Toast.makeText(LoginActivity.this, "Work in progress :)", Toast.LENGTH_SHORT).show();
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            findViewById(R.id.loginScreen).startAnimation(shake);
        });
    }

    void checkLogin()
    {

        String one, two, three, error;
        usernameEditText = (EditText)findViewById(R.id.usernameLoginEditText);
        passwordEditExit = (EditText)findViewById(R.id.passwordLoginEditText);
        userName = usernameEditText.getText().toString();
        password = passwordEditExit.getText().toString();

        boolean checkUser = db.valueChecker("users", "username", new String[] {userName});
        boolean checkMatch = db.checkMatch("users","username", "password", new String[] { userName, password });

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
            savedUsername = usernameEditText.getText().toString();
            if(rememberMe.isChecked())
            StoreUsername();
            else{
                savedUsername = "";
                StoreUsername();
            }

            SetLoggedInUser();


            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

    public void StoreUsername(){
        final SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("savedUsername", savedUsername);
        editor.commit();
    }

    public void SetLoggedInUser(){
        final SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("LoggedUserName", userName);
        editor.commit();
    }

}
