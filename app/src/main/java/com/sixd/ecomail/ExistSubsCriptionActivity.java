package com.sixd.ecomail;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sixd.ecomail.APIManager.APIQueries;
import com.sixd.ecomail.Utility.MyApplication;
import com.sixd.ecomail.Utility.SharedPreferencesManager;
import com.sixd.ecomail.adapter.FolderListDialogAdapter;
import com.sixd.ecomail.adapter.MySubsAdapter;
import com.sixd.ecomail.adapter.SharedSubsAdapter;
import com.sixd.ecomail.adapter.SubsPeopleDocAdapter;
import com.sixd.ecomail.adapter.SubscriptionSharingPeopleAdapter;
import com.sixd.ecomail.model.Document;
import com.sixd.ecomail.model.DocumentGroup;
import com.sixd.ecomail.model.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Rahul Gupta on 30-04-2018.
 */

public class ExistSubsCriptionActivity extends AppCompatActivity {

    RecyclerView recyleMySubs, recyleSharedSubs;
    Context mContext;
    TabLayout tablay;
    TextView txt_suscount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existsubscription);
        mContext = this;
        createSubscriptionData();
        initViews();
        setTabs();
        setMySubsList();
        setSharedSubsList();
    }

    public void initViews() {
        txt_suscount=findViewById(R.id.txt_suscount);
        recyleMySubs = findViewById(R.id.recyleMySubs);
        recyleSharedSubs = findViewById(R.id.recyleSharedSubs);
        tablay = findViewById(R.id.tablay);
        txt_suscount.setText("Existing Subscriptions ("+subscriptionList.size()+")");

    }


    public void setTabs() {

        tablay.setTabTextColors(Color.parseColor("#041C2C"), Color.parseColor("#0057B8"));
        tablay.addTab(tablay.newTab().setText("My Subscriptions"));
        tablay.addTab(tablay.newTab().setText("Shared with me"));


        tablay.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    recyleSharedSubs.setVisibility(View.GONE);
                    recyleMySubs.setVisibility(View.VISIBLE);
                } else {
                    recyleMySubs.setVisibility(View.GONE);
                    recyleSharedSubs.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {


            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    public void setMySubsList() {

        MySubsAdapter mySubsAdapter = new MySubsAdapter(mContext,mySubscriptionList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyleMySubs.setLayoutManager(layoutManager);
        recyleMySubs.setItemAnimator(new DefaultItemAnimator());
        recyleMySubs.setAdapter(mySubsAdapter);

    }

    public void setSharedSubsList() {

        SharedSubsAdapter sharedSubsAdapter = new SharedSubsAdapter(mContext,sharedSubscriptionList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyleSharedSubs.setLayoutManager(layoutManager);
        recyleSharedSubs.setItemAnimator(new DefaultItemAnimator());
        recyleSharedSubs.setAdapter(sharedSubsAdapter);

    }

    List<Subscription> subscriptionList=new ArrayList<>();
    List<Subscription> mySubscriptionList=new ArrayList<>();
    List<Subscription> sharedSubscriptionList=new ArrayList<>();

    public void createSubscriptionData(){

        List<DocumentGroup>  documentGroups=new APIQueries().getDocsGroups();
        MyApplication.MyLogi("documentGroups1111"," "+documentGroups.size());



        for(int i=0;i<documentGroups.size();i++){

            subscriptionList.addAll(documentGroups.get(i).subscriptionToMany);
        }

        String consumerId= SharedPreferencesManager.getConsumerID(mContext);

        mySubscriptionList = subscriptionList.stream().
                filter(p -> p.getOwnerConsumerId().equals(consumerId)).
                collect(Collectors.toList());


        sharedSubscriptionList = subscriptionList.stream().
                filter(p -> !p.getOwnerConsumerId().equals(consumerId)).
                collect(Collectors.toList());


        MyApplication.MyLogi("mySubscriptionList2343",""+mySubscriptionList.size());
        MyApplication.MyLogi("sharedSubscriptionList2343",""+sharedSubscriptionList.size());

        for(int m=0;m<subscriptionList.size();m++){

            if(subscriptionList.get(m).getSubscriptionId().equals("4500153"))
             MyApplication.MyLogi("mySubscriptionCount"," 111");

        }


    }



    public void subscriptionDetailDialog(int position){
        final Dialog subscriptionDetailsDailog = new Dialog(mContext,R.style.AppTheme);
        subscriptionDetailsDailog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        subscriptionDetailsDailog.setContentView(R.layout.subscription_details_view);
        subscriptionDetailsDailog.setCancelable(false);

        ImageView img_back=subscriptionDetailsDailog.findViewById(R.id.img_back);
        Button btnSharing=subscriptionDetailsDailog.findViewById(R.id.btnSharing);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subscriptionDetailsDailog.dismiss();
            }
        });

        btnSharing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manageSharingDialog();
            }
        });

        subscriptionDetailsDailog.show();
    }


    public void manageSharingDialog(){
        final Dialog manageSharingDailog = new Dialog(mContext,R.style.AppTheme);
        manageSharingDailog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        manageSharingDailog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        manageSharingDailog.setContentView(R.layout.subscription_managesharing);
        manageSharingDailog.setCancelable(false);

        ImageView img_back=manageSharingDailog.findViewById(R.id.img_back);
        RecyclerView sharingpersionlist=manageSharingDailog.findViewById(R.id.sharingpersionlist);

        SubscriptionSharingPeopleAdapter subscriptionSharingPeopleAdapter = new SubscriptionSharingPeopleAdapter(mContext);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        sharingpersionlist.setLayoutManager(layoutManager);
        sharingpersionlist.setItemAnimator(new DefaultItemAnimator());
        sharingpersionlist.setAdapter(subscriptionSharingPeopleAdapter);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manageSharingDailog.dismiss();
            }
        });

        manageSharingDailog.show();
    }



    public void peopleharingDialog(int position){
        final Dialog manageSharingDailog = new Dialog(mContext,R.style.AppTheme);
        manageSharingDailog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        manageSharingDailog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        manageSharingDailog.setContentView(R.layout.subs_sharingpeople_docs);
        manageSharingDailog.setCancelable(false);

        ImageView img_back=manageSharingDailog.findViewById(R.id.img_back);
        RecyclerView sharingpersionlist=manageSharingDailog.findViewById(R.id.sharingpersionlist);

        SubsPeopleDocAdapter subsPeopleDocAdapter = new SubsPeopleDocAdapter(mContext);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        sharingpersionlist.setLayoutManager(layoutManager);
        sharingpersionlist.setItemAnimator(new DefaultItemAnimator());
        sharingpersionlist.setAdapter(subsPeopleDocAdapter);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manageSharingDailog.dismiss();
            }
        });

        manageSharingDailog.show();
    }



}
