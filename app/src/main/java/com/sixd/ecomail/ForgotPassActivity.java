package com.sixd.ecomail;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.sixd.ecomail.Utility.ConnectivityReceiver;
import com.sixd.ecomail.Utility.Constant;
import com.sixd.ecomail.Utility.MyApplication;
import com.sixd.ecomail.Utility.VerifierUtility;

/**
 * Created by Rahul Gupta on 06-04-2018.
 */

public class ForgotPassActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{

    ProgressDialog progressDialog=null;
    AsyncTask<String,Void,String> asyncTask=null;
    Context mContext=null;

    EditText et_Email;
    ImageView imgTick,imgCircleTick;
    Button btnSend,btnDone;

    View email_view,done_view,newpass_view,resetdone_view;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int themeID=MyApplication.getInstance().getThemeID();
        setTheme(themeID);
        setContentView(R.layout.activity_forgotpass);
        mContext=this;
        initViews();
    }

    public void initViews(){
        email_view=findViewById(R.id.email_view);
        done_view=findViewById(R.id.done_view);
        newpass_view=findViewById(R.id.newpass_view);
        resetdone_view=findViewById(R.id.resetdone_view);

        et_Email=findViewById(R.id.et_Email);
        imgTick=findViewById(R.id.imgTick);
        imgCircleTick=findViewById(R.id.imgCircleTick);
        btnSend=findViewById(R.id.btnSend);
        btnDone=findViewById(R.id.btnDone);

        ((Animatable) imgCircleTick.getDrawable()).start();
         emailValidator();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imgTick.getVisibility()==View.VISIBLE){
                    email_view.setVisibility(View.GONE);
                    newpass_view.setVisibility(View.GONE);
                    resetdone_view.setVisibility(View.GONE);
                    done_view.setVisibility(View.VISIBLE);
                }

                else{

                    Toast.makeText(mContext, Constant.Message.EMAIL_ERROR,Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email_view.setVisibility(View.GONE);
                done_view.setVisibility(View.GONE);
                resetdone_view.setVisibility(View.GONE);
                newpass_view.setVisibility(View.VISIBLE);
                setNewPassword();
            }
        });


    }


    EditText etNewPass,etRePass;
    Button btnNewPassDone,btnResetDone;

    public void setNewPassword(){
        etNewPass=findViewById(R.id.etNewPass);
        etRePass=findViewById(R.id.etRePass);
        btnNewPassDone=findViewById(R.id.btnNewPassDone);
        btnResetDone=findViewById(R.id.btnResetDone);

        etRePass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String newPass=etNewPass.getText().toString().trim();
                String rePass=editable.toString().trim();

                if(newPass.equals(rePass)){
                    btnNewPassDone.setBackgroundResource(R.drawable.btn_rect);
                }
                else{
                    btnNewPassDone.setBackgroundResource(R.drawable.login_rect_disable);
                }
            }
        });


        btnNewPassDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String s1=etNewPass.getText().toString().trim();
                String s2=etRePass.getText().toString().trim();
                if(s1.equals(s2) && s1.length()>0 && s2.length()>0) {
                    email_view.setVisibility(View.GONE);
                    done_view.setVisibility(View.GONE);
                    newpass_view.setVisibility(View.GONE);
                    resetdone_view.setVisibility(View.VISIBLE);
                }
            }
        });


        btnResetDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }


    public void emailValidator(){



        et_Email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String email = editable.toString().trim();
                if(email.length()>0 && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    imgTick.setVisibility(View.VISIBLE);
                    btnSend.setBackgroundResource(R.drawable.btn_rect);
                }

                else {
                    imgTick.setVisibility(View.GONE);
                    btnSend.setBackgroundResource(R.drawable.login_rect_disable);
                }
            }
        });
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent login_intent=new Intent(mContext,LoginActivity.class);
        startActivity(login_intent);
        finish();
        overridePendingTransition(R.anim.slide_from_left_new, R.anim.slide_to_right_new);
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

        if(isConnected){
            MyApplication.MyLogi("conect11",""+isConnected);
        }

        else{
            if(progressDialog!=null){
                progressDialog.dismiss();
            }

            if(asyncTask!=null){
                asyncTask.cancel(true);
                asyncTask=null;
            }

            MyApplication.MyLogi("conect12",""+isConnected);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
    }
}
