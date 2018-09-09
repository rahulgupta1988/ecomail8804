package com.sixd.ecomail.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sixd.ecomail.APIManager.APIQueries;
import com.sixd.ecomail.APIManager.DocumentMoveManager;
import com.sixd.ecomail.InboxFilterActivity;
import com.sixd.ecomail.InboxSortActivity;
import com.sixd.ecomail.R;
import com.sixd.ecomail.Utility.Constant;
import com.sixd.ecomail.Utility.MyApplication;
import com.sixd.ecomail.Utility.ProgressLoaderUtilities;
import com.sixd.ecomail.Utility.SharedPreferencesManager;
import com.sixd.ecomail.Utility.SwipeController;
import com.sixd.ecomail.Utility.SwipeControllerActions;
import com.sixd.ecomail.adapter.AddressDetailsAdapter;
import com.sixd.ecomail.adapter.FolderListDialogAdapter;
import com.sixd.ecomail.adapter.InboxListAdapter;
import com.sixd.ecomail.model.Custom_FolderModel;
import com.sixd.ecomail.model.Custom_InboxDataModel;
import com.sixd.ecomail.model.Document;
import com.sixd.ecomail.model.Document_Actions;
import com.sixd.ecomail.model.Document_Actions_Display;
import com.sixd.ecomail.model.Document_Actions_Payment;
import com.sixd.ecomail.model.Folder;
import com.sixd.ecomail.model.FundingSources;
import com.sixd.ecomail.model.InboxSortOrder;
import com.sixd.ecomail.model.TempModel;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import io.objectbox.Box;
import io.objectbox.BoxStore;

/**
 * Created by Rahul Gupta on 08-04-2018.
 */

public class InboxFrag extends Fragment {

    Context mContext;
    View view;
    RecyclerView recyleInbox;

    RelativeLayout titlebar;
    View searchbar;
    ImageView imgSearcBack, imgRemoveText, imgSearch, imgSort, imgFilter;
    EditText editSearch;

    List<Document> all_documents = null;
    List<Custom_InboxDataModel> custom_inboxDataModels = new ArrayList<>();

    List<Custom_InboxDataModel> documents = new ArrayList<>();


    ArrayList<String> selCompaniesList = new ArrayList<>();
    ArrayList<String> selDocTypesList = new ArrayList<>();
    ArrayList<String> selResipsList = new ArrayList<>();
    List<InboxSortOrder> sortInboxItemList = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        documents.clear();
        view = inflater.inflate(R.layout.inbox_fragment, null);
        custom_inboxDataModels.clear();
        all_documents = new APIQueries().getDocsByLocation("INBOX");
        createCustomInboxData();
        documents.addAll(custom_inboxDataModels);

        initView();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    public void searchAnimation(View view,boolean isOpen){

        if(isOpen) {
            Animation searchLeftAnim = AnimationUtils.loadAnimation(mContext, R.anim.slide_left);
            view.startAnimation(searchLeftAnim);
        }

        else {
            Animation searchLeftAnim = AnimationUtils.loadAnimation(mContext, R.anim.slide_right);
            searchLeftAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    searchbar.setVisibility(View.GONE);
                    titlebar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            view.startAnimation(searchLeftAnim);
        }
    }

