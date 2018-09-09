package com.sixd.ecomail.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.provider.FontRequest;
import android.provider.FontsContract;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sixd.ecomail.HomeActivity;
import com.sixd.ecomail.PaymentAccountActivity;
import com.sixd.ecomail.PersonalInfoActivity;
import com.sixd.ecomail.R;
import com.sixd.ecomail.RecipentsActivity;
import com.sixd.ecomail.SettingAddressActivty;
import com.sixd.ecomail.SettingSecurityActivity;
import com.sixd.ecomail.Utility.MyApplication;
import com.sixd.ecomail.Utility.SharedPreferencesManager;

/**
 * Created by Rahul Gupta on 08-04-2018.
 */

public class Setting_Frag extends Fragment implements View.OnClickListener{

    Context mContext;
    View view;
    TabLayout tablay;
    TextView txtLogout;

    View viewAccount,viewEcomail;
    RelativeLayout relPersonalInfo,relAddress,relAccounts,relRecipents,relSecurity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.setting_frag, null);
        initViews();
        return view;
    }

    public void initViews(){
        txtLogout=view.findViewById(R.id.txtLogout);
        viewAccount=view.findViewById(R.id.viewAccount);
        viewEcomail=view.findViewById(R.id.viewEcomail);
        relPersonalInfo=view.findViewById(R.id.relPersonalInfo);
        relAddress=view.findViewById(R.id.relAddress);
        relAccounts=view.findViewById(R.id.relAccounts);
        relRecipents=view.findViewById(R.id.relRecipents);
        relSecurity=view.findViewById(R.id.relSecurity);

     /*   relPersonalInfo.setOnClickListener(this);
        relAddress.setOnClickListener(this);
        relAccounts.setOnClickListener(this);
        relRecipents.setOnClickListener(this);
        relSecurity.setOnClickListener(this);*/


        tablay=view.findViewById(R.id.tablay);
        setTabs();

        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomeActivity)getActivity()).logOut();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext=context;
    }

    public void setTabs(){

        tablay.setTabTextColors(Color.parseColor("#041C2C"), Color.parseColor("#0057B8"));
        tablay.addTab(tablay.newTab().setText("Consumer Account"));
        tablay.addTab(tablay.newTab().setText("Eco-Mail"));


        tablay.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition()==0){
                    viewEcomail.setVisibility(View.GONE);
                    viewAccount.setVisibility(View.VISIBLE);
                }

                else {
                    viewAccount.setVisibility(View.GONE);
                    viewEcomail.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                MyApplication.MyLogi("ubselectedTab",""+tab.getPosition());

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.relPersonalInfo:
                Intent personalInfoIntent=new Intent(mContext, PersonalInfoActivity.class);
                startActivity(personalInfoIntent);

                break;

            case R.id.relAddress:
                Intent addressIntent=new Intent(mContext, SettingAddressActivty.class);
                startActivity(addressIntent);

                break;

            case R.id.relAccounts:
                Intent acountIntent=new Intent(mContext, PaymentAccountActivity.class);
                startActivity(acountIntent);

                break;

            case R.id.relRecipents:
                Intent recipentIntent=new Intent(mContext, RecipentsActivity.class);
                startActivity(recipentIntent);

                break;


            case R.id.relSecurity:
                Intent securityIntent=new Intent(mContext, SettingSecurityActivity.class);
                startActivity(securityIntent);

                break;



        }
    }


}

