package com.sixd.ecomail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.sixd.ecomail.adapter.PaymentAccountAdapter;
import com.sixd.ecomail.adapter.SettingAddressAdapter;
import com.sixd.ecomail.model.Card;
import com.sixd.ecomail.model.CardSectionModel;

import java.util.ArrayList;

/**
 * Created by Rahul Gupta on 17-04-2018.
 */

public class PaymentAccountActivity extends Activity {

    Context mContext;
    RecyclerView cardList;
    ImageView img_back;
    FloatingActionButton fabAdd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymentaccount);
        mContext=this;
        initViews();
    }

    public void initViews(){

        fabAdd=findViewById(R.id.fabAdd);
        img_back=findViewById(R.id.img_back);
        cardList=findViewById(R.id.cardList);
        populateRecyclerView();

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addAccountIntent=new Intent(mContext,AddAccountActivity.class);
                startActivity(addAccountIntent);
            }
        });
    }



    //populate recycler view
    private void populateRecyclerView() {
        ArrayList<CardSectionModel> sectionModelArrayList = new ArrayList<>();
        //for loop for sections
        for (int i = 1; i <= 2; i++) {
            ArrayList<Card> itemArrayList = new ArrayList<>();
            //for loop for items
            for (int j = 1; j <= 3; j++) {
                Card card=new Card();
                card.setName("Credit Card " + j);
                if(j==1) card.setIsPrimary("PRIMARY");
                itemArrayList.add(card);
            }

            //add the section and items to array list
            if(i==1)
            sectionModelArrayList.add(new CardSectionModel("Credit Card", itemArrayList));
            else if(i==2)
                sectionModelArrayList.add(new CardSectionModel("Debit", itemArrayList));
        }

        cardList.setHasFixedSize(true);
        PaymentAccountAdapter paymentAccountAdapter = new PaymentAccountAdapter(mContext,sectionModelArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        cardList.setLayoutManager(layoutManager);
        cardList.setItemAnimator(new DefaultItemAnimator());
        cardList.setAdapter(paymentAccountAdapter);

        cardList.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

}
