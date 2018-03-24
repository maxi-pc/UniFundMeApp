package com.example.maxi.unifundme;

import android.content.Context;
import android.icu.text.DateFormat;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewsAdapter extends ArrayAdapter<News> {

    private ArrayList<News> objects;
    private Context context;

    public NewsAdapter(Context context, ArrayList<News> objects) {
        super(context, R.layout.activity_main, objects);
        this.objects = objects;
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Create inflater
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Get rowView from inflater
        View rowView = inflater.inflate(R.layout.news_layout_item, parent, false);

        // Get the two text view from the rowView
        TextView newsTitle = rowView.findViewById(R.id.newsTitleTextView);
        TextView newsContent = rowView.findViewById(R.id.newsContentTextView);
        TextView newsDate = rowView.findViewById(R.id.newsDateTextView);
     //   TextView column4 = rowView.findViewById(R.id.column4);

        RelativeLayout item = rowView.findViewById(R.id.itemNews);

        // Set the text for textView
        newsTitle.setText(objects.get(position).getTitle());
        newsContent.setText(objects.get(position).getContent());

       // DateFormat.getDateInstance(DateFormat.LONG).format(objects.get(position).getDate());
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");

        SimpleDateFormat formatterOut = new SimpleDateFormat("dd-MMM-yyyy");

        try {
            Date date = formatter.parse(objects.get(position).getDate());
            newsDate.setText(formatterOut.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return rowView;
    }
}