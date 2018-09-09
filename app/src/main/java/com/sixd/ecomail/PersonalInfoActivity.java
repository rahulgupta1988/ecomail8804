package com.sixd.ecomail;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;

/**
 * Created by Rahul Gupta on 13-04-2018.
 */

public class PersonalInfoActivity extends AppCompatActivity {

    EditText etPrefix,etSuffix,etAltEmail,eUSerAltName;
    ImageView img_back;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalinfo);
        initViews();
    }

    public void initViews(){
        img_back=findViewById(R.id.img_back);
        etPrefix=findViewById(R.id.etPrefix);
        etSuffix=findViewById(R.id.etSuffix);
        etAltEmail=findViewById(R.id.etAltEmail);
        eUSerAltName=findViewById(R.id.eUSerAltName);

        etPrefix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prificDialogView();
            }
        });

        eUSerAltName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alternateNameDialog();
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }


    String prefixStr="";
    public void prificDialogView(){
        final Dialog prefixDailog = new Dialog(this,R.style.AppTheme);
        prefixDailog.requestWindowFeature(Window.FEATURE_NO_TITLE);
 /* prefixDailog.getWindow().setBackgroundDrawable(
    new ColorDrawable(android.graphics.Color.TRANSPARENT));*/
        //prefixDailog.setContentView(R.layout.fragment_forgetpassword);
        prefixDailog.setContentView(R.layout.personalinfoprefixactivity_view);
        prefixDailog.setCancelable(false);
        // prefixDailog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;

        ImageView img_back=prefixDailog.findViewById(R.id.img_back);
        ImageView img_save=prefixDailog.findViewById(R.id.img_save);
        final RadioButton radioMr=prefixDailog.findViewById(R.id.radioMr);
        final RadioButton radioMrs=prefixDailog.findViewById(R.id.radioMrs);
        final RadioButton radioDr=prefixDailog.findViewById(R.id.radioDr);

       if(prefixStr.equals("Mr"))
           radioMr.setChecked(true);
        else if(prefixStr.equals("Mrs"))
           radioMrs.setChecked(true);
       else if(prefixStr.equals("Dr"))
           radioDr.setChecked(true);


        radioMr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioMr.setChecked(true);
                radioMrs.setChecked(false);
                radioDr.setChecked(false);
                prefixStr="Mr";
            }
        });


        radioMrs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioMr.setChecked(false);
                radioMrs.setChecked(true);
                radioDr.setChecked(false);
                prefixStr="Mrs";
            }
        });


        radioDr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioMr.setChecked(false);
                radioMrs.setChecked(false);
                radioDr.setChecked(true);
                prefixStr="Dr";
            }
        });



        img_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etPrefix.setText(prefixStr);
                prefixDailog.dismiss();
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefixDailog.dismiss();
            }
        });

        prefixDailog.show();
    }


    RadioButton lastChecked=null;
    int totalname=0;
    public void alternateNameDialog(){
        final Dialog alternameDailog = new Dialog(this,R.style.AppTheme);
        alternameDailog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        /* prefixDailog.getWindow().setBackgroundDrawable(
        new ColorDrawable(android.graphics.Color.TRANSPARENT));*/
        //prefixDailog.setContentView(R.layout.fragment_forgetpassword);
        alternameDailog.setContentView(R.layout.personalinf0_alternatename);
        alternameDailog.setCancelable(false);
        ImageView img_save=alternameDailog.findViewById(R.id.img_save);
        ImageView img_back=alternameDailog.findViewById(R.id.img_back);
        ImageView imgAddmore=alternameDailog.findViewById(R.id.imgAddmore);


        final LinearLayout alternateName_lay=alternameDailog.findViewById(R.id.alternateName_lay);
        final LayoutInflater layoutInflater=getLayoutInflater();

        for(int i=1;i<=3;i++){
            totalname++;
            View laternameView=layoutInflater.inflate(R.layout.alternatename_view,null);
            LinearLayout.LayoutParams  layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,200);
            laternameView.setLayoutParams(layoutParams);

            final RadioButton radioButton=laternameView.findViewById(R.id.radiobtn);
            final EditText eUSerAltName=laternameView.findViewById(R.id.eUSerAltName);


            eUSerAltName.setHint("Alternate first name "+i);

            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(lastChecked!=null){
                        lastChecked.setChecked(false);
                    }

                    radioButton.setChecked(true);
                    lastChecked=radioButton;
                }
            });

            alternateName_lay.addView(laternameView);
        }


        imgAddmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalname++;
                View laternameView=layoutInflater.inflate(R.layout.alternatename_view,null);
                LinearLayout.LayoutParams  layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,200);
                laternameView.setLayoutParams(layoutParams);

                final RadioButton radioButton=laternameView.findViewById(R.id.radiobtn);
                final EditText eUSerAltName=laternameView.findViewById(R.id.eUSerAltName);

                eUSerAltName.setHint("Alternate first name "+totalname);

                radioButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(lastChecked!=null){
                            lastChecked.setChecked(false);
                        }

                        radioButton.setChecked(true);
                        lastChecked=radioButton;
                    }
                });

                alternateName_lay.addView(laternameView);
            }
        });


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alternameDailog.dismiss();
            }
        });




        alternameDailog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
