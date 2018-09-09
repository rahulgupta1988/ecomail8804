package com.sixd.ecomail;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sixd.ecomail.APIManager.APIQueries;
import com.sixd.ecomail.Utility.MyApplication;
import com.sixd.ecomail.fragment.InboxFrag;
import com.sixd.ecomail.fragment.PaymentDueFrag;
import com.sixd.ecomail.fragment.PaymentFailedFrag;
import com.sixd.ecomail.fragment.PaymentPaidFrag;
import com.sixd.ecomail.fragment.PaymentScheduleFrag;
import com.sixd.ecomail.model.Document;
import com.sixd.ecomail.model.Payment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rahul Gupta on 26-04-2018.
 */

public class PaymentActivity extends AppCompatActivity {

    Context mContext;
    TabLayout tablay;
    FragmentManager fragmentManager;

    RelativeLayout titlebar;
    View searchbar;
    ImageView imgSearcBack,imgRemoveText,imgSearch,img_back,imgEye;
    EditText editSearch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        mContext = this;
        paymentData();
        initViews();
        fillContainer("due");
    }

    public void initViews() {
        fragmentManager = getSupportFragmentManager();
        tablay = findViewById(R.id.tablay);

        titlebar=findViewById(R.id.titlebar);
        imgEye=findViewById(R.id.imgEye);
        searchbar=findViewById(R.id.searchbar);
        imgSearch=findViewById(R.id.imgSearch);
        imgSearcBack=findViewById(R.id.imgSearcBack);
        imgRemoveText=findViewById(R.id.imgRemoveText);
        editSearch=findViewById(R.id.editSearch);
        img_back=findViewById(R.id.img_back);

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSearch();
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imgRemoveText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editSearch.setText("");
            }
        });

        imgEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        setTabs();
    }


    public void setSearch(){
        titlebar.setVisibility(View.GONE);
        searchbar.setVisibility(View.VISIBLE);
        editSearch.requestFocus();

        final InputMethodManager inputMethodManager =
                (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(
                editSearch.getApplicationWindowToken(),
                InputMethodManager.SHOW_FORCED, 0);


        imgSearcBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editSearch.setText("");
                searchbar.setVisibility(View.GONE);
                titlebar.setVisibility(View.VISIBLE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);


            }
        });

    }

    public void setTabs() {
        tablay.setTabTextColors(Color.parseColor("#041C2C"), Color.parseColor("#0057B8"));
        tablay.addTab(tablay.newTab().setText("DUE"));
        tablay.addTab(tablay.newTab().setText("SCHEDULEd"));
        tablay.addTab(tablay.newTab().setText("PAID"));
        tablay.addTab(tablay.newTab().setText("FAILED"));


        tablay.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if(tab.getPosition()==0)
                    fillContainer("due");
                else if(tab.getPosition()==1)
                    fillContainer("schedule");
                else if(tab.getPosition()==2)
                    fillContainer("paid");
                else if(tab.getPosition()==3)
                    fillContainer("failed");
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {


            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }


    PaymentDueFrag paymentDueFrag=null;
    PaymentScheduleFrag paymentScheduleFrag=null;
    PaymentPaidFrag paymentPaidFrag=null;
    PaymentFailedFrag paymentFailedFrag=null;

    public void fillContainer(String fragname) {

        if (fragname.equals("due")) {
            imgEye.setVisibility(View.GONE);
            if (paymentDueFrag == null) {
                paymentDueFrag = new PaymentDueFrag();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.paymentfragContainer, paymentDueFrag);
                fragmentTransaction.commit();
            } else {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.paymentfragContainer, paymentDueFrag);
                fragmentTransaction.commit();
            }
        }


        else if (fragname.equals("schedule")) {
            imgEye.setVisibility(View.VISIBLE);
            if (paymentScheduleFrag == null) {
                paymentScheduleFrag = new PaymentScheduleFrag();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.paymentfragContainer, paymentScheduleFrag);
                fragmentTransaction.commit();
            } else {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.paymentfragContainer, paymentScheduleFrag);
                fragmentTransaction.commit();
            }
        }

        else if (fragname.equals("paid")) {
            imgEye.setVisibility(View.VISIBLE);
            if (paymentPaidFrag == null) {
                paymentPaidFrag = new PaymentPaidFrag();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.paymentfragContainer, paymentPaidFrag);
                fragmentTransaction.commit();
            } else {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.paymentfragContainer, paymentPaidFrag);
                fragmentTransaction.commit();
            }
        }

        else if (fragname.equals("failed")) {
            imgEye.setVisibility(View.GONE);
            if (paymentFailedFrag == null) {
                paymentFailedFrag = new PaymentFailedFrag();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.paymentfragContainer, paymentFailedFrag);
                fragmentTransaction.commit();
            } else {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.paymentfragContainer, paymentFailedFrag);
                fragmentTransaction.commit();
            }
        }

    }


    public List<Document> paymentDue_doc=new ArrayList<>();
    public List<Document> paymentScheduled_doc=new ArrayList<>();
    public List<Document> paymentPaid_doc=new ArrayList<>();
    public List<Document> paymentFailed_doc=new ArrayList<>();

    public void paymentData(){
        List<Document> payment_doc=new APIQueries().getDocsByPrimaryAction("PAYMNT","TRASH");



        for(int z=0;z<payment_doc.size();z++){
            Document pay_doc=payment_doc.get(z);
            Payment payment=new APIQueries().getpaymentByTransmissionID(pay_doc.getTransmissionId());

            if(payment==null){
                paymentDue_doc.add(pay_doc);
            }

            else{
                if(payment.getPaymentStateId().equals("PAID")){
                    paymentPaid_doc.add(pay_doc);

                }
                else if(payment.getPaymentStateId().equals("FAILED")){
                    paymentFailed_doc.add(pay_doc);
                }
                else{
                    paymentScheduled_doc.add(pay_doc);

                }
            }
        }


        MyApplication.MyLogi("payment_doc1",""+payment_doc.size());
        MyApplication.MyLogi("paymentDue_doc",""+paymentDue_doc.size());
        MyApplication.MyLogi("paymentScheduled_doc",""+paymentScheduled_doc.size());
        MyApplication.MyLogi("paymentPaid_doc",""+paymentPaid_doc.size());
        MyApplication.MyLogi("paymentFailed_doc",""+paymentFailed_doc.size());
    }
}
