package com.thresholdsoft.astra.ui.pickerrequests.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.ui.pickerrequests.model.WithHoldDataResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetWithHoldRemarksResponse;

import java.util.List;

public class RouteTypeDropdownSpinner extends BaseAdapter {
    Activity activity;
    List<String> withholddetailList;
    LayoutInflater inflter;

    public RouteTypeDropdownSpinner(Activity activity, List<String> withholddetailList) {
        this.activity = activity;
        this.withholddetailList = withholddetailList;
        inflter = (LayoutInflater.from(activity));
    }


    @Override
    public int getCount() {
        return withholddetailList.size();
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
        dropDownItemText.setText(withholddetailList.get(position));
        return view;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflter.inflate(R.layout.view_custom_spinner_selected_item_text, null);
        TextView selectedStatusText = view.findViewById(R.id.selected_status_id);
        selectedStatusText.setText(withholddetailList.get(position));
        return view;
    }
}