package com.thresholdsoft.astra.ui.barcode.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.math.RoundingMode;
import java.text.DecimalFormat;
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
        if (holder.barcodeAdapterlayoutBinding.qty.getText().toString().isEmpty()) {

        } else {
            barcodedatumList.get(position).setQty(Integer.parseInt(holder.barcodeAdapterlayoutBinding.qty.getText().toString()));
        }

//        holder.barcodeAdapterlayoutBinding.qty.setImeOptions(EditorInfo.IME_ACTION_DONE);
//        holder.barcodeAdapterlayoutBinding.qty.setImeOptions(EditorInfo.IME_ACTION_NEXT);

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

        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        holder.barcodeAdapterlayoutBinding.qoh.setText(decimalFormat.format(barcodedatum.getOnhandqty()).toString());
        holder.barcodeAdapterlayoutBinding.batch.setText(barcodedatum.getBatch());
        holder.barcodeAdapterlayoutBinding.barcode.setText(barcodedatum.getBarcode());
        holder.barcodeAdapterlayoutBinding.itemId.setText(barcodedatum.getItemid());
        holder.barcodeAdapterlayoutBinding.itemName.setText(barcodedatum.getItemname());
        holder.barcodeAdapterlayoutBinding.manufacture.setText(barcodedatum.getManufacturername());
        holder.barcodeAdapterlayoutBinding.packSize.setText(String.valueOf(barcodedatum.getPacksize()));
        holder.barcodeAdapterlayoutBinding.sitename.setText(barcodedatum.getExpdate());

        holder.barcodeAdapterlayoutBinding.qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                System.out.println("onTextChanged" + position);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                System.out.println("afterTextChanged" + position);
                if (editable.toString() != null) {
                    if (editable.toString().isEmpty()) {
                        barcodedatumList.get(position).setQty(0);
                    } else {
                        barcodedatumList.get(position).setQty(Integer.parseInt(holder.barcodeAdapterlayoutBinding.qty.getText().toString()));
                    }
//                    holder.barcodeAdapterlayoutBinding.qty.clearFocus();
                    barCodeActivityCallback.onNotify();
                } else {
                    return;
                }
            }
        });

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
