package com.thresholdsoft.astra.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.thresholdsoft.astra.ui.picklist.PickListActivityCallback;

public class NetworkChangeReceiver extends BroadcastReceiver {
    private PickListActivityCallback mCallback;

    public void setmCallback(PickListActivityCallback mCallback) {
        this.mCallback = mCallback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (isOnline(context)) {
                Toast.makeText(context, "Online", Toast.LENGTH_SHORT).show();
                if (mCallback != null) {
                    mCallback.onNetworkStateChange();
                }
            }else {
                Toast.makeText(context, "Offline", Toast.LENGTH_SHORT).show();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private boolean isOnline(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            //should check null because in airplane mode it will be null
            return (netInfo != null && netInfo.isConnected());
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }
}
