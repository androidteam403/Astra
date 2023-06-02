package com.thresholdsoft.astra.ui.logout.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.LogoutUsersAdapterlayoutBinding;
import com.thresholdsoft.astra.ui.logout.LogOutActivityCallback;
import com.thresholdsoft.astra.ui.logout.model.LoginDetailsResponse;
import com.thresholdsoft.astra.ui.picklist.model.GetAllocationDataResponse;
import com.thresholdsoft.astra.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class LogOutUsersListAdapter extends RecyclerView.Adapter<LogOutUsersListAdapter.ViewHolder> implements Filterable {
    private Context mContext;
    private List<LoginDetailsResponse.Logindetail> logOutUsersList;
    LogOutActivityCallback mCallBack;
    public  String role;
    private List<LoginDetailsResponse.Logindetail> logOutUsersListSecond=new ArrayList<>();

    private List<LoginDetailsResponse.Logindetail> logOutUsersFilterList=new ArrayList<>();

    public String dcName;
    public String empName;
    String charString;


    public LogOutUsersListAdapter(Context mContext, List<LoginDetailsResponse.Logindetail> logOutUsersList, LogOutActivityCallback logOutActivityCallback,String role,String dcName,String empName) {
        this.mContext = mContext;
        this.logOutUsersList = logOutUsersList;
        this.logOutUsersListSecond=logOutUsersList;
        this.mCallBack=logOutActivityCallback;
        this.dcName=dcName;
        this.role=role;
        this.empName=empName;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LogoutUsersAdapterlayoutBinding logoutUsersAdapterlayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.logout_users_adapterlayout, parent, false);
        return new ViewHolder(logoutUsersAdapterlayoutBinding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        LoginDetailsResponse.Logindetail items = logOutUsersList.get(position);
        holder.logoutUsersAdapterlayoutBinding.empid.setText(items.getUserid());
        holder.logoutUsersAdapterlayoutBinding.role.setText(role);
        holder.logoutUsersAdapterlayoutBinding.dcname.setText(dcName);
        holder.logoutUsersAdapterlayoutBinding.name.setText(empName);

        holder.logoutUsersAdapterlayoutBinding.logindate.setText(Utils.getDate(items.getLogindatetime()));
        holder.logoutUsersAdapterlayoutBinding.logoutdate.setText(Utils.getDate(items.getLogoutdatetime()));
        holder.logoutUsersAdapterlayoutBinding.logout.setOnClickListener(view -> mCallBack.onClickAction(items,position));


    }

    @Override
    public int getItemCount() {
        return logOutUsersList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                charString = charSequence.toString();
                if (charString.isEmpty()) {
                    logOutUsersList = logOutUsersListSecond;

                } else {
                    logOutUsersFilterList.clear();
                    for (LoginDetailsResponse.Logindetail row : logOutUsersListSecond) {
                        if (!logOutUsersFilterList.contains(row) && (row.getUserid()).toLowerCase().contains(charString.toLowerCase())) {
                            logOutUsersFilterList.add(row);

                        }

                    }
                    logOutUsersList = logOutUsersFilterList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = logOutUsersList;
                return filterResults;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (logOutUsersList != null && !logOutUsersList.isEmpty()) {
                    logOutUsersList = (ArrayList<LoginDetailsResponse.Logindetail>) filterResults.values;
                    try {

                        notifyDataSetChanged();

                    } catch (Exception e) {
                        Log.e("FullfilmentAdapter", e.getMessage());
                    }
                } else {

                    notifyDataSetChanged();
                }
            }
        };
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        LogoutUsersAdapterlayoutBinding logoutUsersAdapterlayoutBinding;

        public ViewHolder(@NonNull LogoutUsersAdapterlayoutBinding logoutUsersAdapterlayoutBinding) {
            super(logoutUsersAdapterlayoutBinding.getRoot());
            this.logoutUsersAdapterlayoutBinding = logoutUsersAdapterlayoutBinding;
        }
    }
}


