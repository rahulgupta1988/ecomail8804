package com.sixd.ecomail;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sixd.ecomail.Utility.MyApplication;
import com.sixd.ecomail.adapter.AddressDetailsAdapter;
import com.sixd.ecomail.model.AddressDetails;
import com.sixd.ecomail.model.State;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rahul Gupta on 18-04-2018.
 */

public class BillingAddressActivity extends AppCompatActivity {

    Context mContext;
    ImageView img_back, img_save, imgSearch,imgRemoveText,imgSearcBack;
    EditText editSearch;
    TabLayout tablay;
    View viewAddresslist, viewAddDifferent, searchbar;
    RecyclerView billingAddressList;
    RelativeLayout headerview;
    InputMethodManager inputMethodManager=null;
    ArrayList<AddressDetails> addressDetailsList = new ArrayList<>();
    List<AddressDetails> addressDetailsListSearch = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billingaddress);
        mContext = this;
        inputMethodManager =
                (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        initViews();
    }

    public void initViews() {
        headerview = findViewById(R.id.headerview);
        editSearch = findViewById(R.id.editSearch);
        searchbar = findViewById(R.id.searchbar);
        imgRemoveText= findViewById(R.id.imgRemoveText);
        img_back = findViewById(R.id.img_back);
        img_save = findViewById(R.id.img_save);
        imgSearch = findViewById(R.id.imgSearch);
        imgSearcBack= findViewById(R.id.imgSearcBack);
        tablay = findViewById(R.id.tablay);
        tablay = findViewById(R.id.tablay);
        viewAddresslist = findViewById(R.id.viewAddresslist);
        viewAddDifferent = findViewById(R.id.viewAddDifferent);
        billingAddressList = findViewById(R.id.billingAddressList);
        setTabs();
        setAddress();
        setAddressList();
        setSearch();

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    public void setTabs() {
        tablay.setTabTextColors(Color.parseColor("#041C2C"), Color.parseColor("#0057B8"));
        tablay.addTab(tablay.newTab().setText("ADDRESS LIST"));
        tablay.addTab(tablay.newTab().setText("ADD DIFFERENT"));


        tablay.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == 0) {
                    viewAddDifferent.setVisibility(View.GONE);
                    viewAddresslist.setVisibility(View.VISIBLE);
                    imgSearch.setVisibility(View.VISIBLE);
                } else {
                    imgSearch.setVisibility(View.GONE);
                    viewAddresslist.setVisibility(View.GONE);
                    viewAddDifferent.setVisibility(View.VISIBLE);

                    inputMethodManager.hideSoftInputFromWindow(editSearch.getWindowToken(), 0);
                    headerview.setVisibility(View.VISIBLE);
                    searchbar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                MyApplication.MyLogi("ubselectedTab", "" + tab.getPosition());

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }


        });


    }

    AddressDetailsAdapter addressAdapter=null;
    public void setAddressList() {
        addressAdapter = new AddressDetailsAdapter(mContext, addressStr, addressDetailsListSearch);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        billingAddressList.setLayoutManager(layoutManager);
        billingAddressList.setItemAnimator(new DefaultItemAnimator());
        billingAddressList.setAdapter(addressAdapter);
    }

    String addressStr = "";

    public void setAddress(String addressStr) {
        this.addressStr = addressStr;
    }


    String[] adds = {"123 6th St. Melbourne, FL 32904", "71 Pilgrim Avenue Chevy Chase, MD 20815", "70 Bowman St. South Windsor, CT 06074",
            "4 Goldfield Rd. Honolulu, HI 96815", "44 Shirley Ave. West Chicago, IL 60185", "514 S. Magnolia St. Orlando, FL 32806", "430 Princeton St. Wilmette, IL 60091"};

    public void setAddress() {
        for (int i = 0; i < 6; i++) {
            AddressDetails addressDetails = new AddressDetails();
            addressDetails.setStreetAddress(adds[i]);
            addressDetailsList.add(addressDetails);
        }
        addressDetailsListSearch.addAll(addressDetailsList);
    }



    public void setSearch(){


        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                headerview.setVisibility(View.GONE);
                searchbar.setVisibility(View.VISIBLE);
                editSearch.requestFocus();


                inputMethodManager.toggleSoftInputFromWindow(
                        editSearch.getApplicationWindowToken(),
                        InputMethodManager.SHOW_FORCED, 0);



                editSearch.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        List<AddressDetails> temp = new ArrayList();
                        for(AddressDetails d: addressDetailsList){
                            //or use .equal(text) with you want equal match
                            //use .toLowerCase() for better matches
                            if(d.getStreetAddress().toLowerCase().contains(s.toString().trim().toLowerCase())){
                                temp.add(d);
                            }
                        }
                        //update recyclerview
                        addressDetailsListSearch.clear();
                        addressDetailsListSearch.addAll(temp);
                        addressAdapter.notifyDataSetChanged();
                    }
                });

            }
        });

        imgSearcBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                       /* stateAdapter.notifyDataSetChanged();
                        editSearch.setText("");*/
                searchbar.setVisibility(View.GONE);
                headerview.setVisibility(View.VISIBLE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

                addressDetailsListSearch.clear();
                addressDetailsListSearch.addAll(addressDetailsList);
                addressAdapter.notifyDataSetChanged();
                editSearch.setText("");
            }
        });


        imgRemoveText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editSearch.setText("");
            }
        });



    }

}
