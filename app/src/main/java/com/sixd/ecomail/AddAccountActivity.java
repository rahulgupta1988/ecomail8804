package com.sixd.ecomail;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.ScrollView;

import com.sixd.ecomail.Utility.MyApplication;

/**
 * Created by Rahul Gupta on 17-04-2018.
 */

public class AddAccountActivity extends AppCompatActivity {

    Context mContext;
    ImageView img_back,img_save;
    TabLayout tablay;
    View viewAccountCredit,viewAccountDebit;
    ScrollView scrollLay;

    EditText etAccountType,etBillingAddress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addaccountactivity_view);
        mContext=this;
        initViews();
    }

    public void initViews(){
        img_back=findViewById(R.id.img_back);
        img_save=findViewById(R.id.img_save);
        tablay=findViewById(R.id.tablay);
        tablay=findViewById(R.id.tablay);
        viewAccountCredit=findViewById(R.id.viewAccountCredit);
        viewAccountDebit=findViewById(R.id.viewAccountDebit);
        scrollLay=findViewById(R.id.scrollLay);

        // debit fields
        etAccountType=findViewById(R.id.etAccountType);


        //credit fields
        etBillingAddress=findViewById(R.id.etBillingAddress);


        setTabs();

        img_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        etAccountType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accountTypeDialogView();
            }
        });

        etBillingAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent billingIntent=new Intent(mContext,BillingAddressActivity.class);
                startActivity(billingIntent);
            }
        });
    }


    public void setTabs(){
        tablay.setTabTextColors(Color.parseColor("#041C2C"), Color.parseColor("#0057B8"));
        tablay.addTab(tablay.newTab().setText("CREDIT"));
        tablay.addTab(tablay.newTab().setText("DEBIT"));


        tablay.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                scrollLay.scrollTo(0,scrollLay.getScrollY());
                if(tab.getPosition()==0){
                    viewAccountDebit.setVisibility(View.GONE);
                    viewAccountCredit.setVisibility(View.VISIBLE);
                }

                else {
                    viewAccountCredit.setVisibility(View.GONE);
                    viewAccountDebit.setVisibility(View.VISIBLE);
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


    String accountTypeStr="";
    public void accountTypeDialogView(){
        accountTypeStr=etAccountType.getText().toString().trim();
        final Dialog accountTypeDailog = new Dialog(this,R.style.AppTheme);
        accountTypeDailog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        accountTypeDailog.setContentView(R.layout.accounttype_view);
        accountTypeDailog.setCancelable(false);

        ImageView img_back=accountTypeDailog.findViewById(R.id.img_back);
        ImageView img_save=accountTypeDailog.findViewById(R.id.img_save);
        final RadioButton radioChecking=accountTypeDailog.findViewById(R.id.radioChecking);
        final RadioButton radioSaving=accountTypeDailog.findViewById(R.id.radioSaving);

        if(accountTypeStr.equals("Checking"))
            radioChecking.setChecked(true);
        else if(accountTypeStr.equals("Saving"))
            radioSaving.setChecked(true);



        radioChecking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioChecking.setChecked(true);
                radioSaving.setChecked(false);
                accountTypeStr="Checking";
            }
        });


        radioSaving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioChecking.setChecked(false);
                radioSaving.setChecked(true);
                accountTypeStr="Saving";
            }
        });






        img_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etAccountType.setText(accountTypeStr);
                accountTypeDailog.dismiss();
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accountTypeDailog.dismiss();
            }
        });

        accountTypeDailog.show();
    }



}
