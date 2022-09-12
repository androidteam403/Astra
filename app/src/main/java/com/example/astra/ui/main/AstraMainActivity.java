package com.example.astra.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.example.astra.R;
import com.example.astra.databinding.ActivityAstraMainBinding;
import com.example.astra.ui.adapter.ItemListAdapter;
import com.example.astra.ui.adapter.PickListAdapter;

import java.util.ArrayList;

public class AstraMainActivity extends AppCompatActivity {
    private ActivityAstraMainBinding activityAstraMainBinding;
    private PickListAdapter pickListAdapter;
    private ItemListAdapter itemListAdapter;
    ArrayList<String> pickList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activityAstraMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_astra_main);
        pickListAdapter = new PickListAdapter(this, pickList);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        activityAstraMainBinding.picklistrecycleview.setLayoutManager(mLayoutManager2);
        pickList.add("syed");
        pickList.add("a");
        activityAstraMainBinding.picklistrecycleview.setAdapter(pickListAdapter);
        itemListAdapter = new ItemListAdapter(this, pickList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        activityAstraMainBinding.listitemRecycleview.setLayoutManager(layoutManager);
        activityAstraMainBinding.listitemRecycleview.setAdapter(itemListAdapter);
        RelativeLayout pickListLayout = findViewById(R.id.myProfileLayout);
        pickListLayout.setBackgroundResource(R.color.dark_yellow);

    }
}