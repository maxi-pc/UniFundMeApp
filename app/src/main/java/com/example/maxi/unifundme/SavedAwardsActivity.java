package com.example.maxi.unifundme;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;

public class SavedAwardsActivity extends BaseActivity {

    private ArrayList<Award> savedAwards = new ArrayList<>();
    private ListView awardsListView;
    private ArrayAdapter arrayAdapter;
    private Button deleteBtn;
    private TextView savedAwardCount;
    private TextView sourceCol;
    private TextView typeCol;
    private TextView nameCol;
    private TextView amountCol;
    private boolean sourceClicked = false;
    private boolean typeClicked = false;
    private boolean nameClicked = false;
    private boolean amountClicked = false;

    @SuppressLint("MissingSuperCall")
    protected final void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState, R.layout.activity_saved_awards);

        deleteBtn = (Button)findViewById(R.id.deleteAwardsBtn);

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
