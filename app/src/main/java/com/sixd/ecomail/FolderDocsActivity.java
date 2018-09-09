package com.sixd.ecomail;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sixd.ecomail.APIManager.APIQueries;
import com.sixd.ecomail.APIManager.DocumentMoveManager;
import com.sixd.ecomail.Utility.Constant;
import com.sixd.ecomail.Utility.MyApplication;
import com.sixd.ecomail.Utility.ProgressLoaderUtilities;
import com.sixd.ecomail.Utility.SharedPreferencesManager;
import com.sixd.ecomail.Utility.SwipeAndDragHelper;
import com.sixd.ecomail.Utility.SwipeController;
import com.sixd.ecomail.Utility.SwipeControllerActions;
import com.sixd.ecomail.adapter.FolderDocsAdapter;
import com.sixd.ecomail.adapter.FoldersAdapter;
import com.sixd.ecomail.adapter.InboxListAdapter;
import com.sixd.ecomail.fragment.BottomSheetFragment;
import com.sixd.ecomail.model.Custom_InboxDataModel;
import com.sixd.ecomail.model.Document;
import com.sixd.ecomail.model.Document_Actions;
import com.sixd.ecomail.model.Document_Actions_Display;
import com.sixd.ecomail.model.Document_Actions_Payment;
import com.sixd.ecomail.model.Folder;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;

/**
 * Created by Rahul Gupta on 21-05-2018.
 */
public class FolderDocsActivity extends AppCompatActivity {

    Context mContext;
    RecyclerView recyleInbox;

    RelativeLayout titlebar;
    ImageView img_back;
    EditText editSearch;
    TextView txtFolderName;

    List<Document> all_documents = null;
    List<Custom_InboxDataModel> custom_inboxDataModels = new ArrayList<>();
    String folderName = "";
    String folderID="";

    String leftTxt="Pay";
    String rightTxt1="Trash";
    String rightTxt2="Inbox";
    String rightTxt3="More";

    String isSmartFolder="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folderdocs);
        mContext = this;
        Intent folderIntent = getIntent();

