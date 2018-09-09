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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.sixd.ecomail.APIManager.APIQueries;
import com.sixd.ecomail.APIManager.AddNormalFolderManager;
import com.sixd.ecomail.APIManager.AddSmartFolderManager;
import com.sixd.ecomail.Utility.Constant;
import com.sixd.ecomail.Utility.MyApplication;
import com.sixd.ecomail.Utility.ProgressLoaderUtilities;
import com.sixd.ecomail.Utility.SharedPreferencesManager;
import com.sixd.ecomail.adapter.FolderRecipentsAdapter;
import com.sixd.ecomail.adapter.SmartDocGPAdapter;
import com.sixd.ecomail.adapter.SmartDocTypeAdapter;
import com.sixd.ecomail.adapter.SmartFolderAdapter;
import com.sixd.ecomail.model.DocTypes;
import com.sixd.ecomail.model.DocumentGroup;
import com.sixd.ecomail.model.Folder;
import com.sixd.ecomail.model.Recipents;
import com.sixd.ecomail.model.SmartFolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import io.objectbox.Box;
import io.objectbox.BoxStore;

/**
 * Created by Rahul Gupta on 20-04-2018.
 */

public class AddSmartFolderActivity extends AppCompatActivity {

    Context mContext;
    EditText etSmartFolderName;
    Button btnNext, btnCancel;
    RelativeLayout folderNameLay, docTypeLay, docgroupLay, recipentsLay,folderLay;
    List<Recipents> recipentsArrayList = null;
    String folderNamestr="";
    int maxSequence;
    String addoredit;

