package com.sixd.ecomail;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * Created by Rahul Gupta on 19-04-2018.
 */

public class SettingSecurityActivity extends AppCompatActivity {

    TextView txt_chgpass;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);
        initViews();
    }


    public void initViews(){
        txt_chgpass=findViewById(R.id.txt_chgpass);

        txt_chgpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassdDialog();
            }
        });
    }

    public void changePassdDialog(){
        final Dialog recipentsDailog = new Dialog(this,R.style.AppTheme);
        recipentsDailog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        recipentsDailog.setContentView(R.layout.settingchangepassword);
        recipentsDailog.setCancelable(false);

        ImageView img_back=recipentsDailog.findViewById(R.id.img_back);
        ImageView img_save=recipentsDailog.findViewById(R.id.img_save);



        img_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recipentsDailog.dismiss();
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recipentsDailog.dismiss();
            }
        });

        recipentsDailog.show();
    }

}
