package com.sixd.ecomail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.sixd.ecomail.adapter.SettingAddressAdapter;
import com.sixd.ecomail.adapter.SortInboxAdapter;

/**
 * Created by Rahul Gupta on 16-04-2018.
 */

public class SettingAddressActivty extends AppCompatActivity {

    Context mContext;
    RecyclerView addressLIst;
    ImageView img_back,img_save;
    FloatingActionButton fabAdd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingaddress_activty);
        mContext=this;
        initViews();
        initAdddreeList();
    }

    public void initViews(){
        addressLIst=findViewById(R.id.addressLIst);
        img_back=findViewById(R.id.img_back);
        img_save=findViewById(R.id.img_save);
        fabAdd=findViewById(R.id.fabAdd);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              onBackPressed();
            }
        });


        img_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent addAddressIntent= new Intent(mContext,AddAddressActivity.class);
                startActivity(addAddressIntent);
            }
        });


    }

    public void initAdddreeList(){
        SettingAddressAdapter settingAddressAdapter = new SettingAddressAdapter(mContext);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        addressLIst.setLayoutManager(layoutManager);
        addressLIst.setItemAnimator(new DefaultItemAnimator());
        addressLIst.setAdapter(settingAddressAdapter);

        addressLIst.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fabAdd.getVisibility() == View.VISIBLE) {
                    fabAdd.hide();
                } else if (dy < 0 && fabAdd.getVisibility() != View.VISIBLE) {
                    fabAdd.show();
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