        if (folderIntent != null) {
            all_documents=FoldersAdapter.temp_list;
            folderName = folderIntent.getStringExtra("folderName");
            MyApplication.MyLogi("FolderName23424",""+folderName);
            isSmartFolder=folderName.split("_")[1];
            folderName= folderName.split("_")[0];
            if(folderName.equals("Archive")){
                rightTxt1="Trash";
            }
            else if(folderName.equals("Trash")){
                rightTxt1="Archive";
            }

            else{
                folderID=folderIntent.getStringExtra("folderID");
            }




            initView();
            createCustomInboxData();

        }
    }



    public void initView() {
        titlebar = findViewById(R.id.titlebar);
        img_back = findViewById(R.id.img_back);
        recyleInbox = findViewById(R.id.recyleInbox);
        txtFolderName =findViewById(R.id.txtFolderName);

        txtFolderName.setText("" + folderName);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

            }
        });

        setInboxList();
    }

    FolderDocsAdapter inboxListAdapter = null;
    SwipeController swipeController = null;
    public void setInboxList() {
        inboxListAdapter = new FolderDocsAdapter(mContext, custom_inboxDataModels);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyleInbox.setLayoutManager(layoutManager);
        recyleInbox.setItemAnimator(new DefaultItemAnimator());
        recyleInbox.setAdapter(inboxListAdapter);

       /* SwipeAndDragHelper swipeAndDragHelper = new SwipeAndDragHelper(inboxListAdapter, inboxListAdapter, mContext);
        ItemTouchHelper touchHelper = new ItemTouchHelper(swipeAndDragHelper);
        inboxListAdapter.setTouchHelper(touchHelper);
        recyleInbox.setAdapter(inboxListAdapter);
        touchHelper.attachToRecyclerView(recyleInbox);*/
       if(isSmartFolder.equals("false")) {

           swipeController = new SwipeController(new SwipeControllerActions() {
               @Override
               public void onLeftClicked(int position, String actionName) {
                   // super.onLeftClicked(position,actionName);
                   Toast.makeText(mContext, "" + actionName, Toast.LENGTH_SHORT).show();
               }

               @Override
               public void onRightClicked(int position, String actionName) {
                   //  inboxListAdapter.players.remove(position);
               /* inboxListAdapter.notifyItemRemoved(position);
                inboxListAdapter.notifyItemRangeChanged(position, inboxListAdapter.getItemCount());*/

                   if (actionName.equals("More"))
                       bottomsheetDialog();
                   else if (actionName.equals("Archive"))
                       moveDocumnet("ARCHIVE", position);
                   else if (actionName.equals("Trash"))
                       moveDocumnet("TRASH", position);
                   else if (actionName.equals("Inbox"))
                       moveDocumnet("INBOX", position);

               }
           }, mContext, leftTxt, rightTxt1, rightTxt2, rightTxt3);

           ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
           itemTouchhelper.attachToRecyclerView(recyleInbox);

           recyleInbox.addItemDecoration(new RecyclerView.ItemDecoration() {
               @Override
               public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                   swipeController.onDraw(c);
               }
           });
       }
    }


    public void createCustomInboxData() {

        for (int l = 0; l < all_documents.size(); l++) {

            Document document = all_documents.get(l);

            String docID = document.getDocumentId();
            String documentName = document.getDocumentName();
            String documentGroupName = new APIQueries().getDocsGroupNameByID(document.getDocumentGroupId());
            String primaryConsumerName = document.getPrimaryConsumerName();
            String contentCreationDate = document.getContentCreationDate();
            String logoUrl = document.getDocumentLogoLocation();
            String documentWebLocation = document.getDocumentWebLocation();
            String documentCategroyId = document.getDocumentCategroyId();
            String DocumentGroupId = document.getDocumentGroupId();
            String contentLocation = document.getContentLocation();
            String producerName = document.getProducerName();
            String recipientName = document.getRecipientName();
            String transmissionId = document.getTransmissionId();
            String displayLocation=document.getDisplayLocation();
            String documentDate = document.getDocumentDate();
            String preiodStart = document.getPreiodStart();
            String preiodEnd = document.getPreiodEnd();
            String paymentStateId = new APIQueries().getpaymentStateIdByTransmissionID(transmissionId);
            String viewType = "";

            String actionDueDate = "";
            String amountDue = "";
            String minimumAmountDue = "";
            String actionID = "";
            String message = "";
            String link = "";
            MyApplication.MyLogi("09443", "" + documentCategroyId);

            if (document.getDocumentCategroyId().equals("INVOIC") || document.getDocumentCategroyId().equals("DOCMNT")) {

                viewType = "1";
                Document_Actions document_actions = document.document_actionsToOne.getTarget();
                if (document_actions != null) {
                    Document_Actions_Payment document_actions_payment = document_actions.document_actions_paymentToOne.getTarget();
                    if (document_actions_payment != null) {
                        actionDueDate = document_actions_payment.getActionDueDate();
                        amountDue = document_actions_payment.getAmountDue();
                        minimumAmountDue = document_actions_payment.getMinimumAmountDue();
                    }

                    Document_Actions_Display document_actions_display = document_actions.document_actions_displayToOne.getTarget();
                    if (document_actions_display != null) {

                        actionID = document_actions_display.getActionId();
                        message = document_actions_display.getMessage();
                        link = document_actions_display.getLink();


                    }

                }
            } else {
                viewType = "0";
            }


            Custom_InboxDataModel custom_inboxDataModel = new Custom_InboxDataModel(documentName, documentGroupName, primaryConsumerName, contentCreationDate,
                    logoUrl, documentWebLocation, documentCategroyId, amountDue, minimumAmountDue, actionDueDate, link, message,
                    actionID, DocumentGroupId, contentLocation, producerName, recipientName, paymentStateId, viewType);
            custom_inboxDataModel.setTransmissionId(transmissionId);
            custom_inboxDataModel.setDocumentID(docID);
            custom_inboxDataModel.setDocumentDate(documentDate);
            custom_inboxDataModel.setDisplayLocation(displayLocation);
            custom_inboxDataModel.setPreiodStart(preiodStart);
            custom_inboxDataModel.setPreiodEnd(preiodEnd);
            custom_inboxDataModels.add(custom_inboxDataModel);


        }


    }


    public void bottomsheetDialog(){
       /* View view = getLayoutInflater().inflate(R.layout.fragment_bottom_sheet_dialog, null);
        BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(mContext);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();*/
        BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());

    }

    ProgressDialog progressDialog = null;

    public void moveDocumnet(String displayLocation,int docPos){

        new AsyncTask<String, Void, String>(){
            String responce_Str = "";
            String consumerId;
            String transmissionId="";
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressLoaderUtilities().getProgressDialog(mContext);
                consumerId = SharedPreferencesManager.getConsumerID(mContext);
                Custom_InboxDataModel temp_document=custom_inboxDataModels.get(docPos);
                transmissionId=temp_document.getTransmissionId();


            }

            @Override
            protected String doInBackground(String... strings) {

                responce_Str = new DocumentMoveManager(mContext).callMoveDocument(transmissionId, consumerId,displayLocation);
                return responce_Str;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                progressDialog.dismiss();
                if (responce_Str.equals(Constant.SUCCESS_MSG)) {


                    if(!folderName.equals("Archive") && !folderName.equals("Trash")){
                        Folder folder=new APIQueries().getFolderByID(folderID);

                        List<Document> documents=new ArrayList<>();
                        documents.addAll(folder.documentToMany);
                        folder.documentToMany.removeAll(documents
                        );

                        for(int j=0;j<documents.size();j++){
                            Document document_temp=documents.get(j);
                            if(document_temp.getDocumentId().equals(custom_inboxDataModels.get(docPos).getDocumentID())){
                                documents.remove(j);
                                folder.documentToMany.addAll(documents);

                                BoxStore boxStore = MyApplication.getInstance().getBoxStore();
                                Box<Folder> folderBox = boxStore.boxFor(Folder.class);
                                folderBox.put(folder);
                                break;
                            }
                        }
                    }



                    Toast.makeText(mContext, "" + Constant.Message.FOLDER_MOVE_SUCCESS, Toast.LENGTH_SHORT).show();
                    new APIQueries().updateLocationByTransmissionId(transmissionId,displayLocation);
                    custom_inboxDataModels.remove(docPos);
                    inboxListAdapter.notifyDataSetChanged();




                } else {
                    Toast.makeText(mContext, "" + responce_Str, Toast.LENGTH_SHORT).show();
                }



            }
        }.execute();

    }



    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        Intent intent = new Intent();
        setResult(701, intent);
        finish();//finishing activity
    }
}
