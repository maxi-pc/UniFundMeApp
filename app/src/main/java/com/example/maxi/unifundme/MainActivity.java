package com.example.maxi.unifundme;



import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    private TextView welcome;
    private TextView profileMessage;
    private ArrayList<News> news = new ArrayList<>();
    private ListView newsListView;
    private ArrayAdapter arrayAdapter;


    @SuppressLint("MissingSuperCall")
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_main);

        profileMessage = (TextView) findViewById(R.id.profileAlertMessage);
        welcome = (TextView) findViewById(R.id.welcome);
        welcome.append(" " + userName);

        news.addAll(db.getAllNews());
        newsListView = (ListView) findViewById(R.id.newsListView);

        arrayAdapter = new NewsAdapter(MainActivity.this, news);
        newsListView.setAdapter(arrayAdapter);

        if (userAccount.getProfileSet() == 0) {
            profileMessage.setText("Please update your profile");
        }

        profileMessage.setOnClickListener(view -> profileMessage.setText(""));
    }

}
