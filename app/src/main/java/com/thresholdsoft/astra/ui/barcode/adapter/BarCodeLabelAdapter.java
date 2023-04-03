package com.thresholdsoft.astra.ui.barcode.adapter;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.BarcodeAdapterlayoutBinding;
import com.thresholdsoft.astra.ui.barcode.BarCodeActivityCallback;
import com.thresholdsoft.astra.ui.barcode.GetBarCodeResponse;

import java.util.List;

public class BarCodeLabelAdapter extends RecyclerView.Adapter<BarCodeLabelAdapter.ViewHolder> {

    private Context mContext;
    private List<GetBarCodeResponse.Barcodedatum> barcodedatumList;
    private BarCodeActivityCallback barCodeActivityCallback;


    public BarCodeLabelAdapter(Context mContext, List<GetBarCodeResponse.Barcodedatum> barcodedatumList, BarCodeActivityCallback barCodeActivityCallback) {
        this.mContext = mContext;
        this.barcodedatumList = barcodedatumList;
        this.barCodeActivityCallback = barCodeActivityCallback;

    }


    @NonNull
    @Override
    public BarCodeLabelAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BarcodeAdapterlayoutBinding barcodeAdapterlayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.barcode_adapterlayout, parent, false);
        return new BarCodeLabelAdapter.ViewHolder(barcodeAdapterlayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull BarCodeLabelAdapter.ViewHolder holder, int position) {
        GetBarCodeResponse.Barcodedatum barcodedatum = barcodedatumList.get(position);
        barcodedatumList.get(position).setQty(Integer.parseInt(holder.barcodeAdapterlayoutBinding.qty.getText().toString()));


        holder.barcodeAdapterlayoutBinding.qty.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    barcodedatumList.get(position).setQty(Integer.parseInt(holder.barcodeAdapterlayoutBinding.qty.getText().toString()));

                    holder.barcodeAdapterlayoutBinding.qty.clearFocus();
                }
                return false;
            }
        });


        holder.barcodeAdapterlayoutBinding.batch.setText(barcodedatum.getBatch());
        holder.barcodeAdapterlayoutBinding.barcode.setText(barcodedatum.getBarcode());
        holder.barcodeAdapterlayoutBinding.itemId.setText(barcodedatum.getItemid());
        holder.barcodeAdapterlayoutBinding.itemName.setText(barcodedatum.getItemname());
        holder.barcodeAdapterlayoutBinding.manufacture.setText(barcodedatum.getManufacturername());
        holder.barcodeAdapterlayoutBinding.packSize.setText(String.valueOf(barcodedatum.getPacksize()));
        holder.barcodeAdapterlayoutBinding.sitename.setText(barcodedatum.getExpdate());


    }

    @Override
    public int getItemCount() {
        return barcodedatumList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        BarcodeAdapterlayoutBinding barcodeAdapterlayoutBinding;

        public ViewHolder(@NonNull BarcodeAdapterlayoutBinding barcodeAdapterlayoutBinding) {
            super(barcodeAdapterlayoutBinding.getRoot());
            this.barcodeAdapterlayoutBinding = barcodeAdapterlayoutBinding;
        }
    }
}
