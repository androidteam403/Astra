package com.thresholdsoft.astra.ui.barcode.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.BarcodeAdapterlayoutBinding;
import com.thresholdsoft.astra.databinding.DialogCustomAlertBinding;
import com.thresholdsoft.astra.ui.barcode.BarCodeActivityCallback;
import com.thresholdsoft.astra.ui.barcode.GetBarCodeResponse;

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
    public void onBindViewHolder(@NonNull BarCodeLabelAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
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
                    if (holder.barcodeAdapterlayoutBinding.qty.getText().toString() != null && !holder.barcodeAdapterlayoutBinding.qty.getText().toString().isEmpty()) {
                        barcodedatumList.get(position).setQty(Integer.parseInt(holder.barcodeAdapterlayoutBinding.qty.getText().toString()));
                    } else {
                        barcodedatumList.get(position).setQty(0);
                    }
                    holder.barcodeAdapterlayoutBinding.qty.clearFocus();
                }
                return false;
            }
        });

        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        holder.barcodeAdapterlayoutBinding.qoh.setText(String.valueOf(barcodedatum.getOnhandqty()));
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
                        if (barcodedatumList.get(position).getOnhandqty() >= Integer.valueOf(holder.barcodeAdapterlayoutBinding.qty.getText().toString())) {
                            barcodedatumList.get(position).setQty(Integer.parseInt(holder.barcodeAdapterlayoutBinding.qty.getText().toString()));
                        } else {
                            showAlertMessage("Qty should not be greater than QOH.");
                            barcodedatumList.get(position).setQty(0);
                            holder.barcodeAdapterlayoutBinding.qty.setText("");
                        }
                    }
//                    holder.barcodeAdapterlayoutBinding.qty.clearFocus();
                    barCodeActivityCallback.onNotify();
                } else {
                    return;
                }
            }
        });

    }

    private void showAlertMessage(String message) {
        Dialog customDialog = new Dialog(mContext);
        DialogCustomAlertBinding dialogCustomAlertBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dialog_custom_alert, null, false);
        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customDialog.setContentView(dialogCustomAlertBinding.getRoot());
        customDialog.setCancelable(false);
        dialogCustomAlertBinding.message.setText(message);
        dialogCustomAlertBinding.alertListenerLayout.setVisibility(View.GONE);
        dialogCustomAlertBinding.okBtn.setOnClickListener(v -> customDialog.dismiss());
        customDialog.show();
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
