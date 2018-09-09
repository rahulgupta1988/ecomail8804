package com.sixd.ecomail.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sixd.ecomail.APIManager.APIQueries;
import com.sixd.ecomail.ExistSubsCriptionActivity;
import com.sixd.ecomail.NewsNavigationActivity;
import com.sixd.ecomail.PaymentActivity;
import com.sixd.ecomail.R;
import com.sixd.ecomail.Utility.MyApplication;
import com.sixd.ecomail.Utility.SharedPreferencesManager;
import com.sixd.ecomail.model.Document;
import com.sixd.ecomail.model.DocumentGroup;
import com.sixd.ecomail.model.Payment;
import com.sixd.ecomail.model.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Rahul Gupta on 08-04-2018.
 */

public class RectFrag extends Fragment {

    Context mContext;
    View view;
    RelativeLayout newLay, paymentsLay;
    TextView txteExisting,txtExistingCount;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.rect_fragment, null);
        initViews();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public void initViews() {
        txtExistingCount=view.findViewById(R.id.txtExistingCount);
        txteExisting=view.findViewById(R.id.txteExisting);
        newLay = view.findViewById(R.id.newLay);
        paymentsLay = view.findViewById(R.id.paymentsLay);
       /* newLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newsIntent = new Intent(mContext, NewsNavigationActivity.class);
                startActivity(newsIntent);

            }

        });*/

        paymentsLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent paymentIntent = new Intent(mContext, PaymentActivity.class);
                startActivity(paymentIntent);

            }

        });

        txteExisting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent exisSubsIntent=new Intent(mContext, ExistSubsCriptionActivity.class);
                startActivity(exisSubsIntent);
            }
        });

        createSubscriptionData();
    }



    public void createSubscriptionData(){

        List<DocumentGroup>  documentGroups=new APIQueries().getDocsGroups();
        MyApplication.MyLogi("documentGroups1111"," "+documentGroups.size());

        List<Subscription> subscriptionList=new ArrayList<>();

        for(int i=0;i<documentGroups.size();i++){

            subscriptionList.addAll(documentGroups.get(i).subscriptionToMany);
        }



        txtExistingCount.setText(""+subscriptionList.size());




    }





}