    public void initView() {
        titlebar = view.findViewById(R.id.titlebar);
        searchbar = view.findViewById(R.id.searchbar);
        imgSearch = view.findViewById(R.id.imgSearch);
        imgSort = view.findViewById(R.id.imgSort);
        imgFilter = view.findViewById(R.id.imgFilter);
        imgSearcBack = view.findViewById(R.id.imgSearcBack);
        imgRemoveText = view.findViewById(R.id.imgRemoveText);
        editSearch = view.findViewById(R.id.editSearch);
        recyleInbox = view.findViewById(R.id.recyleInbox);

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSearch();
            }
        });

        imgRemoveText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editSearch.setText("");
                documents.clear();
                documents.addAll(custom_inboxDataModels);
                inboxListAdapter.notifyDataSetChanged();
            }
        });


        imgSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent sortActivity = new Intent(mContext, InboxSortActivity.class);
                startActivityForResult(sortActivity, 1);

            }
        });


        imgFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sortActivity = new Intent(mContext, InboxFilterActivity.class);
                sortActivity.putExtra("selCompanies", selCompaniesList);
                sortActivity.putExtra("selDocTypes", selDocTypesList);
                sortActivity.putExtra("selResips", selResipsList);
                startActivityForResult(sortActivity, 1);
            }
        });
        setInboxList();
    }


    public void setSearch() {
        titlebar.setVisibility(View.GONE);
        searchbar.setVisibility(View.VISIBLE);
        searchAnimation(searchbar,true);
        editSearch.requestFocus();

        final InputMethodManager inputMethodManager =
                (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(
                editSearch.getApplicationWindowToken(),
                InputMethodManager.SHOW_FORCED, 0);


        imgSearcBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editSearch.setText("");
                searchAnimation(searchbar,false);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);


            }
        });

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                List<Custom_InboxDataModel> search_documents = new ArrayList<>();

                String search_text = editable.toString();


                for (int k = 0; k < custom_inboxDataModels.size(); k++) {
                    MyApplication.MyLogi("search_text", "" + custom_inboxDataModels.get(k).getDocumentGroupName());

                    if (custom_inboxDataModels.get(k).getDocumentName().toLowerCase().contains(search_text.toLowerCase())) {

                        search_documents.add(custom_inboxDataModels.get(k));

                    }
                }


                documents.clear();
                documents.addAll(search_documents);
                inboxListAdapter.notifyDataSetChanged();

            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MyApplication.MyLogi("onAttach", "onAttach");
        mContext = context;
    }

    InboxListAdapter inboxListAdapter = null;
    SwipeController swipeController = null;

    public void setInboxList() {
        inboxListAdapter = new InboxListAdapter(mContext, documents);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyleInbox.setLayoutManager(layoutManager);
        recyleInbox.setItemAnimator(new DefaultItemAnimator());
        recyleInbox.setAdapter(inboxListAdapter);
      /*  SwipeAndDragHelper swipeAndDragHelper = new SwipeAndDragHelper(inboxListAdapter, inboxListAdapter, mContext);
        ItemTouchHelper touchHelper = new ItemTouchHelper(swipeAndDragHelper);
        inboxListAdapter.setTouchHelper(touchHelper);
        recyleInbox.setAdapter(inboxListAdapter);
        touchHelper.attachToRecyclerView(recyleInbox);*/


        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onLeftClicked(int position, String actionName) {
                // super.onLeftClicked(position,actionName);
                Toast.makeText(mContext, "" + actionName + " " + position, Toast.LENGTH_SHORT).show();
                payModifyDialog(position);
            }

            @Override
            public void onRightClicked(int position, String actionName) {

                //  inboxListAdapter.players.remove(position);
              /*  inboxListAdapter.notifyItemRemoved(position);
                inboxListAdapter.notifyItemRangeChanged(position, inboxListAdapter.getItemCount());*/
                if (actionName.equals("More"))
                    bottomsheetDialog();
                else if (actionName.equals("Archive"))
                    folderListDialog(position);

                else if (actionName.equals("Trash"))
                    moveDocumnet("TRASH", position);
            }
        }, mContext, "Pay", "Trash", "Archive", "More");

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyleInbox);

        recyleInbox.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onDetach() {
        super.onDetach();

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
            custom_inboxDataModel.setPreiodStart(preiodStart);
            custom_inboxDataModel.setPreiodEnd(preiodEnd);

            custom_inboxDataModels.add(custom_inboxDataModel);


            sortInbox();
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 601) {
            HashMap<String, ArrayList<String>> selFilters = (HashMap<String, ArrayList<String>>) data.getSerializableExtra("selFilters");

            if (selFilters.size() != 0) {
                selCompaniesList = selFilters.get("selCompanies");
                selDocTypesList = selFilters.get("selDocTypes");
                selResipsList = selFilters.get("selResips");
/*
                if (selCompaniesList.size() == 0 && selDocTypesList.size() == 0 && selResipsList.size() == 0) {
                    selCompaniesList.clear();
                    selDocTypesList.clear();
                    selResipsList.clear();

                    documents.clear();
                    documents.addAll(custom_inboxDataModels);
                    inboxListAdapter.notifyDataSetChanged();


                }*/

                List<TempModel> tempModels1 = new ArrayList<>();
                List<TempModel> tempModels2 = new ArrayList<>();
                List<TempModel> tempModels3 = new ArrayList<>();



                /*for(int y=0;y<custom_inboxDataModels.size();y++){
                    MyApplication.MyLogi("343",""+custom_inboxDataModels.get(y).getRecipientName());
                }*/


                //selCompaniesList
                for (int y = 0; y < selCompaniesList.size(); y++) {
                    TempModel tempModel = new TempModel();
                    tempModel.setDocTypes(selCompaniesList.get(y));
                    tempModels1.add(tempModel);
                }


                //selDocTypesList
                for (int y = 0; y < selDocTypesList.size(); y++) {
                    TempModel tempModel = new TempModel();
                    tempModel.setDocTypes(selDocTypesList.get(y));
                    tempModels2.add(tempModel);
                }


                //selResipsList
                for (int y = 0; y < selResipsList.size(); y++) {
                    TempModel tempModel = new TempModel();
                    tempModel.setDocTypes(selResipsList.get(y));
                    tempModels3.add(tempModel);
                }


            /*    List<Custom_InboxDataModel> companiesOutput =
                        custom_inboxDataModels.stream()
                                .filter(e -> tempModels1.stream().map(TempModel::getDocTypes).anyMatch(name -> name.equals(e.getProducerName())))
                                .collect(Collectors.toList());


                List<Custom_InboxDataModel> docTypesOutput =
                        custom_inboxDataModels.stream()
                                .filter(e -> tempModels2.stream().map(TempModel::getDocTypes).anyMatch(name -> name.equals(e.getDocumentCategroyId())))
                                .collect(Collectors.toList());

                List<Custom_InboxDataModel> resipsOutput =
                        custom_inboxDataModels.stream()
                                .filter(e -> tempModels3.stream().map(TempModel::getDocTypes).anyMatch(name -> name.equals(e.getRecipientName())))
                                .collect(Collectors.toList());*/


                List<Custom_InboxDataModel> filterOutput = new ArrayList<>();


                // for all filter
                if (tempModels1.size() > 0 && tempModels2.size() > 0 && tempModels3.size() > 0) {

                    MyApplication.MyLogi("check filter111", "CDR");

                    List<Custom_InboxDataModel> companiesOutput =
                            custom_inboxDataModels.stream()
                                    .filter(e -> tempModels1.stream().map(TempModel::getDocTypes).anyMatch(name -> name.equals(e.getProducerName())))
                                    .collect(Collectors.toList());


                    List<Custom_InboxDataModel> docTypesOutput =
                            companiesOutput.stream()
                                    .filter(e -> tempModels2.stream().map(TempModel::getDocTypes).anyMatch(name -> name.equals(e.getDocumentCategroyId())))
                                    .collect(Collectors.toList());

                    List<Custom_InboxDataModel> resipsOutput =
                            docTypesOutput.stream()
                                    .filter(e -> tempModels3.stream().map(TempModel::getDocTypes).anyMatch(name -> name.equals(e.getRecipientName())))
                                    .collect(Collectors.toList());

                    filterOutput.addAll(resipsOutput);

                }


                // for company and doctype filter
                else if (tempModels1.size() > 0 && tempModels2.size() > 0) {
                    MyApplication.MyLogi("check filter111", "CD");

                    List<Custom_InboxDataModel> companiesOutput =
                            custom_inboxDataModels.stream()
                                    .filter(e -> tempModels1.stream().map(TempModel::getDocTypes).anyMatch(name -> name.equals(e.getProducerName())))
                                    .collect(Collectors.toList());


                    List<Custom_InboxDataModel> docTypesOutput =
                            companiesOutput.stream()
                                    .filter(e -> tempModels2.stream().map(TempModel::getDocTypes).anyMatch(name -> name.equals(e.getDocumentCategroyId())))
                                    .collect(Collectors.toList());


                    filterOutput.addAll(docTypesOutput);

                }


                // for doctype and recipent filter
                else if (tempModels2.size() > 0 && tempModels3.size() > 0) {

                    MyApplication.MyLogi("check filter111", "DR");
                    List<Custom_InboxDataModel> docTypesOutput =
                            custom_inboxDataModels.stream()
                                    .filter(e -> tempModels2.stream().map(TempModel::getDocTypes).anyMatch(name -> name.equals(e.getDocumentCategroyId())))
                                    .collect(Collectors.toList());

                    List<Custom_InboxDataModel> resipsOutput =
                            docTypesOutput.stream()
                                    .filter(e -> tempModels3.stream().map(TempModel::getDocTypes).anyMatch(name -> name.equals(e.getRecipientName())))
                                    .collect(Collectors.toList());


                    filterOutput.addAll(resipsOutput);

                }


                // for company and recipent filter
                else if (tempModels1.size() > 0 && tempModels3.size() > 0) {

                    MyApplication.MyLogi("check filter111", "CR");
                    List<Custom_InboxDataModel> companiesOutput =
                            custom_inboxDataModels.stream()
                                    .filter(e -> tempModels1.stream().map(TempModel::getDocTypes).anyMatch(name -> name.equals(e.getProducerName())))
                                    .collect(Collectors.toList());


                    List<Custom_InboxDataModel> resipsOutput =
                            companiesOutput.stream()
                                    .filter(e -> tempModels3.stream().map(TempModel::getDocTypes).anyMatch(name -> name.equals(e.getRecipientName())))
                                    .collect(Collectors.toList());


                    filterOutput.addAll(resipsOutput);

                }


                // for company filter
                else if (tempModels1.size() > 0) {
                    MyApplication.MyLogi("check filter111", "C");
                    List<Custom_InboxDataModel> companiesOutput =
                            custom_inboxDataModels.stream()
                                    .filter(e -> tempModels1.stream().map(TempModel::getDocTypes).anyMatch(name -> name.equals(e.getProducerName())))
                                    .collect(Collectors.toList());


                    filterOutput.addAll(companiesOutput);

                }


                // for dovtype filter
                else if (tempModels2.size() > 0) {

                    MyApplication.MyLogi("check filter111", "D");
                    List<Custom_InboxDataModel> docTypesOutput =
                            custom_inboxDataModels.stream()
                                    .filter(e -> tempModels2.stream().map(TempModel::getDocTypes).anyMatch(name -> name.equals(e.getDocumentCategroyId())))
                                    .collect(Collectors.toList());


                    filterOutput.addAll(docTypesOutput);

                }


                // for recipent filter
                else if (tempModels3.size() > 0) {
                    MyApplication.MyLogi("check filter111", "R");

                    List<Custom_InboxDataModel> resipsOutput =
                            custom_inboxDataModels.stream()
                                    .filter(e -> tempModels3.stream().map(TempModel::getDocTypes).anyMatch(name -> name.equals(e.getRecipientName())))
                                    .collect(Collectors.toList());

                    filterOutput.addAll(resipsOutput);

                }


                documents.clear();
                documents.addAll(filterOutput);
                inboxListAdapter.notifyDataSetChanged();

                MyApplication.MyLogi("size3434", "" + filterOutput.size());


            } else {
                selCompaniesList.clear();
                selDocTypesList.clear();
                selResipsList.clear();

                documents.clear();
                documents.addAll(custom_inboxDataModels);
                inboxListAdapter.notifyDataSetChanged();
            }


        } else if (resultCode == 602) {
            sortInboxItemList = new APIQueries().getSortOptionBycatName("Inbox");
            /*for (int k = 0; k < sortInboxItemList.size(); k++) {

                if (sortInboxItemList.get(k).isSelected()) {
                    MyApplication.MyLogi("sort op34", "" + sortInboxItemList.get(k).getCaption());
                }
            }*/

            sortInbox();
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
                Custom_InboxDataModel temp_document = documents.get(docPos);
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
                Log.i("res1321", "" + s);

                if (responce_Str.equals(Constant.SUCCESS_MSG)) {

                    Toast.makeText(mContext, "" + Constant.Message.FOLDER_MOVE_SUCCESS, Toast.LENGTH_SHORT).show();
                    new APIQueries().updateLocationByTransmissionId(transmissionId, displayLocation);
                    documents.remove(docPos);
                    inboxListAdapter.notifyDataSetChanged();


                   /* for(int n=0;n<custom_folderList.size();n++){

                        Custom_FolderModel custom_folderModel=custom_folderList.get(n);

                        if(custom_folderModel.isSelected()){
                            BoxStore boxStore = MyApplication.getInstance().getBoxStore();
                            Box<Folder> folderBox = boxStore.boxFor(Folder.class);
                            Box<com.sixd.ecomail.model.Document> documentBox = boxStore.boxFor(com.sixd.ecomail.model.Document.class);

                            String folderid = custom_folderModel.getFolderId();
                            Folder folder=new APIQueries().getFolderByID(folderid);
                            Document  document=new APIQueries().getDocumentByID(documents.get(docPos).getDocumentID());

                            folder.documentToMany.add(document);
                            folderBox.put(folder);
                            document.folderToOne.setTarget(folder);
                            documentBox.put(document);
                        }

                    }

*/

                } else {
                    Toast.makeText(mContext, "" + responce_Str, Toast.LENGTH_SHORT).show();
                }


            }
        }.execute();

    }


    public void sortInbox() {
        List<InboxSortOrder> sortInboxItemList = new APIQueries().getSortOptionBycatName("Inbox");

        Collections.sort(sortInboxItemList, new Comparator<InboxSortOrder>() {
            @Override
            public int compare(InboxSortOrder inboxSortOrder, InboxSortOrder t1) {
                return (int) (inboxSortOrder.getDefaultOrder() - t1.getDefaultOrder());
            }
        });

        for (int t = 0; t < sortInboxItemList.size(); t++) {
            InboxSortOrder inboxSortOrder = sortInboxItemList.get(t);

            if (inboxSortOrder.getCaption().equals("Company") && inboxSortOrder.isSelected()) {
                Collections.sort(custom_inboxDataModels, new Comparator<Custom_InboxDataModel>() {
                    @Override
                    public int compare(Custom_InboxDataModel custom_inboxDataModel, Custom_InboxDataModel t1) {
                        return custom_inboxDataModel.getProducerName().compareTo(t1.getProducerName());
                    }
                });
            } else if (inboxSortOrder.getCaption().equals("Document Date") && inboxSortOrder.isSelected()) {
                MyApplication.MyLogi("in121", "doc date");
                Collections.sort(custom_inboxDataModels, new Comparator<Custom_InboxDataModel>() {
                    @Override
                    public int compare(Custom_InboxDataModel custom_inboxDataModel, Custom_InboxDataModel t1) {

                        Date date1 = dateConvert(custom_inboxDataModel.getDocumentDate());
                        Date date2 = dateConvert(t1.getDocumentDate());
                        long n1 = date1.getTime();
                        long n2 = date2.getTime();
                        if (n1 > n2) return -1;
                        else if (n1 < n2) return 1;
                        else return 0;

                    }
                });
            } else if (inboxSortOrder.getCaption().equals("Document Received") && inboxSortOrder.isSelected()) {
                Collections.sort(custom_inboxDataModels, new Comparator<Custom_InboxDataModel>() {
                    @Override
                    public int compare(Custom_InboxDataModel custom_inboxDataModel, Custom_InboxDataModel t1) {
                        Date date1 = dateConvert(custom_inboxDataModel.getContentCreationDate());
                        Date date2 = dateConvert(t1.getContentCreationDate());
                        long n1 = date1.getTime();
                        long n2 = date2.getTime();
                        if (n1 > n2) return -1;
                        else if (n1 < n2) return 1;
                        else return 0;
                    }
                });
            } else if (inboxSortOrder.getCaption().equals("Document Type") && inboxSortOrder.isSelected()) {
                Collections.sort(custom_inboxDataModels, new Comparator<Custom_InboxDataModel>() {
                    @Override
                    public int compare(Custom_InboxDataModel custom_inboxDataModel, Custom_InboxDataModel t1) {
                        return custom_inboxDataModel.getDocumentName().toLowerCase().compareTo(t1.getDocumentName().toLowerCase());
                    }
                });
            }

        }

        if (inboxListAdapter != null)
            inboxListAdapter.notifyDataSetChanged();
    }

    public Date dateConvert(String dateStr) {
        Date datetemp = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            datetemp = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return datetemp;
    }



    public void folderListDialog(int position){
        makeFolderList();
        final Dialog accountTypeDailog = new Dialog(getContext(),R.style.AppTheme);
        accountTypeDailog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        accountTypeDailog.setContentView(R.layout.folderlistdialog);
        accountTypeDailog.setCancelable(false);

        TextView foldertype_title=accountTypeDailog.findViewById(R.id.foldertype_title);
        ImageView img_back=accountTypeDailog.findViewById(R.id.img_back);
        ImageView img_save=accountTypeDailog.findViewById(R.id.img_save);
        RecyclerView folderlist=accountTypeDailog.findViewById(R.id.folderlist);

        foldertype_title.setText("Archive");


        FolderListDialogAdapter folderListDialogAdapter = new FolderListDialogAdapter(mContext,custom_folderList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        folderlist.setLayoutManager(layoutManager);
        folderlist.setItemAnimator(new DefaultItemAnimator());
        folderlist.setAdapter(folderListDialogAdapter);


        img_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveDocumnet("ARCHIVE", position);
                accountTypeDailog.dismiss();
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accountTypeDailog.dismiss();
            }
        });

        accountTypeDailog.show();
    }

    List<Custom_FolderModel> custom_folderList = new ArrayList<>();
    public void makeFolderList() {
        custom_folderList.clear();
        BoxStore boxStore = MyApplication.getInstance().getBoxStore();
        Box<Folder> folderBox = boxStore.boxFor(Folder.class);
        List<Folder> temp_list1 = folderBox.getAll();
        List<Folder> temp_list = new ArrayList<>();
        temp_list.addAll(temp_list1.stream().sorted(Comparator.comparing(Folder::getSequenceNo)).collect(Collectors.toList()));

        for (int i = 0; i < temp_list.size(); i++) {

            Folder folder = temp_list.get(i);

            String folderId = folder.getFolderId();
            String folderName = folder.getFolderName();
            int sequenceNo = folder.getSequenceNo();
            boolean isSelected = false;
            boolean isSmartFolder = false;
            List<Document> folderDocs = folder.documentToMany;
            int docCount = folderDocs.size();
            Custom_FolderModel custom_folderModel = new Custom_FolderModel(folderId, folderName, sequenceNo, isSelected, isSmartFolder, docCount);
            custom_folderModel.setSmartDocs(folderDocs);
            custom_folderList.add(custom_folderModel);


        }
    }


    public void payModifyDialog(int position){
        makeFolderList();
        final Dialog  payModifyDailog = new Dialog(getContext(),R.style.AppTheme);
        payModifyDailog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        payModifyDailog.setContentView(R.layout.pay_modify_view);
        payModifyDailog.setCancelable(false);

        ImageView img_back=payModifyDailog.findViewById(R.id.img_back);
        TextView txt_amount=payModifyDailog.findViewById(R.id.txt_amount);
        TextView txt_dueDate=payModifyDailog.findViewById(R.id.txt_dueDate);
        TextView txt_fullpay=payModifyDailog.findViewById(R.id.txt_fullpay);
        TextView txt_minpay=payModifyDailog.findViewById(R.id.txt_minpay);
        TextView txt_otherpayamount=payModifyDailog.findViewById(R.id.txt_otherpayamount);
        TextView txt_todayDate=payModifyDailog.findViewById(R.id.txt_todayDate);
        TextView txt_minDate=payModifyDailog.findViewById(R.id.txt_minDate);
        TextView txt_setDate=payModifyDailog.findViewById(R.id.txt_setDate);
        TextView txt_cardname=payModifyDailog.findViewById(R.id.txt_cardname);
        TextView txt_changeCard=payModifyDailog.findViewById(R.id.txt_changeCard);
        TextView txt_creditAnount=payModifyDailog.findViewById(R.id.txt_creditAnount);

        LinearLayout btn_paylay=payModifyDailog.findViewById(R.id.btn_paylay);
        LinearLayout btn_updatetrashlay=payModifyDailog.findViewById(R.id.btn_updatetrashlay);

        Button btnPay=payModifyDailog.findViewById(R.id.btnPay);
        Button btnUpdate=payModifyDailog.findViewById(R.id.btnUpdate);
        Button btnTrash=payModifyDailog.findViewById(R.id.btnTrash);

        RadioButton radio_fullpay=payModifyDailog.findViewById(R.id.radio_fullpay);
        RadioButton radio_minpay=payModifyDailog.findViewById(R.id.radio_minpay);
        RadioButton radio_otherpayamount=payModifyDailog.findViewById(R.id.radio_otherpayamount);

        RadioButton radio_todayDate=payModifyDailog.findViewById(R.id.radio_todayDate);
        RadioButton radio_minDate=payModifyDailog.findViewById(R.id.radio_minDate);
        RadioButton radio_setDate=payModifyDailog.findViewById(R.id.radio_setDate);

        RadioButton radio_credite=payModifyDailog.findViewById(R.id.radio_credite);

        radio_fullpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCheckedRadioBtn(radio_fullpay,radio_minpay,radio_otherpayamount,0);
            }
        });

        radio_minpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCheckedRadioBtn(radio_fullpay,radio_minpay,radio_otherpayamount,1);
            }
        });

        radio_otherpayamount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCheckedRadioBtn(radio_fullpay,radio_minpay,radio_otherpayamount,2);
            }
        });


        radio_todayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCheckedRadioBtn(radio_todayDate,radio_minDate,radio_setDate,0);
            }
        });

        radio_minDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCheckedRadioBtn(radio_todayDate,radio_minDate,radio_setDate,1);
            }
        });

        radio_setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCheckedRadioBtn(radio_todayDate,radio_minDate,radio_setDate,2);
            }
        });


        Custom_InboxDataModel custom_inboxDataModels=documents.get(position);
        double amountDue = 0;
        double minimumAmountDue = 0;
        String actionDueDate = documents.get(position).getActionDueDate();
        try {
            amountDue = Double.parseDouble(documents.get(position).getAmountDue());
            minimumAmountDue = Double.parseDouble(documents.get(position).getMinimumAmountDue());

            txt_dueDate.setText("Due on " + dateConvert_pay(actionDueDate));
            txt_amount.setText(showPriceInUSD(amountDue));
            txt_minDate.setText("" + dateConvert_pay(actionDueDate));
            txt_fullpay.setText("" + showPriceInUSD(amountDue));
            txt_minpay.setText(showPriceInUSD(minimumAmountDue));

            Date now = new Date();
            SimpleDateFormat df = new SimpleDateFormat("MMM dd,yyyy", Locale.ENGLISH);
            String currentdateTimeStr = df.format(now);
            txt_todayDate.setText(""+currentdateTimeStr);


            List<FundingSources> fundingSources=new APIQueries().getFundingSources();
            if(fundingSources.size()>0){
                FundingSources sources=fundingSources.get(0);
                txt_cardname.setText(""+sources.getBankName());
                txt_creditAnount.setText(""+sources.getCurrentBalance());
            }


        } catch (NumberFormatException e) {
            e.printStackTrace();
        }


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payModifyDailog.dismiss();
            }
        });



        String paymentStateId=custom_inboxDataModels.getPaymentStateId();
        if(paymentStateId.equals("SCHEDULED")){
            btn_paylay.setVisibility(View.GONE);
            btn_updatetrashlay.setVisibility(View.VISIBLE);

        }

        else{
            btn_updatetrashlay.setVisibility(View.GONE);
            btn_paylay.setVisibility(View.VISIBLE);

        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext,"Update....",Toast.LENGTH_SHORT).show();

            }
        });

        payModifyDailog.show();
    }

    public static String showPriceInUSD(double balanceUSD) {
        // NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "in")); // for indian currency

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        String currencrStr = currencyFormat.format(balanceUSD);
        return currencrStr;

    }


    public String dateConvert_pay(String dateStr) {

        String[] dateStrs = dateStr.split(" ");
        String tempstr = dateStrs[0];

        String out = "";
        SimpleDateFormat month_date = new SimpleDateFormat("MMM dd,yyyy", Locale.ENGLISH);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(tempstr);
            out = month_date.format(date);
            MyApplication.MyLogi("out utc 32", "" + out);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return out;
    }

    public void setCheckedRadioBtn(RadioButton radio1,RadioButton radio2,RadioButton radio3,int checked){

        if(checked==0){
            radio1.setChecked(true);
            radio2.setChecked(false);
            radio3.setChecked(false);
        }
        else if(checked==1){
            radio1.setChecked(false);
            radio2.setChecked(true);
            radio3.setChecked(false);
        }
        else if(checked==2){
            radio1.setChecked(false);
            radio2.setChecked(false);
            radio3.setChecked(true);
        }

    }


}
