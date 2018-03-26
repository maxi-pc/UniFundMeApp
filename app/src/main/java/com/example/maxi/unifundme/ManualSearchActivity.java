package com.example.maxi.unifundme;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ManualSearchActivity extends BaseActivity {

    private Spinner province;
    private Spinner school;
    private RadioGroup studies;
    private RadioGroup student;
    private RadioGroup aboriginal;
    private EditText gpa;
    private Button findAwards;
    // radio button values
    private String study;
    private String locality;
    private String aboriginality;
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

    @SuppressLint("MissingSuperCall")
    protected final void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState, R.layout.activity_manual_search);

        findAwards = (Button)findViewById(R.id.findAwardsBtn);

        findAwards.setOnClickListener(view -> {
            validationCheck();
        });
    }

    public void validationCheck() {


        province = (Spinner) findViewById(R.id.provinceSpinner);
        school = (Spinner) findViewById(R.id.schoolSpinner);
        studies = (RadioGroup)findViewById(R.id.studiesRdGrp);
        student = (RadioGroup)findViewById(R.id.domesticRdGrp);
        aboriginal = (RadioGroup)findViewById(R.id.aborignalRdGrp);
        gpa = (EditText)findViewById(R.id.gpaEditText);


        String one, two, three, four, five, six, seven ="", eight ="", nine = "", error;
        boolean numeric = true, gpaformat = true, gpagreater = true;
        String gpaStr = String.valueOf(gpa.getText());

        Pattern pattern = Pattern.compile("^\\d+\\.\\d{2}$");
        Matcher matcher = pattern.matcher(gpaStr);

        if (studies.getCheckedRadioButtonId() == -1) {
            one = "Studies Check\n";
        } else {
            one = "";
        }
        if (student.getCheckedRadioButtonId() == -1) {
            two = "Domestic Check\n";
        } else {
            two = "";
        }
        if (aboriginal.getCheckedRadioButtonId() == -1) {
            three = "Aboriginal Check\n";
        } else {
            three = "";
        }
        int posProvince = province.getSelectedItemPosition();
        if (posProvince == 0) {
            four = "Province Check\n";
        } else {
            four = "";
        }
        int posSchool = school.getSelectedItemPosition();
        if (posSchool == 0) {
            five = "School Check\n";
        } else {
            five = "";
        }
        if (gpa.getText().toString().trim().isEmpty()) {
            six = "GPA Check\n";
        } else {
            six = "";
        }
        if (!gpa.getText().toString().trim().isEmpty()) {
            seven = "";
            eight = "";

            try {
                Double.valueOf(gpa.getText().toString());
            } catch(Exception e) {
                seven = "Non-Numeric GPA\n";
                numeric = false;
            }

            if (numeric == true) {
                if (matcher.matches()) {
                    Double gpanum = Double.valueOf(gpa.getText().toString());
                    if (gpanum > 4.33) {
                        nine = "GPA cannot exceed 4.33\n";
                        gpagreater = false;
                    }
                    else if(gpanum <= 1.00) {
                        nine = "Please consider dropping out of school\n";
                        gpagreater = false;
                    }
                } else {
                    eight = "Invalid GPA format (0.00)\n";
                    gpaformat = false;
                }


            }

        }

        error = four + five + one + two + three + six + seven + eight + nine;
        if (studies.getCheckedRadioButtonId() == -1 || student.getCheckedRadioButtonId() == -1 ||
                aboriginal.getCheckedRadioButtonId() == -1 || posProvince == 0 ||
                posSchool == 0 || gpa.getText().toString().trim().isEmpty() || numeric == false || gpaformat == false || gpagreater == false) {

            Animation shake = AnimationUtils.loadAnimation(ManualSearchActivity.this, R.anim.shake);
            findViewById(R.id.manualScreen).startAnimation(shake);
            Toast.makeText(ManualSearchActivity.this, error, Toast.LENGTH_LONG).show();

        }
        else {
            setValuesOfSearch();
        }
    }


    public void setValuesOfSearch() {
        fulltimeRdBtn = (RadioButton)findViewById(R.id.fullTimeRdBtn);
        domesticRdBtn = (RadioButton)findViewById(R.id.domesticRdBtn);
        abotiginalRdBtn = (RadioButton)findViewById(R.id.yesRdBtn);

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
        schoolInt = Integer.toString(school.getSelectedItemPosition() - 1);

        gpaVal = gpa.getText().toString();

        queryStrings[0] = provinceInt;
        queryStrings[1] = schoolInt;
        queryStrings[2] = study;
        queryStrings[3] = locality;
        queryStrings[4] = aboriginality;
        queryStrings[5] = gpaVal;

        Intent myIntent = new Intent(ManualSearchActivity.this, ViewDataActivity.class);
        myIntent.putExtra("queryStrings",queryStrings);
        myIntent.putExtra("searchType", "manual");
        startActivity(myIntent);
    }



}
