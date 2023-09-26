package com.thresholdsoft.astra.ui.services;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.thresholdsoft.astra.db.room.AppDatabase;
import com.thresholdsoft.astra.ui.picklist.PickListActivity;
import com.thresholdsoft.astra.ui.picklist.model.GetAllocationLineResponse;
import com.thresholdsoft.astra.ui.services.model.MrpBarcodeBulkUpdateResponse;

import org.json.JSONObject;

import java.util.List;
import java.util.stream.Collectors;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.e("newToken", s);
        getSharedPreferences("_", MODE_PRIVATE).edit().putString("fb", s).apply();
    }


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage != null && remoteMessage.getData() != null) {
            JSONObject object = new JSONObject(remoteMessage.getData());

            String mJsonString = object.toString();
            JsonParser parser = new JsonParser();
            JsonElement mJson = parser.parse(mJsonString);
            Gson gson = new Gson();
            MrpBarcodeBulkUpdateResponse mrpBarcodeBulkUpdateResponse = gson.fromJson(mJson, MrpBarcodeBulkUpdateResponse.class);
            mrpBarcodeBulkUpdate(mrpBarcodeBulkUpdateResponse);
        }
    }

    private void mrpBarcodeBulkUpdate(MrpBarcodeBulkUpdateResponse mrpBarcodeBulkUpdateResponse) {
        boolean isUpdated = false;
        GetAllocationLineResponse.Allocationdetail allocationdetailUpdate = null;
        List<GetAllocationLineResponse> getAllocationLineResponseFromDb = AppDatabase.getDatabaseInstance(this).dbDao().getAllAllocationLineList();
        for (GetAllocationLineResponse getAllocationLineResponse : getAllocationLineResponseFromDb) {
            List<GetAllocationLineResponse.Allocationdetail> allocationdetailList = getAllocationLineResponse.getAllocationdetails().stream().filter(u -> (u.getItemid().equalsIgnoreCase(mrpBarcodeBulkUpdateResponse.getItemid()) && u.getInventbatchid().equalsIgnoreCase(mrpBarcodeBulkUpdateResponse.getBatch()))).collect(Collectors.toList());
            if (allocationdetailList.size() > 0) {
                for (GetAllocationLineResponse.Allocationdetail allocationdetail : allocationdetailList) {
                    if ((allocationdetail.getAllocatedPackscompleted() - allocationdetail.getSupervisorApprovedQty()) != 0) {
                        if (mrpBarcodeBulkUpdateResponse.getRequesttype().equalsIgnoreCase("mrp")) {
                            allocationdetail.setMrp(Double.parseDouble(mrpBarcodeBulkUpdateResponse.getNewmrp()));
                            isUpdated = true;
                            allocationdetailUpdate = allocationdetail;
                        } else if (mrpBarcodeBulkUpdateResponse.getRequesttype().equalsIgnoreCase("barcode")) {
                            allocationdetail.setItembarcode(mrpBarcodeBulkUpdateResponse.getNewbarcode());
                            isUpdated = true;
                            allocationdetailUpdate = allocationdetail;
                        } else if (mrpBarcodeBulkUpdateResponse.getRequesttype().equalsIgnoreCase("isbulk")) {
                            allocationdetail.setIsbulkscanenable(mrpBarcodeBulkUpdateResponse.getIsbulk().equalsIgnoreCase("TRUE"));
                            isUpdated = true;
                            allocationdetailUpdate = allocationdetail;
                        }
                    }
                }
            }
            AppDatabase.getDatabaseInstance(this).dbDao().getAllocationLineUpdate(getAllocationLineResponse);
//            if (isUpdated) {
//                Intent intent = new Intent(this, PickListActivity.class);
//                intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
//                intent.putExtra("ITEM_UPDATED", "Items data has been updated.");
//                intent.putExtra("NOTIFICATION_DATA", allocationdetailUpdate);
//                intent.putExtra("NOTIFICATION_DATA_OBJ", mrpBarcodeBulkUpdateResponse);
//                startActivity(intent);
//            }
        }
        if (isUpdated) {
            Intent intent = new Intent(this, PickListActivity.class);
            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.putExtra("ITEM_UPDATED", "Items data has been updated.");
            intent.putExtra("NOTIFICATION_DATA", allocationdetailUpdate);
            intent.putExtra("NOTIFICATION_DATA_OBJ", mrpBarcodeBulkUpdateResponse);
            startActivity(intent);
        }
    }
}