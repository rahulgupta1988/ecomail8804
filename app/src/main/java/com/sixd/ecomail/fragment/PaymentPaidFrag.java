package com.sixd.ecomail.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sixd.ecomail.APIManager.APIQueries;
import com.sixd.ecomail.APIManager.DocumentMoveManager;
import com.sixd.ecomail.PaymentActivity;
import com.sixd.ecomail.R;
import com.sixd.ecomail.Utility.Constant;
import com.sixd.ecomail.Utility.MyApplication;
import com.sixd.ecomail.Utility.ProgressLoaderUtilities;
import com.sixd.ecomail.Utility.SharedPreferencesManager;
import com.sixd.ecomail.Utility.SwipeAndDragHelper;
import com.sixd.ecomail.Utility.SwipeController;
import com.sixd.ecomail.Utility.SwipeControllerActions;
import com.sixd.ecomail.adapter.PaymentPaidAdapter;
import com.sixd.ecomail.adapter.PaymentScheduleAdapter;
import com.sixd.ecomail.model.Custom_InboxDataModel;
import com.sixd.ecomail.model.Document;
import com.sixd.ecomail.model.Document_Actions;
import com.sixd.ecomail.model.Document_Actions_Display;
import com.sixd.ecomail.model.Document_Actions_Payment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rahul Gupta on 27-04-2018.
 */

public class PaymentPaidFrag extends Fragment {
    View view;
    Context mContext;
    RecyclerView recylePaid;
    List<Document> all_documents = new ArrayList<>();
    List<Custom_InboxDataModel> custom_inboxDataModels = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_paymentpaid, null);
        all_documents.clear();
        all_documents= ((PaymentActivity)getActivity()).paymentPaid_doc;
        createCustomInboxData();
        initViews();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;

    }


    public void initViews(){
        recylePaid=view.findViewById(R.id.recylePaid);
        setPaidList();
    }

    SwipeController swipeController = null;
    PaymentPaidAdapter paymentPaidAdapter=null;
    public void setPaidList(){
        PaymentPaidAdapter paymentPaidAdapter=new PaymentPaidAdapter(mContext,custom_inboxDataModels);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(mContext);
        recylePaid.setLayoutManager(layoutManager);
        recylePaid.setItemAnimator(new DefaultItemAnimator());
        recylePaid.setAdapter(paymentPaidAdapter);

        /*SwipeAndDragHelper swipeAndDragHelper = new SwipeAndDragHelper(paymentPaidAdapter,paymentPaidAdapter,mContext);
        ItemTouchHelper touchHelper = new ItemTouchHelper(swipeAndDragHelper);
        paymentPaidAdapter.setTouchHelper(touchHelper);
        recylePaid.setAdapter(paymentPaidAdapter);
        touchHelper.attachToRecyclerView(recylePaid);*/

        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onLeftClicked(int position, String actionName) {
                // super.onLeftClicked(position,actionName);
                Toast.makeText(mContext, "" + actionName + " " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightClicked(int position, String actionName) {
                //  inboxListAdapter.players.remove(position);
              /*  inboxListAdapter.notifyItemRemoved(position);
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
        }, mContext, "Pay", "Trash", "Archive", "More");

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
         itemTouchhelper.attachToRecyclerView(recylePaid);

        recylePaid.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
    }

    public void createCustomInboxData() {

        for (int l = 0; l < all_documents.size(); l++) {

            Document document = all_documents.get(l);

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
            custom_inboxDataModel.setDocumentDate(documentDate);
            custom_inboxDataModel.setDisplayLocation(displayLocation);

            custom_inboxDataModels.add(custom_inboxDataModel);


        }

    }


    public void bottomsheetDialog() {
       /* View view = getLayoutInflater().inflate(R.layout.fragment_bottom_sheet_dialog, null);
        BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(mContext);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();*/
        BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
        bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());

    }

    ProgressDialog progressDialog = null;

    public void moveDocumnet(String displayLocation, int docPos) {

        new AsyncTask<String, Void, String>() {
            String responce_Str = "";
            String consumerId;
            String transmissionId = "";

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressLoaderUtilities().getProgressDialog(mContext);
                consumerId = SharedPreferencesManager.getConsumerID(mContext);
                Custom_InboxDataModel temp_document = custom_inboxDataModels.get(docPos);
                transmissionId = temp_document.getTransmissionId();

            }

            @Override
            protected String doInBackground(String... strings) {

                responce_Str = new DocumentMoveManager(mContext).callMoveDocument(transmissionId, consumerId, displayLocation);
                return responce_Str;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                progressDialog.dismiss();


                if (responce_Str.equals(Constant.SUCCESS_MSG)) {

                    Toast.makeText(mContext, "" + Constant.Message.FOLDER_MOVE_SUCCESS, Toast.LENGTH_SHORT).show();
                    new APIQueries().updateLocationByTransmissionId(transmissionId, displayLocation);
                    custom_inboxDataModels.remove(docPos);
                    paymentPaidAdapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(mContext, "" + responce_Str, Toast.LENGTH_SHORT).show();
                }


            }
        }.execute();

    }


}
