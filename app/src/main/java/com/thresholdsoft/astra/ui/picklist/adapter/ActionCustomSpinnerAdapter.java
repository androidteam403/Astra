package com.thresholdsoft.astra.ui.picklist.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.thresholdsoft.astra.R;

public class ActionCustomSpinnerAdapter extends BaseAdapter {
    String[] statusList;
    LayoutInflater inflter;
    Activity activity;

    public ActionCustomSpinnerAdapter(Activity activity, String[] statusList) {
        this.activity=activity;
        this.statusList = statusList;
        inflter = (LayoutInflater.from(activity));

    }


    @Override
    public int getCount() {
        return statusList.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = inflter.inflate(R.layout.view_custom_spinner, null);
        TextView dropDownItemText = view.findViewById(R.id.status_id);
        dropDownItemText.setText(statusList[position]);
        return view;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflter.inflate(R.layout.view_custom_spinner_selected_item, null);
        TextView selectedStatusText = view.findViewById(R.id.bulk_selected_status);
        selectedStatusText.setText(statusList[position]);
        return view;
    }
}
