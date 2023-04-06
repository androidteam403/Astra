package com.thresholdsoft.astra.ui.picklist.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.ui.barcode.BarCodeActivityCallback;

public class ShippingBatchNumberAdapter extends BaseAdapter {
    Activity activity;
    String[] statusList;
    LayoutInflater inflter;
    private BarCodeActivityCallback mListener;

    public ShippingBatchNumberAdapter(Activity activity, String[] statusList, BarCodeActivityCallback mListener) {
        this.activity = activity;
        this.statusList = statusList;
        inflter = (LayoutInflater.from(activity));
        this.mListener = mListener;
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
        View view = inflter.inflate(R.layout.view_barcode_spinner, null);
        TextView dropDownItemText = view.findViewById(R.id.barcode_id);
        dropDownItemText.setText(statusList[position]);
        return view;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflter.inflate(R.layout.view_batch_spinner_selected_item_text, null);
        TextView selectedStatusText = view.findViewById(R.id.select_batch_num);
        selectedStatusText.setText(statusList[position]);
        return view;
    }
}
