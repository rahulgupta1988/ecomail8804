package com.sixd.ecomail;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.sixd.ecomail.Utility.CallResponce;
import com.sixd.ecomail.Utility.ConnectivityReceiver;
import com.sixd.ecomail.Utility.InitAPICall;
import com.sixd.ecomail.Utility.MyApplication;
import com.sixd.ecomail.Utility.ProgressLoaderUtilities;
import com.sixd.ecomail.Utility.SharedPreferencesManager;
import com.sixd.ecomail.fragment.FolderFrag;
import com.sixd.ecomail.fragment.InboxFrag;
import com.sixd.ecomail.fragment.OffersFrag;
import com.sixd.ecomail.fragment.RectFrag;
import com.sixd.ecomail.fragment.Setting_Frag;

public class HomeActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    ProgressDialog progressDialog = null;
    AsyncTask<String, Void, String> asyncTask = null;
    FragmentManager fragmentManager;
    Context mContext;
    ProgressLoaderUtilities progressLoaderUtilities=null;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.navigation_inbox:
                    fillContainer("inbox");
                    //setTxt("inbox");
                    return true;

                case R.id.navigation_rect:
                    fillContainer("navigation");
                    //setTxt("navigation");
                    return true;

                case R.id.navigation_folder:
                    fillContainer("folder");
                    //setTxt("folder");
                    return true;

                case R.id.navigation_offers:
                    fillContainer("offers");
                    //setTxt("offers");
                    return true;

                case R.id.navigation_setting:
                    fillContainer("setting");
                    //setTxt("setting");
                    return true;

            }

            return false;
        }
    };


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int themeID=MyApplication.getInstance().getThemeID();
        setTheme(themeID);

        setContentView(R.layout.activity_home);
        mContext = this;
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        String consumerID = SharedPreferencesManager.getConsumerID(mContext);
        progressLoaderUtilities=new ProgressLoaderUtilities();
        progressDialog = progressLoaderUtilities.getProgressDialog(mContext);
        new InitAPICall(mContext,progressLoaderUtilities, consumerID, new CallResponce() {
            @Override
            public void onSuccess() {

                progressDialog.dismiss();
                InitViews();
            }

            @Override
            public void onFail() {

                progressDialog.dismiss();
                Toast.makeText(mContext,"Some error occurred. Please try again.",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
       //InitViews();

    }


    public void InitViews() {
        fragmentManager = getSupportFragmentManager();
        fillContainer("inbox");
        //setTxt("inbox");
    }


    InboxFrag inboxFrag = null;
    RectFrag rectFrag = null;
    FolderFrag folderFrag = null;
    OffersFrag offersFrag = null;
    Setting_Frag setting_frag = null;

    public void fillContainer(String fragname) {

        if (fragname.equals("inbox")) {
            if (inboxFrag == null) {
                inboxFrag = new InboxFrag();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragContainer, inboxFrag);
                fragmentTransaction.commit();
            } else {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragContainer, inboxFrag);
                fragmentTransaction.commit();
            }
        } else if (fragname.equals("navigation")) {
            if (rectFrag == null) {
                rectFrag = new RectFrag();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragContainer, rectFrag);
                fragmentTransaction.commit();
            } else {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragContainer, rectFrag);
                fragmentTransaction.commit();
            }
        } else if (fragname.equals("folder")) {
            if (folderFrag == null) {
                folderFrag = new FolderFrag();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragContainer, folderFrag);
                fragmentTransaction.commit();
            } else {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragContainer, folderFrag);
                fragmentTransaction.commit();
            }
        } else if (fragname.equals("offers")) {
            if (offersFrag == null) {
                offersFrag = new OffersFrag();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragContainer, offersFrag);
                fragmentTransaction.commit();
            } else {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragContainer, offersFrag);
                fragmentTransaction.commit();
            }
        } else if (fragname.equals("setting")) {
            if (setting_frag == null) {
                setting_frag = new Setting_Frag();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragContainer, setting_frag);
                fragmentTransaction.commit();
            } else {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragContainer, setting_frag);
                fragmentTransaction.commit();
            }
        }


    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

        if (isConnected) {
            MyApplication.MyLogi("conect11", "" + isConnected);
        } else {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }

            if (asyncTask != null) {
                asyncTask.cancel(true);
                asyncTask = null;
            }

            MyApplication.MyLogi("conect12", "" + isConnected);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
    }

    public void setTxt(String fragName) {
        TextView textView = findViewById(R.id.txt_frag);
        textView.setText("" + fragName);
    }


    public void logOut(){
        SharedPreferencesManager.putThemeName(mContext, null);
        SharedPreferencesManager.deleteConsumerID(mContext);
        Intent intent=new Intent(mContext,LoginActivity.class);
        startActivity(intent);
        finish();
    }

 /*   @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MyApplication.MyLogi("resultCode","OK "+resultCode);
        if(requestCode==601){
           inboxFrag.onActivityResult(requestCode,  resultCode,  data);
        }

    }*/
}
