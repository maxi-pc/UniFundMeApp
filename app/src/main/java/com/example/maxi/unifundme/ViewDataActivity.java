package com.example.maxi.unifundme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Collections;

public class ViewDataActivity extends AppCompatActivity {

    private ArrayAdapter arrayAdapter;
    private ArrayList<AwardList> awards = new ArrayList<>();
    private ArrayList<AwardList> savedAwards =  new ArrayList<>();
    private TextView sourceCol;
    private TextView typeCol;
    private TextView nameCol;
    private TextView amountCol;
   private boolean sourceClicked = false;
   private boolean typeClicked = false;
   private boolean nameClicked = false;
   private boolean amountClicked = false;
   private String[] myQueryStrings = new String[6];
   private String[] myQueryStringsAnySchool = new String[5];
   private String[] myQueryUserName = new String[1];
   private ListView awardsListView;
   private Button saveBtn;
   private User currentUser;
   private TextView awardsFound;
   private String provinceInt;
   private String schoolInt;
   private String study;
   private String locality;
   private String aboriginality;
   private String gpaVal;
   private String searchType;
   private DatabaseManager db;
   private String userName;

   // private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data);
        db = new DatabaseManager(this);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        userName = prefs.getString("LoggedUserName", "");

        savedAwards = db.getSavedAwards(new String[]{userName});

        currentUser = db.getUserInfo(new String[] {userName});

       Intent intent = getIntent();

       String[] checkNull = intent.getStringArrayExtra("queryStrings");

       if(checkNull != null);
            myQueryStrings = checkNull;

       searchType = intent.getStringExtra("searchType");

       awardsFound = (TextView)findViewById(R.id.awardsFoundTxtView);

        awardsListView = findViewById(R.id.lv);
        awardsListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        saveBtn = (Button)findViewById(R.id.saveAwardsBtn);



        arrayAdapter = new AwardListAdapter(ViewDataActivity.this,awards);
        awardsListView.setAdapter(arrayAdapter);

        // debugging  sent data
       //Toast.makeText(ViewDataActivity.this, myQueryStrings[0] + " " + myQueryStrings[1] + " " + myQueryStrings[2] + " " + myQueryStrings[3] + " " + myQueryStrings[4] + " " + myQueryStrings[5], Toast.LENGTH_LONG).show();

        if(searchType.equals("manual")) {
            searchManual();
        }

        else if(searchType.equals("auto")) {
            searchAuto();
        }

        Toast.makeText(ViewDataActivity.this, searchType, Toast.LENGTH_SHORT).show();

       sourceCol = findViewById(R.id.columnSource);
       typeCol = findViewById(R.id.columnType);
       nameCol = findViewById(R.id.columnName);
       amountCol = findViewById(R.id.columnAmount);


       Button saveAwards = (Button)findViewById(R.id.saveAwardsBtn);

       saveAwards.setOnClickListener(view -> checkSavedAward());

