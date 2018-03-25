package com.example.maxi.unifundme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
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

public class SavedAwardsActivity extends AppCompatActivity {

    private ArrayList<Award> savedAwards = new ArrayList<>();
    private ListView awardsListView;
    private ArrayAdapter arrayAdapter;
    private DatabaseManager db;
    private String userName;
    private User userAccount;
    private Button deleteBtn;
    private ArrayList<Award> currentSelectedAwards = new ArrayList<>();
    private TextView savedAwardCount;
    private TextView sourceCol;
    private TextView typeCol;
    private TextView nameCol;
    private TextView amountCol;
    private boolean sourceClicked = false;
    private boolean typeClicked = false;
    private boolean nameClicked = false;
    private boolean amountClicked = false;
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

        setContentView(R.layout.activity_saved_awards);

        deleteBtn = (Button)findViewById(R.id.deleteAwardsBtn);

        db = new DatabaseManager(this);

        userAccount = db.getUserInfo(new String[] {userName});


        //savedAwards.clear();

        savedAwards.addAll(db.getSavedAwards(new String[] { userName }));

        savedAwardCount = (TextView)findViewById(R.id.savedAwardsFoundTxtView);

        savedAwardCount.setText(String.valueOf(savedAwards.size()));

        awardsListView = (ListView)findViewById(R.id.lvSaved);

        arrayAdapter = new AwardAdapter(SavedAwardsActivity.this,savedAwards);
        awardsListView.setAdapter(arrayAdapter);


        deleteBtn.setOnClickListener(view -> {
            GetCurrentSelectedItems();
            deleteBtn.setEnabled(false);
            new Handler().postDelayed(() -> deleteBtn.setEnabled(true),5000);
        });

        sourceCol = findViewById(R.id.savedColumnSource);
        typeCol = findViewById(R.id.savedColumnType);
        nameCol = findViewById(R.id.savedColumnName);
        amountCol = findViewById(R.id.savedColumnAmount);

        sourceCol.setOnClickListener(v -> {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                if (sourceClicked == false) {
                    savedAwards.sort((a1, a2) -> a1.getSource().compareTo(a2.getSource()));
                    awardsListView.setAdapter(arrayAdapter);
                    Toast.makeText(SavedAwardsActivity.this, "Source Ascending", Toast.LENGTH_SHORT).show();
                    sourceClicked = true;
                } else if (sourceClicked == true) {
                    Collections.reverse(savedAwards);

                    awardsListView.setAdapter(arrayAdapter);
                    Toast.makeText(SavedAwardsActivity.this, "Source Descending", Toast.LENGTH_SHORT).show();
                    sourceClicked = false;
                }
            }
        });

        typeCol.setOnClickListener(v -> {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                if (typeClicked == false) {
                    savedAwards.sort((a1, a2) -> a1.getType().compareTo(a2.getType()));
                    awardsListView.setAdapter(arrayAdapter);
                    Toast.makeText(SavedAwardsActivity.this, "Type Ascending", Toast.LENGTH_SHORT).show();
                    typeClicked = true;
                } else if (typeClicked == true) {
                    Collections.reverse(savedAwards);

                    awardsListView.setAdapter(arrayAdapter);
                    Toast.makeText(SavedAwardsActivity.this, "Type Descending", Toast.LENGTH_SHORT).show();
                    typeClicked = false;
                }
            }
        });

        nameCol.setOnClickListener(v -> {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                if (nameClicked == false) {
                    savedAwards.sort((a1, a2) -> a1.getName().compareTo(a2.getName()));
                    awardsListView.setAdapter(arrayAdapter);
                    Toast.makeText(SavedAwardsActivity.this, "Name Ascending", Toast.LENGTH_SHORT).show();
                    nameClicked = true;
                } else if (nameClicked == true) {
                    Collections.reverse(savedAwards);

                    awardsListView.setAdapter(arrayAdapter);
                    Toast.makeText(SavedAwardsActivity.this, "Name Descending", Toast.LENGTH_SHORT).show();
                    nameClicked = false;
                }
            }
        });

        amountCol.setOnClickListener(v -> {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                if (amountClicked == false) {
                    savedAwards.sort((p1, p2) -> p1.getAmount().compareTo(p2.getAmount()));
                    awardsListView.setAdapter(arrayAdapter);
                    Toast.makeText(SavedAwardsActivity.this, "Amount Ascending", Toast.LENGTH_SHORT).show();
                    amountClicked = true;
                } else if (amountClicked == true) {
                    Collections.reverse(savedAwards);
                    awardsListView.setAdapter(arrayAdapter);
                    Toast.makeText(SavedAwardsActivity.this, "Amount Descending", Toast.LENGTH_SHORT).show();
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

        switch(item.getItemId()) {
            case R.id.profileItem:
                startActivity(new Intent(this, ProfileActivity.class));
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

    public void GetCurrentSelectedItems() {
        SparseBooleanArray checkedAwards = awardsListView.getCheckedItemPositions();
        if (checkedAwards.size() >= 1) {
            for (int i = 0; i < checkedAwards.size(); ++i) {
                if (checkedAwards.valueAt(i)) {
                    int pos = checkedAwards.keyAt(i);
                    //doSomethingWith(adapter.getItem(pos));

                    String selectedIndex = savedAwards.get(pos).getId().toString();
                    db.DeleteSavedAward(new String[]{selectedIndex, userName});
                }

            }
            Toast.makeText(SavedAwardsActivity.this, "Removed: " + Integer.toString(checkedAwards.size()) + " award(s)", Toast.LENGTH_SHORT).show();
            savedAwards.clear();
            savedAwards.addAll(db.getSavedAwards(new String[]{userName}));
            arrayAdapter.notifyDataSetChanged();
            savedAwardCount.setText(String.valueOf(savedAwards.size()));
            awardsListView.setAdapter(arrayAdapter);
        }
        else {
            // do nothing (or shake)
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            findViewById(R.id.viewSavedDataScreen).startAnimation(shake);
        }
    }
}
