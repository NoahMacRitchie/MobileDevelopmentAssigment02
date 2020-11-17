package com.example.mobiledevelopmentassigment02;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class ReadingListAdapter extends ArrayAdapter<Reading> {
    private Activity context;
    private List<Reading> readingList;

    public ReadingListAdapter(Activity context, List<Reading> readingList) {
        super(context, R.layout.list_layout, readingList);
        this.context = context;
        this.readingList = readingList;
    }

    public ReadingListAdapter(Context context, int resource, List<Reading> objects, Activity context1, List<Reading> readingList) {
        super(context, resource, objects);
        this.context = context1;
        this.readingList = readingList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_layout, null, true);

        TextView tvName = listViewItem.findViewById(R.id.memeber);
        TextView tvSys = listViewItem.findViewById(R.id.text_sys);
        TextView tvDis = listViewItem.findViewById(R.id.text_dias);
        TextView time = listViewItem.findViewById(R.id.time);
        TextView condition = listViewItem.findViewById(R.id.con);

        Reading reading = readingList.get(position);
        tvName.setText(reading.getFamilyMember());
        String sys = context.getString(R.string.systolic_reading) + " " + String.valueOf(reading.getSystolicNum());
        tvSys.setText(sys);
        String dia = context.getString(R.string.diastolic_reading) + " " + String.valueOf(reading.getDiastolicNum());
        tvDis.setText(dia);
        String dateOfReading = context.getString(R.string.date) + reading.getCurrentDate();
        time.setText(dateOfReading);
        String cond = context.getString(R.string.condition) + reading.getCondition();
        condition.setText(cond);

        return listViewItem;
    }

}