       sourceCol.setOnClickListener(v -> {

          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

             if (sourceClicked == false) {
                awards.sort((a1, a2) -> a1.getSource().compareTo(a2.getSource()));
                awardsListView.setAdapter(arrayAdapter);
                Toast.makeText(ViewDataActivity.this, "Source Ascending", Toast.LENGTH_SHORT).show();
                sourceClicked = true;
             } else if (sourceClicked == true) {
                Collections.reverse(awards);

                awardsListView.setAdapter(arrayAdapter);
                Toast.makeText(ViewDataActivity.this, "Source Descending", Toast.LENGTH_SHORT).show();
                sourceClicked = false;
             }
          }
       });

       typeCol.setOnClickListener(v -> {

          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

             if (typeClicked == false) {
                awards.sort((a1, a2) -> a1.getType().compareTo(a2.getType()));
                awardsListView.setAdapter(arrayAdapter);
                 Toast.makeText(ViewDataActivity.this, "Type Ascending", Toast.LENGTH_SHORT).show();
                typeClicked = true;
             } else if (typeClicked == true) {
                Collections.reverse(awards);

                awardsListView.setAdapter(arrayAdapter);
                 Toast.makeText(ViewDataActivity.this, "Type Descending", Toast.LENGTH_SHORT).show();
                typeClicked = false;
             }
          }
       });

       nameCol.setOnClickListener(v -> {

          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

             if (nameClicked == false) {
                awards.sort((a1, a2) -> a1.getName().compareTo(a2.getName()));
                awardsListView.setAdapter(arrayAdapter);
                 Toast.makeText(ViewDataActivity.this, "Name Ascending", Toast.LENGTH_SHORT).show();
                nameClicked = true;
             } else if (nameClicked == true) {
                Collections.reverse(awards);

                awardsListView.setAdapter(arrayAdapter);
                 Toast.makeText(ViewDataActivity.this, "Name Descending", Toast.LENGTH_SHORT).show();
                nameClicked = false;
             }
          }
       });

       amountCol.setOnClickListener(v -> {

          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

             if (amountClicked == false) {
                awards.sort((p1, p2) -> p1.getAmount().compareTo(p2.getAmount()));
                awardsListView.setAdapter(arrayAdapter);
                 Toast.makeText(ViewDataActivity.this, "Amount Ascending", Toast.LENGTH_SHORT).show();
                amountClicked = true;
             } else if (amountClicked == true) {
                Collections.reverse(awards);
                awardsListView.setAdapter(arrayAdapter);
                 Toast.makeText(ViewDataActivity.this, "Amount Descending", Toast.LENGTH_SHORT).show();
                amountClicked = false;
             }
          }

       });
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

    private void CheckSavedAwards(){

    }

    private void searchManual() {
        int school = Integer.parseInt(myQueryStrings[1]);


        // if school type is at position of "Any School" then display anySchool data query;
        if (school == 0) {
            // clear previous awards
            awards.clear();
            // change userInput minute school
            myQueryStringsAnySchool[0] = myQueryStrings[0];
            myQueryStringsAnySchool[1] = myQueryStrings[2];
            myQueryStringsAnySchool[2] = myQueryStrings[3];
            myQueryStringsAnySchool[3] = myQueryStrings[4];
            myQueryStringsAnySchool[4] = myQueryStrings[5];

            // add all results from query method
            awards.addAll(db.getAnySchoolData(myQueryStringsAnySchool));
            // set the DataSetChanges
            arrayAdapter.notifyDataSetChanged();
            awardsFound.setText(Integer.toString(awards.size()));

        } else if (school >= 1) {
            awards.clear();
            awards.addAll(db.getDefaultData(myQueryStrings));
            arrayAdapter.notifyDataSetChanged();
            awardsFound.setText(Integer.toString(awards.size()));

        }
        arrayAdapter.notifyDataSetChanged();
        awardsFound.setText(Integer.toString(awards.size()));
    }

    private void searchAuto() {
        int school = Integer.parseInt(currentUser.getSchool());


        // if school type is at position of "Any School" then display anySchool data query;
        if (school == 0) {
            // clear previous awards
            awards.clear();
            // change userInput minute school
            myQueryStringsAnySchool[0] = currentUser.getProvince();
            myQueryStringsAnySchool[1] = currentUser.getStudy();
            myQueryStringsAnySchool[2] = currentUser.getLocality();
            myQueryStringsAnySchool[3] = currentUser.getAboriginality();
            myQueryStringsAnySchool[4] = currentUser.getGpa().toString();

            // add all results from query method
            awards.addAll(db.getAnySchoolData(myQueryStringsAnySchool));
            // set the DataSetChanges
            arrayAdapter.notifyDataSetChanged();
            awardsFound.setText(Integer.toString(awards.size()));

        } else if (school >= 1) {
            awards.clear();
            awards.addAll(db.getDefaultData(new String[] {currentUser.getProvince(), currentUser.getSchool(), currentUser.getStudy(), currentUser.getLocality(), currentUser.getAboriginality(), currentUser.getGpa().toString()}));
            arrayAdapter.notifyDataSetChanged();
            awardsFound.setText(Integer.toString(awards.size()));

        }
        arrayAdapter.notifyDataSetChanged();
        awardsFound.setText(Integer.toString(awards.size()));
    }

    public void checkSavedAward(){

        SparseBooleanArray checkedAwards = awardsListView.getCheckedItemPositions();

        if(checkedAwards.size() != 0) {
            for (int i = 0; i < checkedAwards.size(); ++i) {
                if (checkedAwards.valueAt(i)) {
                    int pos = checkedAwards.keyAt(i);
                    if(db.checkMatch("savedAwards", "award_id","user_name",new String[] {awards.get(pos).getId().toString(), userName})) {
                        db.CreateSavedAward(awards.get(pos), userName);
                    }
                    else {
                        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
                        findViewById(R.id.viewDataScreen).startAnimation(shake);
                        Toast.makeText(ViewDataActivity.this, "Some awards already in saved list", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
/*
        else if(checkedAwards == null) {
            // do nothing
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            findViewById(R.id.viewDataScreen).startAnimation(shake);
        }
        */

        else{
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            findViewById(R.id.viewDataScreen).startAnimation(shake);
        }
    }
}