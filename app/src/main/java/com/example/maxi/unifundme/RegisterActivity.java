package com.example.maxi.unifundme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private String email;
    private String username;
    private String password;
    EditText emailText;
    EditText userText;
    EditText passText;
    Button registerBtn;
    Button cancelBtn;
    DatabaseManager db;
    private EditText passwordOneEditText;
    private EditText passwordTwoEditEdit;
    private String passwordOne;
    private String passwordTwo;
    private String themePref;

    private String[] userInput = new String[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // change theme based on user settings
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        themePref = prefs.getString("ThemePrefs", "Light");
        if(themePref.equals("Light"))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        else if(themePref.equals("Dark"))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        setContentView(R.layout.activity_register);

        db = new DatabaseManager(this);

        registerBtn = (Button)findViewById(R.id.registerBtn);
        cancelBtn = (Button)findViewById(R.id.cancelBtn);

    registerBtn.setOnClickListener(view -> {
        try {
            validationCheck();
        } catch (IOException e) {
            e.printStackTrace();
        }
    });

    cancelBtn.setOnClickListener(view -> {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
    });
    }



    public final static boolean CheckEmail(CharSequence email)
    {
        if (TextUtils.isEmpty(email))
        {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

    public void validationCheck()throws IOException {

        boolean passMatch = true;
        boolean passLessThanCheck = true;
        boolean emailCheck = true;
        boolean userCheck = true;

        emailText = (EditText)findViewById(R.id.emaiEditText);
        userText = (EditText)findViewById(R.id.usernameEditText);
   //     passText = (EditText)findViewById(R.id.passwordEditText);
        username = userText.getText().toString();
        email = emailText.getText().toString();
      //  password = passText.getText().toString();

        passwordOneEditText = (EditText) findViewById(R.id.passwordEditText);
        passwordTwoEditEdit = (EditText) findViewById(R.id.passwordEditTextTwo);
        passwordOne = passwordOneEditText.getText().toString().trim();
        passwordTwo = passwordTwoEditEdit.getText().toString().trim();

        String one, two, three, four = "", five = "", error;



        if (CheckEmail(emailText.getText()) == false) {
            one = "Email Check\n";

        } else {
            one = "";
        }
        if (username.isEmpty() || username.length() <= 5) {
            two = "Username Check (char min 6)\n";
            userCheck = false;
        } else {
            two = "";
        }

            //check if they match
            if ((!passwordTwo.equals(passwordOne)) || (!passwordOne.equals(passwordTwo))) {

                four = "Passwords do not match\n";
                passMatch = false;
            } else {

                four = "";
            }

            if (passwordTwo.length() <= 5 || passwordTwo.length() <= 5 || passwordOne.isEmpty() || passwordTwo.isEmpty()) {

                five = "Min password 6 chars\n";
                passLessThanCheck = false;
            } else {
                five = "";
            }

        error = one + two + four + five;
        if (CheckEmail(emailText.getText()) == false || userCheck == false ||
                passLessThanCheck == false || passMatch == false) {

            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            findViewById(R.id.registerScreen).startAnimation(shake);
            Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();

        }
        else {

         //   setValuesOfSearch();
               CheckDatabase();
             // Toast.makeText(RegisterActivity.this, "SUCCESS!", Toast.LENGTH_LONG).show();

        }
    }

    private void CheckDatabase() {
        // check name
        String one, two, error;
        boolean checkUser = db.valueChecker("users", "username", new String[] {username});
        boolean checkEmail = db.valueChecker("users", "email", new String[] {email});

        if (checkUser == false) {
            one = "Username already exists\n";
        } else {
            one = "";
        }
        if (checkEmail == false) {
            two = "email already exists\n";
        } else {
            two = "";
        }

        error = one + two;
        if(checkUser == false || checkEmail == false)
        {

            Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();
        }
        else
        {
            //Toast.makeText(RegisterActivity.this, "YES CAN INSERT INTO DB", Toast.LENGTH_SHORT).show();

            User myAccount = new User(username, email, passwordOne);
            db.CreateUser(myAccount);
            Toast.makeText(RegisterActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        }
    }
}
