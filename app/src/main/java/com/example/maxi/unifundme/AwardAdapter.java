package com.example.maxi.unifundme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class AwardAdapter extends ArrayAdapter<Award> {

    private ArrayList<Award> objects;
    private Context context;

    public AwardAdapter(Context context, ArrayList<Award> objects) {
        super(context, R.layout.activity_view_data, objects);
        this.objects = objects;
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Create inflater
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Get rowView from inflater
        View rowView = inflater.inflate(R.layout.award_list_item, parent, false);

        // Get the two text view from the rowView
        TextView column1 = rowView.findViewById(R.id.column1);
        TextView column2 = rowView.findViewById(R.id.column2);
        TextView column3 = rowView.findViewById(R.id.column3);
        TextView column4 = rowView.findViewById(R.id.column4);

        LinearLayout item = rowView.findViewById(R.id.item);

        // Set the text for textView
        column1.setText(objects.get(position).getSource());
        column2.setText(objects.get(position).getType());
        column3.setText(objects.get(position).getName());
        DecimalFormat myFormat = new DecimalFormat("$###,###.00");
        column4.setText(myFormat.format(objects.get(position).getAmount()));

        // return rowView
        return rowView;
    }
}