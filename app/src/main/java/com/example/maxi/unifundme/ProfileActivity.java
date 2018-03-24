package com.example.maxi.unifundme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileActivity extends AppCompatActivity {

    private User profileInfo = null;
    private DatabaseManager db;
    private Spinner province;
    private Spinner school;
    private RadioGroup studies;
    private RadioGroup student;
    private RadioGroup aboriginal;
    private EditText gpa;
    private EditText passwordOneEditText;
    private EditText passwordTwoEditEdit;
    private String passwordOne;
    private String passwordTwo;
    // radio button values
    private String study;
    private String locality;
    private String aboriginality;
    private Button saveProfileBtn;
    // radio buttons
    private RadioButton fulltimeRdBtn;
    private RadioButton domesticRdBtn;
    private RadioButton abotiginalRdBtn;
    // spinner index / position values
    private String provinceInt;
    private String schoolInt;
    // gpa value
    private String gpaVal;
    private String[] queryStrings = new String[6];
    private Boolean passMatch = true;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        db = new DatabaseManager(this);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        userName = prefs.getString("LoggedUserName", "");

        profileInfo = db.getUserInfo(new String[] {userName});


        if(profileInfo.getProfileSet() == 1)
            LoadProfile();

        saveProfileBtn = (Button)findViewById(R.id.saveBtnProfile);

        saveProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    validationCheck();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void LoadProfile() {
        province = (Spinner) findViewById(R.id.provinceSpinnerProfile);
        school = (Spinner) findViewById(R.id.schoolSpinnerProfile);
        studies = (RadioGroup)findViewById(R.id.studiesRdGrpProfile);
        student = (RadioGroup)findViewById(R.id.domesticRdGrpProfile);
        aboriginal = (RadioGroup)findViewById(R.id.aborignalRdGrpProfile);
        gpa = (EditText)findViewById(R.id.gpaEditTextProfile);

        province.setSelection(Integer.parseInt(String.format(profileInfo.getProvince().toString())));
        school.setSelection(1 + Integer.parseInt(String.format(profileInfo.getSchool().toString())));

        if(Integer.parseInt(profileInfo.getStudy()) == 1) {

            studies.check(R.id.fullTimeRdBtnProfile);
        }
        else  {
            studies.check(R.id.partTimeRdBtnProfile);
        }

        if(Integer.parseInt(profileInfo.getLocality()) == 1) {

            student.check(R.id.domesticRdBtnProfile);
        }
        else  {
            student.check(R.id.internationalRdBtnProfile);
        }

        if(Integer.parseInt(profileInfo.getAboriginality()) == 1) {

            aboriginal.check(R.id.yesRdBtnProfile);
        }
        else  {
            aboriginal.check(R.id.noRdBtnProfile);
        }
        gpa.setText(String.format(profileInfo.getGpa().toString(), "#.##"));

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

    public void validationCheck()throws IOException {


        province = (Spinner) findViewById(R.id.provinceSpinnerProfile);
        school = (Spinner) findViewById(R.id.schoolSpinnerProfile);
        studies = (RadioGroup) findViewById(R.id.studiesRdGrpProfile);
        student = (RadioGroup) findViewById(R.id.domesticRdGrpProfile);
        aboriginal = (RadioGroup) findViewById(R.id.aborignalRdGrpProfile);
        gpa = (EditText) findViewById(R.id.gpaEditTextProfile);

        passwordOneEditText = (EditText) findViewById(R.id.profilePassOneEditText);
        passwordTwoEditEdit = (EditText) findViewById(R.id.profilePassTwoEditText);
        passwordOne = passwordOneEditText.getText().toString().trim();
        passwordTwo = passwordTwoEditEdit.getText().toString().trim();


        String one, two, three, four, five, six, seven = "", eight = "", nine = "", ten = "", eleven = "", error;
        boolean numeric = true, gpaformat = true, gpagreater = true;
        boolean passMatch = true;
        boolean provinceCheck = true;
        boolean schoolCheck = true;
        boolean studyCheck = true;
        boolean localityCheck = true;
        boolean aboriginalityCheck = true;
        boolean gpaCheck = true;
        boolean passLessThanCheck = true;


        String gpaStr = String.valueOf(gpa.getText());

        Pattern pattern = Pattern.compile("^\\d+\\.\\d{2}$");
        Matcher matcher = pattern.matcher(gpaStr);

        if (studies.getCheckedRadioButtonId() == -1) {
            one = "Studies Check\n";
            studyCheck = false;
        } else {
            one = "";
        }
        if (student.getCheckedRadioButtonId() == -1) {
            two = "Domestic Check\n";
            localityCheck = false;
        } else {
            two = "";
        }
        if (aboriginal.getCheckedRadioButtonId() == -1) {
            three = "Aboriginal Check\n";
            aboriginalityCheck = false;
        } else {
            three = "";
        }
        int posProvince = province.getSelectedItemPosition();
        if (posProvince == 0) {
            four = "Province Check\n";
            provinceCheck = false;
        } else {
            four = "";
        }
        int posSchool = school.getSelectedItemPosition();
        if (posSchool == 0) {
            five = "School Check\n";
            schoolCheck = false;
        } else {
            five = "";
        }
        if (gpa.getText().toString().trim().isEmpty()) {
            six = "GPA Check\n";
            gpaCheck = false;
        } else {
            six = "";
        }
        if (!gpa.getText().toString().trim().isEmpty()) {
            seven = "";
            eight = "";

            try {
                Double.valueOf(gpa.getText().toString());
            } catch (Exception e) {
                seven = "Non-Numeric GPA\n";
                numeric = false;
            }

            if (numeric == true) {
                if (matcher.matches()) {
                    Double gpanum = Double.valueOf(gpa.getText().toString());
                    if (gpanum > 4.33) {
                        nine = "GPA cannot exceed 4.33\n";
                        gpagreater = false;
                    } else if (gpanum <= 1.00) {
                        nine = "Please consider dropping out of school\n";
                        gpagreater = false;
                    }
                } else {
                    eight = "Invalid GPA format (0.00)\n";
                    gpaformat = false;
                }


            }

        }

        if (!passwordOne.isEmpty() || !passwordTwo.isEmpty()) {
            //check if they match
            if ((!passwordTwo.equals(passwordOne)) || (!passwordOne.equals(passwordTwo))) {

                ten = "Passwords do not match\n";
                passMatch = false;
            } else {

                ten = "";
                passMatch = true;
            }

            if (passwordTwo.length() <= 5 || passwordTwo.length() <= 5) {

                eleven = "Min password 6 chars\n";
                passLessThanCheck = false;
            } else {
                eleven = "";
            }
        }

        if (provinceCheck == false || schoolCheck == false || studyCheck == false || localityCheck == false || aboriginalityCheck == false ||
                gpaCheck == false || numeric == false || gpaformat == false || gpagreater == false || passMatch == false || passLessThanCheck == false)
        {
            error = four + five + one + two + three + six + seven + eight + nine + ten + eleven;
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            findViewById(R.id.profileScreen).startAnimation(shake);
            Toast.makeText(ProfileActivity.this, error, Toast.LENGTH_SHORT).show();
            passwordOneEditText.getText().clear();
            passwordTwoEditEdit.getText().clear();
        }
        else{
               profileInfo.setProfileSet(1);

            setProfileValues();

            db.UpdateAccount(profileInfo);
               Toast.makeText(ProfileActivity.this, "Account Updated Successfully", Toast.LENGTH_SHORT).show();
        }
    }

    public void setProfileValues() {
        fulltimeRdBtn = (RadioButton)findViewById(R.id.fullTimeRdBtnProfile);
        domesticRdBtn = (RadioButton)findViewById(R.id.domesticRdBtnProfile);
        abotiginalRdBtn = (RadioButton)findViewById(R.id.yesRdBtnProfile);

        // full time or part time student radio buttons
        if(fulltimeRdBtn.isChecked()) {
            study = "1";
        }
        else  {
            study = "0";
        }

        // domestic or international radio buttons
        if(domesticRdBtn.isChecked()) {
            locality = "1";
        }
        else {
            locality = "0";
        }

        // aboriginal or not radio buttons
        if(abotiginalRdBtn.isChecked()) {
            aboriginality = "1";
        }
        else  {
            aboriginality = "0";
        }

        provinceInt = Integer.toString(province.getSelectedItemPosition());
        schoolInt = Integer.toString(school.getSelectedItemPosition() -1);

       Double gpaNum = Double.parseDouble( gpa.getText().toString());

        if(passMatch == true && !passwordOne.isEmpty() || !passwordTwo.isEmpty())
            profileInfo.setPassword(passwordOne);
        profileInfo.setProvince(provinceInt);
        profileInfo.setSchool(schoolInt);
        profileInfo.setStudy(study);
        profileInfo.setLocality(locality);
        profileInfo.setAboriginality(aboriginality);
        profileInfo.setGpa(gpaNum);
    }
}
