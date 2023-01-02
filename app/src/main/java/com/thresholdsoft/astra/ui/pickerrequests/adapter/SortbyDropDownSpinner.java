package com.thresholdsoft.astra.ui.pickerrequests.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.thresholdsoft.astra.R;

import java.util.List;

public class SortbyDropDownSpinner extends BaseAdapter {
    Activity activity;
    List<String> sortbyDropDownList;
    LayoutInflater inflter;

    public SortbyDropDownSpinner(Activity activity, List<String> sortbyDropDownList) {
        this.activity = activity;
        this.sortbyDropDownList = sortbyDropDownList;
        inflter = (LayoutInflater.from(activity));
    }


    @Override
    public int getCount() {
        return sortbyDropDownList.size();
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
        dropDownItemText.setText(sortbyDropDownList.get(position));
        return view;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflter.inflate(R.layout.view_custom_spinner_selected_item_text, null);
        TextView selectedStatusText = view.findViewById(R.id.selected_status_id);
        selectedStatusText.setText(sortbyDropDownList.get(position));
        return view;
    }
}
