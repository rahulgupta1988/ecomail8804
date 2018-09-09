package com.sixd.ecomail.Utility;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.sixd.ecomail.APIManager.FolderAPIManager;
import com.sixd.ecomail.APIManager.FundingSourcesAPIManager;
import com.sixd.ecomail.APIManager.RecipientsAPIManager;
import com.sixd.ecomail.APIManager.SmartFoldersAPIManager;
import com.sixd.ecomail.APIManager.StateSynchronizerAPIManager;
import com.sixd.ecomail.APIManager.SubscriptionsAPIManager;
import com.sixd.ecomail.APIManager.SynchronizerAPIManager;

/**
 * Created by ram on 27-04-2018.
 */

public class InitAPICall {

    Context context;
    String consumerId;
    CallResponce callResponce;
    ProgressLoaderUtilities progressLoaderUtilities=null;

    public InitAPICall(Context context,ProgressLoaderUtilities progressLoaderUtilities ,String consumerId, CallResponce callResponce) {
        this.context = context;
        this.consumerId = consumerId;
        this.callResponce = callResponce;
        this.progressLoaderUtilities=progressLoaderUtilities;
        new FolderAPITask().execute();


    }


    public class FolderAPITask extends AsyncTask<String, Void, String> {
        String responce_Str = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressLoaderUtilities.setWaitingTxt("Saving Folder...");
        }

        @Override
        protected String doInBackground(String... strings) {

            responce_Str = new FolderAPIManager(context).callFolderAPI(consumerId);
            return responce_Str;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            Log.i("res1321", "" + s);

            if (responce_Str.equals(Constant.SUCCESS_MSG)) {
                new RecipentsAPITask().execute();
            } else {
                callResponce.onFail();
            }


        }
    }

    public class RecipentsAPITask extends AsyncTask<String, Void, String> {
        String responce_Str = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressLoaderUtilities.setWaitingTxt("Saving Recipents...");
        }

        @Override
        protected String doInBackground(String... strings) {

            responce_Str = new RecipientsAPIManager(context).callRecipentsAPI(consumerId);
            return responce_Str;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.i("res1321", "" + s);

            if (responce_Str.equals(Constant.SUCCESS_MSG)) {
                new FundingSourcesAPITask().execute();
            } else {
                callResponce.onFail();
            }


        }
    }

    public class FundingSourcesAPITask extends AsyncTask<String, Void, String> {
        String responce_Str = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressLoaderUtilities.setWaitingTxt("Saving Funding Sources...");

        }

        @Override
        protected String doInBackground(String... strings) {

            responce_Str = new FundingSourcesAPIManager(context).callFundingAPI(consumerId);
            return responce_Str;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.i("res1321", "" + s);

            if (responce_Str.equals(Constant.SUCCESS_MSG)) {
                new SubscriptionsAPIManagerAPITask().execute();
            } else {
                callResponce.onFail();
            }


        }
    }

    public class SubscriptionsAPIManagerAPITask extends AsyncTask<String, Void, String> {
        String responce_Str = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressLoaderUtilities.setWaitingTxt("Saving Subscriptions...");

        }

        @Override
        protected String doInBackground(String... strings) {

            responce_Str = new SubscriptionsAPIManager(context).callSubscriptionsAPI(consumerId);
            return responce_Str;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            Log.i("res1321", "" + s);

            if (responce_Str.equals(Constant.SUCCESS_MSG)) {
                new SynchronizerAPIManagerTask().execute();
            } else {
                callResponce.onFail();
            }


        }
    }




    public class SynchronizerAPIManagerTask extends AsyncTask<String, Void, String> {
        String responce_Str = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressLoaderUtilities.setWaitingTxt("Saving Synchronizer...");
        }

        @Override
        protected String doInBackground(String... strings) {

            responce_Str = new SynchronizerAPIManager(context).callSynchronizerAPI(consumerId);
            return responce_Str;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.i("res1321", "" + s);

            if (responce_Str.equals(Constant.SUCCESS_MSG)) {
                new StateSynchronizerAPIManagerTask().execute();
            } else {
                callResponce.onFail();
            }


        }
    }


    public class StateSynchronizerAPIManagerTask extends AsyncTask<String, Void, String> {
        String responce_Str = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressLoaderUtilities.setWaitingTxt("Saving StateSynchronizer...");
        }

        @Override
        protected String doInBackground(String... strings) {

            responce_Str = new StateSynchronizerAPIManager(context).callStateSyncAPI(consumerId);
            return responce_Str;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            Log.i("res1321", "" + s);

            if (responce_Str.equals(Constant.SUCCESS_MSG)) {
                new SmartFolderAPIManagerAPITask().execute();
            } else {
                callResponce.onFail();
            }


        }
    }



    public class SmartFolderAPIManagerAPITask extends AsyncTask<String, Void, String> {
        String responce_Str = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressLoaderUtilities.setWaitingTxt("Saving SmartFolder...");
        }

        @Override
        protected String doInBackground(String... strings) {

            responce_Str = new SmartFoldersAPIManager(context).callSmartFolderAPI(consumerId);
            return responce_Str;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.i("res1321", "" + s);

            if (responce_Str.equals(Constant.SUCCESS_MSG)) {
                callResponce.onSuccess();
            } else {
                callResponce.onFail();
            }


        }
    }


}