    String folderID;
    SmartFolder smartFolder=null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smartfolderactivity);
        mContext = this;


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
                smartFolder=new APIQueries().getSmartFolderByID(folderID);

            }

        }

        createDocTypes();
        createDocGroups();
        makeFolderList();
        getRecipients();


        initViews();

    }

    public void initViews() {
        folderNameLay = findViewById(R.id.folderNameLay);
        docTypeLay = findViewById(R.id.docTypeLay);
        docgroupLay = findViewById(R.id.docgroupLay);
        recipentsLay = findViewById(R.id.recipentsLay);
        folderLay= findViewById(R.id.folderLay);

        etSmartFolderName = findViewById(R.id.etSmartFolderName);
        etSmartFolderName.setText(""+folderNamestr);
        btnNext = findViewById(R.id.btnNext);
        btnCancel = findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (screenCount == 0) {
                    addSmartFolder();
                } else if (screenCount == 1) {
                    screenCount = 2;
                    addDocGroupsView();
                } else if (screenCount == 2) {
                    screenCount = 3;
                    addRecipents();
                } else if (screenCount == 3) {
                    {

                        if(btnNext.getText().toString().toLowerCase().equals("save")){
                            collectData();
                        }
                        else {
                            setFolder();
                            btnNext.setText("Save");
                        }
                    }
                }

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    int screenCount = 0;
    String folderName = "";

    public void addSmartFolder() {

        folderName = etSmartFolderName.getText().toString().trim();
        if (folderName.length() > 0) {
            folderNameValidation();
        } else {
            Toast.makeText(mContext, "" + Constant.Message.FOLDERNAME_ERROR, Toast.LENGTH_SHORT).show();
        }
    }

    public void addDocTypesView() {
        folderNameLay.setVisibility(View.GONE);
        docgroupLay.setVisibility(View.GONE);
        recipentsLay.setVisibility(View.GONE);
        folderLay.setVisibility(View.GONE);
        docTypeLay.setVisibility(View.VISIBLE);

        RecyclerView listDocTypes = findViewById(R.id.listDocTypes);


        SmartDocTypeAdapter smartDocTypeAdapter = new SmartDocTypeAdapter(mContext, docTypesList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        listDocTypes.setLayoutManager(layoutManager);
        listDocTypes.setItemAnimator(new DefaultItemAnimator());
        listDocTypes.setAdapter(smartDocTypeAdapter);
    }


    public void addDocGroupsView() {
        folderNameLay.setVisibility(View.GONE);
        docTypeLay.setVisibility(View.GONE);
        recipentsLay.setVisibility(View.GONE);
        folderLay.setVisibility(View.GONE);
        docgroupLay.setVisibility(View.VISIBLE);


        RecyclerView listDocGroups = findViewById(R.id.listDocGroups);

        SmartDocGPAdapter smartDocGPAdapter = new SmartDocGPAdapter(mContext, docGPList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        listDocGroups.setLayoutManager(layoutManager);
        listDocGroups.setItemAnimator(new DefaultItemAnimator());
        listDocGroups.setAdapter(smartDocGPAdapter);
    }




    public void addRecipents() {



        folderNameLay.setVisibility(View.GONE);
        docTypeLay.setVisibility(View.GONE);
        docgroupLay.setVisibility(View.GONE);
        folderLay.setVisibility(View.GONE);
        recipentsLay.setVisibility(View.VISIBLE);

        RecyclerView listRecipents = findViewById(R.id.listRecipents);
        FolderRecipentsAdapter folderRecipentsAdapter = new FolderRecipentsAdapter(mContext,recipentsArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        listRecipents.setLayoutManager(layoutManager);
        listRecipents.setItemAnimator(new DefaultItemAnimator());
        listRecipents.setAdapter(folderRecipentsAdapter);
    }

    public void setFolder(){
        folderNameLay.setVisibility(View.GONE);
        docTypeLay.setVisibility(View.GONE);
        recipentsLay.setVisibility(View.GONE);
        docgroupLay.setVisibility(View.GONE);
        folderLay.setVisibility(View.VISIBLE);

        RecyclerView listFolders = findViewById(R.id.listFolders);

        SmartFolderAdapter smartDocGPAdapter = new SmartFolderAdapter(mContext, folderArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        listFolders.setLayoutManager(layoutManager);
        listFolders.setItemAnimator(new DefaultItemAnimator());
        listFolders.setAdapter(smartDocGPAdapter);
    }


    List<DocTypes> docTypesList=null;
    List<DocumentGroup> docGPList = new ArrayList<>();
    List<Folder> folderArrayList = new ArrayList<>();

    public void createDocTypes() {
        docTypesList=new APIQueries().getDocumentTypes();
        if(smartFolder!=null) {
            List<DocTypes> selectedDoctypes=smartFolder.docTypesToMany;
            for (int i = 0; i < docTypesList.size(); i++) {
                for (int j = 0; j < selectedDoctypes.size(); j++) {
                    if(docTypesList.get(i).getDocumentTypeName().equals(selectedDoctypes.get(j).getDocumentTypeName())){
                        docTypesList.get(i).setSelected(true);
                    }
                }
            }
        }

    }

    public void createDocGroups() {

        docGPList= new APIQueries().getDocumentGroup();

        if(smartFolder!=null) {
            List<DocumentGroup> selectedDoctGp=smartFolder.documentGroupToMany;
            for (int i = 0; i < docGPList.size(); i++) {
                for (int j = 0; j < selectedDoctGp.size(); j++) {
                    if(docGPList.get(i).getDocumentGroupId().equals(selectedDoctGp.get(j).getDocumentGroupId())){
                        docGPList.get(i).setSelected(true);
                    }
                }
            }
        }

    }


    public void getRecipients(){
        recipentsArrayList = new APIQueries().getRecipents();

        if(smartFolder!=null) {
            List<Recipents> selectedRecip=smartFolder.recipentsToMany;
            for (int i = 0; i < recipentsArrayList.size(); i++) {
                for (int j = 0; j < selectedRecip.size(); j++) {
                    if(recipentsArrayList.get(i).getNetworkLoginId().equals(selectedRecip.get(j).getNetworkLoginId())){
                        recipentsArrayList.get(i).setSelected(true);
                    }
                }
            }
        }
    }


    String[] folderNamearr = {"Investment Related", "EMX Enterprise", "Bills", "T1 Bikram", "T2 Bikram", "T3 Bikram"};

    public void makeFolderList() {


        BoxStore boxStore = MyApplication.getInstance().getBoxStore();
        Box<Folder> folderBox = boxStore.boxFor(Folder.class);
        folderArrayList = folderBox.getAll();

        if(smartFolder!=null) {
            List<Folder> selectedFolder=smartFolder.folderToMany;
            for (int i = 0; i < folderArrayList.size(); i++) {
                for (int j = 0; j < selectedFolder.size(); j++) {
                    if(folderArrayList.get(i).getFolderId().equals(selectedFolder.get(j).getFolderId())){
                        folderArrayList.get(i).setSelected(true);
                    }
                }
            }
        }

    }

    JSONObject jsonObject=null;
    public void collectData(){

        Log.i("FolderName",""+folderName);

        JSONArray DocumentTypes=new JSONArray();
        JSONArray Tags=new JSONArray();
        JSONArray Recipients=new JSONArray();
        JSONArray DocumentGroups=new JSONArray();

        for(int i=0;i<docTypesList.size();i++){

            if(docTypesList.get(i).isSelected())
            DocumentTypes.put(docTypesList.get(i).getDocumentType());
        }


        for(int i=0;i<docGPList.size();i++){

            if(docGPList.get(i).isSelected())
            DocumentGroups.put(docGPList.get(i).getDocumentGroupId());
        }

        for(int i=0;i<recipentsArrayList.size();i++){

            if(recipentsArrayList.get(i).isSelected())
            Recipients.put(recipentsArrayList.get(i).getConsumerId());
        }

        for(int i=0;i<folderArrayList.size();i++){

            if(folderArrayList.get(i).isSelected())
            Tags.put(folderArrayList.get(i).getFolderId());
        }


         jsonObject=new JSONObject();
        try {
            jsonObject.put("DocumentTypes",DocumentTypes);
            jsonObject.put("Tags",Tags);
            jsonObject.put("Recipients",Recipients);
            jsonObject.put("DocumentGroups",DocumentGroups);
            Log.i("json23534",""+jsonObject.toString());

            if(addoredit.equals("1")) {
                new AddFolderTask().execute();
            }

            else if(addoredit.equals("2")) {
                new EditFolderTask().execute();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void folderNameValidation(){
        String patternReg="[a-zA-Z0-9?~!@#$%^*()\\[\\]{}+'_-` ]*$";
        Pattern pattern = Pattern.compile(patternReg);
        folderName=folderName.trim();
        boolean isMatched=Pattern.matches(patternReg,folderName);
        if(folderName.length()>0 && isMatched){
            screenCount = 1;
            addDocTypesView();
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
            sequenceNo =maxSequence+1;
        }

        @Override
        protected String doInBackground(String... strings) {

            responce_Str = new AddSmartFolderManager(mContext).callAddSmartFolderAPI(folderName,consumerId,jsonObject.toString(),sequenceNo);
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
        String createDate="";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressLoaderUtilities().getProgressDialog(mContext);
            consumerId = SharedPreferencesManager.getConsumerID(mContext);
            createDate=smartFolder.getCreateDate();
        }

        @Override
        protected String doInBackground(String... strings) {

            responce_Str = new AddSmartFolderManager(mContext).callEditFolderAPI(folderName, consumerId, jsonObject.toString(),maxSequence, folderID,createDate);
            return responce_Str;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressDialog.dismiss();
            Log.i("res1321", "" + s);

            if (responce_Str.equals(Constant.SUCCESS_MSG)) {
                AddEditedFolder();
                Toast.makeText(mContext, "" + Constant.Message.FOLDER_EDIT_SUCCESS, Toast.LENGTH_SHORT).show();
               /* new APIQueries().updateFolderByFolderName(folderID,folderNamestr);*/

                Intent intent = new Intent();
                setResult(605, intent);
                finish();//finishing activity




            } else {
                Toast.makeText(mContext, "" + responce_Str, Toast.LENGTH_SHORT).show();
            }



        }
    }


    public void AddEditedFolder(){
        BoxStore boxStore = MyApplication.getInstance().getBoxStore();
        Box<SmartFolder> smartFolderBox = boxStore.boxFor(SmartFolder.class);

        int order = maxSequence;
        String createDate = smartFolder.getCreateDate();
        String smartFolderId = folderID;
        String smartFolderName = folderName;



        Date now = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS ZZZ");
        String currentdateTimeStr = df.format(now);

        new APIQueries().removeFolderByFolderID(smartFolderId,true);

        SmartFolder smartFolder = new SmartFolder(order, createDate, smartFolderId, smartFolderName, currentdateTimeStr);


        for (int m = 0; m < docGPList.size(); m++) {
            if(docGPList.get(m).isSelected()) {
                String docGPID = docGPList.get(m).getDocumentGroupId();
                DocumentGroup documentGroup = new APIQueries().getDocumentGroupByID(docGPID);
                smartFolder.documentGroupToMany.add(documentGroup);
            }
        }

        for (int n = 0; n < docTypesList.size(); n++) {
            if(docTypesList.get(n).isSelected()) {
                String doctypeID = docTypesList.get(n).getDocumentType();
                DocTypes docTypes = new APIQueries().getDocumentTypeByID(doctypeID);
                smartFolder.docTypesToMany.add(docTypes);
            }

        }

        for (int p = 0; p < recipentsArrayList.size(); p++) {
            if(recipentsArrayList.get(p).isSelected()) {
                String recipID = recipentsArrayList.get(p).getConsumerId();
                Recipents recipents = new APIQueries().getRecipentsByID(recipID);
                smartFolder.recipentsToMany.add(recipents);
            }
        }

        for (int l = 0; l < folderArrayList.size(); l++) {
            if(folderArrayList.get(l).isSelected()) {
                String folderid = folderArrayList.get(l).getFolderId();
                Folder folder = new APIQueries().getFolderByID(folderid);
                smartFolder.folderToMany.add(folder);
            }
        }

        smartFolderBox.put(smartFolder);

    }

}