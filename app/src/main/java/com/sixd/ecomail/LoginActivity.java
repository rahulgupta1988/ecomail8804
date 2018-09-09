package com.sixd.ecomail;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sixd.ecomail.APIManager.APIQueries;
import com.sixd.ecomail.APIManager.LoginManager;
import com.sixd.ecomail.Utility.ConnectivityReceiver;
import com.sixd.ecomail.Utility.Constant;
import com.sixd.ecomail.Utility.InternetConnectionDetector;
import com.sixd.ecomail.Utility.MyApplication;
import com.sixd.ecomail.Utility.SharedPreferencesManager;
import com.sixd.ecomail.adapter.LoginSpinnerAdapter;
import com.sixd.ecomail.model.Theme;

import java.util.ArrayList;

/**
 * Created by Rahul Gupta on 04-04-2018.
 */

public class LoginActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {


    Context context;
    RelativeLayout theme_selector;
    TextView txtThemeName, txHidePass, txForgotPass;
    Button btnSignIn, btnSignUp, btnGO;
    ArrayList<Theme> themes = new ArrayList<>();
    LinearLayout btnGo_lay;
    EditText etUserID, etUserPass;
    AsyncTask<String, Void, String> asyncTask = null;
    ProgressBar progressBarLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;


        String consumerID=SharedPreferencesManager.getConsumerID(context);
        if(consumerID!=null){
            if(consumerID.length()>0){
                Intent home_intent = new Intent(context, HomeActivity.class);
                startActivity(home_intent);
                finish();
            }


            else
                startActivityInit();

        }


        else{
            startActivityInit();
        }

       /* Intent home_intent = new Intent(context, HomeActivity.class);
        startActivity(home_intent);
        finish();*/



    }


    public void startActivityInit(){
        int themeID=MyApplication.getInstance().getThemeID();
        setTheme(themeID);
        setContentView(R.layout.activity_login);
        initViews();
        setSpinner();

        new APIQueries().initSortOptions();
    }


    String userID = "";
    String password = "";

    public void initViews() {
        progressBarLogin = findViewById(R.id.progressBarLogin);
        txForgotPass = findViewById(R.id.txForgotPass);
        btnGo_lay = (LinearLayout) findViewById(R.id.btnGo_lay);
        theme_selector = findViewById(R.id.theme_selector);
        txHidePass = findViewById(R.id.txHidePass);
        txtThemeName = findViewById(R.id.txtThemeName);
        txtThemeName.setText("Select Theme");
        etUserID = findViewById(R.id.etUserID);
        etUserPass = findViewById(R.id.etUserPass);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnGO = findViewById(R.id.btnGO);


        theme_selector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showThemes();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

          /*    Intent home_intent=new Intent(context,HomeActivity.class);
                startActivity(home_intent);
                finish();*/
                if (new InternetConnectionDetector(context).isConnectingToInternet()) {
                    userID = etUserID.getText().toString().trim();
                    password = etUserPass.getText().toString().trim();

                    if (userID.length() == 0) {
                        Toast.makeText(context, "" + Constant.Message.LOGIN_ERROR, Toast.LENGTH_SHORT).show();
                        return;
                    } else if (password.length() == 0) {
                        Toast.makeText(context, "" + Constant.Message.PASSWORD_ERROR, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    asyncTask = new LoginTask();
                    asyncTask.execute();
                } else {
                    Toast.makeText(context, "" + Constant.Message.INTERNET_ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });


        btnGO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home_intent = new Intent(context, HomeActivity.class);
                startActivity(home_intent);
                finish();
            }
        });

        txHidePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (txHidePass.getText().toString().equals("Hide Password")) {
                        txHidePass.setText("Show Password");
                        etUserPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        etUserPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        etUserPass.setSelection(etUserPass.getText().length());
                    } else {
                        txHidePass.setText("Hide Password");
                        etUserPass.setInputType(InputType.TYPE_CLASS_TEXT);
                        etUserPass.setSelection(etUserPass.getText().length());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        txForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forgot_intent = new Intent(context, ForgotPassActivity.class);
                startActivity(forgot_intent);
                finish();
                overridePendingTransition(R.anim.slide_from_right_new, R.anim.slide_to_left_new);

            }
        });
    }

    public void setSpinner() {
        themes.add(new Theme("Ecomail", "#0057B8"));
        themes.add(new Theme("Doctors Without Borders", "#0057B8"));
        themes.add(new Theme("Chase Bank", "#0057B8"));
        themes.add(new Theme("The Nature Conservancy", "#0057B8"));
        themes.add(new Theme("Bank Of America", "#0057B8"));
        themes.add(new Theme("Wells Fargo", "#0057B8"));
        themes.add(new Theme("Citizens Bank", "#0057B8"));
    }

    Dialog themeDialog = null;

    private void showThemes() {
        int themeID=MyApplication.getInstance().getThemeID();
        themeDialog = new Dialog(context, themeID);
        themeDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        themeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        themeDialog.setContentView(R.layout.theme_dialog);
        themeDialog.setCancelable(false);

        android.support.v7.widget.ListViewCompat theme_list = themeDialog.findViewById(R.id.theme_list);
        TextView txt_cancel = themeDialog.findViewById(R.id.txt_cancel);
        TextView txt_save = themeDialog.findViewById(R.id.txt_save);
        LoginSpinnerAdapter loginSpinnerAdapter = new LoginSpinnerAdapter(context, themes);
        theme_list.setAdapter(loginSpinnerAdapter);


        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themeDialog.dismiss();
            }
        });

        txt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (pos == -1) {
                    Toast.makeText(context, "Please select a theme.", Toast.LENGTH_SHORT).show();

                } else {
                    String themeName = themes.get(pos).getThemeName();
                    SharedPreferencesManager.putThemeName(context, themeName);
                    txtThemeName.setText("" + themeName);

                    btnGo_lay.setVisibility(View.VISIBLE);

                    themeDialog.dismiss();
                    pos = -1;
                }

            }
        });


        themeDialog.show();

    }


    int pos = -1;

    public void selectedTheme(int pos) {
        this.pos = pos;
    }

    public class LoginTask extends AsyncTask<String, Void, String> {
        String responce_Str = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // progressDialog.show();
            progressBarLogin.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {

            responce_Str = new LoginManager(context).loginUser(userID, password);
            return responce_Str;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("res1321", "" + s);

            if (responce_Str.equals(Constant.SUCCESS_MSG)) {
                btnSignIn.setEnabled(false);
                btnSignUp.setEnabled(false);
                theme_selector.setVisibility(View.VISIBLE);
                Toast.makeText(context, "" + Constant.Message.LOGIN_SUCCESS, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "" + responce_Str, Toast.LENGTH_SHORT).show();
            }
            // progressDialog.dismiss();
            progressBarLogin.setVisibility(View.GONE);
        }
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

        if (isConnected) {
            MyApplication.MyLogi("conect11", "" + isConnected);
        } else {
            progressBarLogin.setVisibility(View.GONE);

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

}
