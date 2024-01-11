package com.thresholdsoft.astra.ui.changeuser.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.AdapterSuggestedUsersBinding;
import com.thresholdsoft.astra.databinding.DialogChangeUserBinding;
import com.thresholdsoft.astra.ui.changeuser.ChangeUserCallback;
import com.thresholdsoft.astra.ui.changeuser.model.ChangeUserResponse;

import java.util.List;

public class SuggestedUsersAdapter extends RecyclerView.Adapter<SuggestedUsersAdapter.ViewHolder> {
    private Context mContext;
    private ChangeUserCallback mCallback;
    private List<ChangeUserResponse.Suggesteduserlist> suggesteduserlist;
    private DialogChangeUserBinding dialogChangeUserBinding;
    private Dialog suggestedUserDialog;

    public SuggestedUsersAdapter(Context mContext, ChangeUserCallback mCallback, List<ChangeUserResponse.Suggesteduserlist> suggesteduserlist, DialogChangeUserBinding dialogChangeUserBinding, Dialog suggestedUserDialog) {
        this.mContext = mContext;
        this.mCallback = mCallback;
        this.suggesteduserlist = suggesteduserlist;
        this.dialogChangeUserBinding = dialogChangeUserBinding;
        this.suggestedUserDialog = suggestedUserDialog;
    }

    @NonNull
    @Override
    public SuggestedUsersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterSuggestedUsersBinding adapterSuggestedUsersBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.adapter_suggested_users, parent, false);
        return new SuggestedUsersAdapter.ViewHolder(adapterSuggestedUsersBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestedUsersAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.adapterSuggestedUsersBinding.setSuggestedUser(suggesteduserlist.get(position));
        holder.adapterSuggestedUsersBinding.setSuggestedUserDialog(suggestedUserDialog);
        holder.adapterSuggestedUsersBinding.setCallback(mCallback);
        holder.itemView.setOnClickListener(v -> {
            if (mCallback != null){
                mCallback.onSelectedSuggestedUser(suggesteduserlist.get(position), suggestedUserDialog, dialogChangeUserBinding);
            }
        });
    }

    @Override
    public int getItemCount() {
        return suggesteduserlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AdapterSuggestedUsersBinding adapterSuggestedUsersBinding;

        public ViewHolder(@NonNull AdapterSuggestedUsersBinding adapterSuggestedUsersBinding) {
            super(adapterSuggestedUsersBinding.getRoot());
            this.adapterSuggestedUsersBinding = adapterSuggestedUsersBinding;
        }
    }
}
