package com.sixd.ecomail;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.sixd.ecomail.APIManager.APIQueries;
import com.sixd.ecomail.APIManager.AddNormalFolderManager;
import com.sixd.ecomail.APIManager.FolderAPIManager;
import com.sixd.ecomail.Utility.Constant;
import com.sixd.ecomail.Utility.InitAPICall;
import com.sixd.ecomail.Utility.MyApplication;
import com.sixd.ecomail.Utility.ProgressLoaderUtilities;
import com.sixd.ecomail.Utility.SharedPreferencesManager;

import java.util.regex.Pattern;

/**
 * Created by Rahul Gupta on 20-04-2018.
 */

public class AddNormalFolderActivity extends AppCompatActivity {

    Context mContext;
    EditText etFolderName;
    ImageView img_save,img_back;

    String folderNamestr="";
    int maxSequence;
    String addoredit;

    String folderID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.normalfolderactivity);
        mContext=this;
        Intent intent=getIntent();
        if(intent!=null){
            addoredit=intent.getStringExtra("addoredit");
            if(addoredit.equals("1")){
                maxSequence=intent.getIntExtra("maxSequence",0);
            }

            else if(addoredit.equals("2")){
                maxSequence=intent.getIntExtra("maxSequence",0);
                folderNamestr=intent.getStringExtra("folderName");
                folderID=intent.getStringExtra("folderID");
            }

        }
        initViews();
    }


    public void initViews(){
        etFolderName=findViewById(R.id.etFolderName);
        img_save=findViewById(R.id.img_save);
        img_back=findViewById(R.id.img_back);

        etFolderName.setText(""+folderNamestr);

        img_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                folderNameValidation();
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(1000, intent);
                finish();//finishing activity
            }
        });

    }


    public void folderNameValidation(){
        String patternReg="[a-zA-Z0-9?~!@#$%^*()\\[\\]{}+'_-` ]*$";
        Pattern pattern = Pattern.compile(patternReg);
        folderNamestr=etFolderName.getText().toString().trim();
        boolean isMatched=Pattern.matches(patternReg,folderNamestr);
        if(folderNamestr.length()>0 && isMatched){

            if(addoredit.equals("1")) {
                new AddFolderTask().execute();
            }

            else if(addoredit.equals("2")) {
                new EditFolderTask().execute();
            }
        }

        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Folder name can be content only:\n 1. a-z\n\n 2. A-Z\n\n 3. 0-9\n\n 4. ?~!@#$%^*()\\[\\]{}+'_-`\n\n 5. Space")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert = builder.create();
            alert.show();

        }
    }


    ProgressDialog progressDialog = null;
    public class AddFolderTask extends AsyncTask<String, Void, String> {
        String responce_Str = "";
        String consumerId;
        int sequenceNo;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressLoaderUtilities().getProgressDialog(mContext);
            consumerId = SharedPreferencesManager.getConsumerID(mContext);
            MyApplication.MyLogi("maxSequence",""+maxSequence);
            sequenceNo =maxSequence+1;
        }

        @Override
        protected String doInBackground(String... strings) {

            responce_Str = new AddNormalFolderManager(mContext).callAddFolderAPI(folderNamestr,consumerId,sequenceNo);
            return responce_Str;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressDialog.dismiss();
            Log.i("res1321", "" + s);

            if (responce_Str.equals(Constant.SUCCESS_MSG)) {
                Toast.makeText(mContext, "" + Constant.Message.FOLDER_ADD_SUCCESS, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                setResult(604, intent);
                finish();//finishing activity

            }

            else {
                Toast.makeText(mContext,""+responce_Str,Toast.LENGTH_SHORT).show();
            }


        }
    }


    public class EditFolderTask extends AsyncTask<String, Void, String> {
        String responce_Str = "";
        String consumerId;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressLoaderUtilities().getProgressDialog(mContext);
            consumerId = SharedPreferencesManager.getConsumerID(mContext);
        }

        @Override
        protected String doInBackground(String... strings) {

            responce_Str = new AddNormalFolderManager(mContext).callEditFolderAPI(folderNamestr, consumerId, maxSequence, folderID);
            return responce_Str;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressDialog.dismiss();
            Log.i("res1321", "" + s);

            if (responce_Str.equals(Constant.SUCCESS_MSG)) {

                Toast.makeText(mContext, "" + Constant.Message.FOLDER_EDIT_SUCCESS, Toast.LENGTH_SHORT).show();
                new APIQueries().updateFolderByFolderName(folderID,folderNamestr);

                Intent intent = new Intent();
                setResult(605, intent);
                finish();//finishing activity




            } else {
                Toast.makeText(mContext, "" + responce_Str, Toast.LENGTH_SHORT).show();
            }



        }
    }








}
