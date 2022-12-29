package com.thresholdsoft.astra.ui.pickerrequests.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.ui.picklist.model.GetWithHoldRemarksResponse;

import java.util.List;

public class RequestTypeDropdownSpinner extends BaseAdapter {
    Activity activity;
    List<GetWithHoldRemarksResponse.Remarksdetail> remarksdetails;
    LayoutInflater inflter;

    public RequestTypeDropdownSpinner(Activity activity, List<GetWithHoldRemarksResponse.Remarksdetail> remarksdetails) {
        this.activity = activity;
        this.remarksdetails = remarksdetails;
        inflter = (LayoutInflater.from(activity));
    }


    @Override
    public int getCount() {
        return remarksdetails.size();
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
        dropDownItemText.setText(remarksdetails.get(position).getRemarksdesc());
        return view;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflter.inflate(R.layout.view_custom_spinner_selected_item_text, null);
        TextView selectedStatusText = view.findViewById(R.id.selected_status_id);
        selectedStatusText.setText(remarksdetails.get(position).getRemarksdesc());
        return view;
    }
}