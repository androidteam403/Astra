package com.thresholdsoft.astra.ui.picklist.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.ui.barcode.BarCodeActivityCallback;
import com.thresholdsoft.astra.ui.picklist.PickListActivityCallback;

public class ShippingLabelAdapter extends BaseAdapter {
    Context context;
    String[] statusList;
    LayoutInflater inflter;
    private BarCodeActivityCallback mListener;

    public ShippingLabelAdapter(Context context, String[] statusList, BarCodeActivityCallback mListener) {
        this.context = context;
        this.statusList = statusList;
        inflter = (LayoutInflater.from(context));
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
        View view = inflter.inflate(R.layout.view_barcode_spinner_selected_item_text, null);
        TextView selectedStatusText = view.findViewById(R.id.selected_barcode_id);
        selectedStatusText.setText(statusList[position]);
        return view;
    }
}
