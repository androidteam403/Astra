package com.thresholdsoft.astra.ui.logout.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.astra.R;
import com.thresholdsoft.astra.databinding.LoadingProgressbarBinding;
import com.thresholdsoft.astra.databinding.LogoutUsersAdapterlayoutBinding;
import com.thresholdsoft.astra.db.SessionManager;
import com.thresholdsoft.astra.ui.logout.LogOutActivityCallback;
import com.thresholdsoft.astra.ui.logout.model.LoginDetailsResponse;
import com.thresholdsoft.astra.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class LogOutUsersListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private Context mContext;
    private List<LoginDetailsResponse.Logindetail> logOutUsersList;
    LogOutActivityCallback mCallBack;
    public String role;
    private List<LoginDetailsResponse.Logindetail> logOutUsersListSecond = new ArrayList<>();
    private List<LoginDetailsResponse.Logindetail> logOutUsersFilterList = new ArrayList<>();

    public String dcName;
    public String empName;
    String charString;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public LogOutUsersListAdapter(Context mContext, List<LoginDetailsResponse.Logindetail> logOutUsersList, LogOutActivityCallback logOutActivityCallback, String role, String dcName, String empName) {
        this.mContext = mContext;
        this.logOutUsersList = logOutUsersList;
        this.logOutUsersListSecond = logOutUsersList;
        this.mCallBack = logOutActivityCallback;
        this.dcName = dcName;
        this.role = role;
        this.empName = empName;

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            LogoutUsersAdapterlayoutBinding logoutUsersAdapterlayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.logout_users_adapterlayout, parent, false);
            return new ViewHolder(logoutUsersAdapterlayoutBinding);
        } else {
            LoadingProgressbarBinding loadingProgressbarBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.loading_progressbar, parent, false);
            return new LoadingViewHolder(loadingProgressbarBinding);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (holder instanceof LogOutUsersListAdapter.ViewHolder) {
            onBindViewHolderItem((ViewHolder) holder, position);
        }
    }

    private void onBindViewHolderItem(LogOutUsersListAdapter.ViewHolder holder, int position) {
        LoginDetailsResponse.Logindetail items = logOutUsersList.get(position);
        holder.logoutUsersAdapterlayoutBinding.empid.setText(items.getUserid());
        holder.logoutUsersAdapterlayoutBinding.role.setText(items.getRole());
        holder.logoutUsersAdapterlayoutBinding.dcname.setText(dcName);
        holder.logoutUsersAdapterlayoutBinding.name.setText(items.getName());

        holder.logoutUsersAdapterlayoutBinding.logindate.setText(Utils.getLoginLogoutDate(items.getLogindatetime()));
        holder.logoutUsersAdapterlayoutBinding.logoutdate.setText(Utils.getLoginLogoutDate(items.getLogoutdatetime()));
        holder.logoutUsersAdapterlayoutBinding.logout.setOnClickListener(view -> mCallBack.onClickAction(items, position));
        String empId = new SessionManager(mContext).getEmplId();
        if (items.getIsactive() && !items.getUserid().equalsIgnoreCase(empId)) {
            holder.logoutUsersAdapterlayoutBinding.logout.setBackgroundResource(R.color.yellow);
            holder.logoutUsersAdapterlayoutBinding.logout.setEnabled(true);
            holder.logoutUsersAdapterlayoutBinding.logout.setTextColor(mContext.getResources().getColor(R.color.black));
        } else {
            holder.logoutUsersAdapterlayoutBinding.logout.setBackgroundResource(R.color.grey);
            holder.logoutUsersAdapterlayoutBinding.logout.setEnabled(false);
            holder.logoutUsersAdapterlayoutBinding.logout.setTextColor(mContext.getResources().getColor(R.color.customer_header_text));
        }
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

    @Override
    public int getItemViewType(int position) {
        return logOutUsersList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LogoutUsersAdapterlayoutBinding logoutUsersAdapterlayoutBinding;

        public ViewHolder(@NonNull LogoutUsersAdapterlayoutBinding logoutUsersAdapterlayoutBinding) {
            super(logoutUsersAdapterlayoutBinding.getRoot());
            this.logoutUsersAdapterlayoutBinding = logoutUsersAdapterlayoutBinding;
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        LoadingProgressbarBinding loadingProgressbarBinding;

        public LoadingViewHolder(@NonNull LoadingProgressbarBinding loadingProgressbarBinding) {
            super(loadingProgressbarBinding.getRoot());
            this.loadingProgressbarBinding = loadingProgressbarBinding;
        }
    }
}


