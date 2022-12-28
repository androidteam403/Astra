package com.thresholdsoft.astra.ui.picklist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.AdapterScannedBarcodeItemsBinding;
import com.thresholdsoft.astra.ui.picklist.PickListActivityCallback;
import com.thresholdsoft.astra.ui.picklist.model.GetAllocationLineResponse;

import java.util.List;

public class ScannedBacodeItemsAdapter extends RecyclerView.Adapter<ScannedBacodeItemsAdapter.ViewHolder> {
    private Context mContext;
    private List<GetAllocationLineResponse.Allocationdetail> allocationdetailList;
    private PickListActivityCallback mainActivityCallback;

    public ScannedBacodeItemsAdapter(Context mContext, List<GetAllocationLineResponse.Allocationdetail> allocationdetailList, PickListActivityCallback mainActivityCallback) {
        this.mContext = mContext;
        this.allocationdetailList = allocationdetailList;
        this.mainActivityCallback = mainActivityCallback;
    }

    @NonNull
    @Override
    public ScannedBacodeItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterScannedBarcodeItemsBinding scannedBarcodeItemsBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.adapter_scanned_barcode_items, parent, false);
        return new ViewHolder(scannedBarcodeItemsBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ScannedBacodeItemsAdapter.ViewHolder holder, int position) {
        holder.scannedBarcodeItemsBinding.setAllocationDetails(allocationdetailList.get(position));
        holder.scannedBarcodeItemsBinding.setCallback(mainActivityCallback);
    }

    @Override
    public int getItemCount() {
        return allocationdetailList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AdapterScannedBarcodeItemsBinding scannedBarcodeItemsBinding;

        public ViewHolder(@NonNull AdapterScannedBarcodeItemsBinding scannedBarcodeItemsBinding) {
            super(scannedBarcodeItemsBinding.getRoot());
            this.scannedBarcodeItemsBinding = scannedBarcodeItemsBinding;
        }
    }
}
