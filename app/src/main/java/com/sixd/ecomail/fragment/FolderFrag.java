package com.sixd.ecomail.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sixd.ecomail.APIManager.APIQueries;
import com.sixd.ecomail.APIManager.AddNormalFolderManager;
import com.sixd.ecomail.APIManager.AddSmartFolderManager;
import com.sixd.ecomail.AddNormalFolderActivity;
import com.sixd.ecomail.AddSmartFolderActivity;
import com.sixd.ecomail.FolderDocsActivity;
import com.sixd.ecomail.HomeActivity;
import com.sixd.ecomail.R;
import com.sixd.ecomail.Utility.Constant;
import com.sixd.ecomail.Utility.DragNDropHelper;
import com.sixd.ecomail.Utility.MyApplication;
import com.sixd.ecomail.Utility.ProgressLoaderUtilities;
import com.sixd.ecomail.adapter.FoldersAdapter;
import com.sixd.ecomail.adapter.SortFoldersAdapter;
import com.sixd.ecomail.model.Custom_FolderModel;
import com.sixd.ecomail.model.DocTypes;
import com.sixd.ecomail.model.Document;
import com.sixd.ecomail.model.DocumentGroup;
import com.sixd.ecomail.model.Folder;
import com.sixd.ecomail.model.Recipents;
import com.sixd.ecomail.model.SmartFolder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import io.objectbox.Box;
import io.objectbox.BoxStore;

/**
 * Created by Rahul Gupta on 08-04-2018.
 */

public class FolderFrag extends Fragment {

    View view;
    Context mContext;
    ImageView imgFilter;
    TextView txtArchiveCount, txtTrashCount;
    RecyclerView recyleFolders;
    FloatingActionButton fabAdd, fab_1, fab_2;
    RelativeLayout archivelay, trashlay;

    ArrayList<Custom_FolderModel> sortFolderArrayList = new ArrayList<>();
    Animation show_fab, hide_fab, show_fab2, hide_fab2, rotatefab;
    List<Custom_FolderModel> custom_folderList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.folder_fragment, null);
        sortFolderArrayList.clear();
        makeFolderList();

        initViews();
        setFoldersList();
        floatFlag = 0;
        return view;


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    int floatFlag = 0;

    public void initViews() {
        archivelay = view.findViewById(R.id.archivelay);
        txtArchiveCount = view.findViewById(R.id.txtArchiveCount);
        trashlay = view.findViewById(R.id.trashlay);
        txtTrashCount = view.findViewById(R.id.txtTrashCount);
        fabAdd = view.findViewById(R.id.fabAdd);
        fab_1 = view.findViewById(R.id.fab_1);
        fab_2 = view.findViewById(R.id.fab_2);
        imgFilter = view.findViewById(R.id.imgFilter);
        recyleFolders = view.findViewById(R.id.recyleFolders);
        imgFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortFolderDialog();
            }
        });

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (floatFlag == 0) {
                    rotate(0, -45);
                    showFloatingMenu();
                    floatFlag = 1;
                } else {
                    rotate(-45, 0);
                    hideFloatingMenu();
                    floatFlag = 0;
                }


            }
        });


        rotatefab = AnimationUtils.loadAnimation(mContext, R.anim.rotatefab);
        show_fab = AnimationUtils.loadAnimation(mContext, R.anim.fab_show);
        hide_fab = AnimationUtils.loadAnimation(mContext, R.anim.fab_hide);

        show_fab2 = AnimationUtils.loadAnimation(mContext, R.anim.fab2_show);
        hide_fab2 = AnimationUtils.loadAnimation(mContext, R.anim.fab2_hide);


        fab_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int maxSequence = 0;
                try {
                    Custom_FolderModel maxSequenceFolder = custom_folderList
                            .stream()
                            .max(Comparator.comparing(Custom_FolderModel::getSequenceNo))
                            .orElseThrow(NoSuchElementException::new);
                    maxSequence = maxSequenceFolder.getSequenceNo();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }

                Intent normalFolderIntent = new Intent(mContext, AddNormalFolderActivity.class);
                normalFolderIntent.putExtra("maxSequence", maxSequence);
                normalFolderIntent.putExtra("addoredit", "1");
                startActivityForResult(normalFolderIntent, 2);
            }
        });

        fab_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int maxSequence = 0;
                try {
                    Custom_FolderModel maxSequenceFolder = custom_folderList
                            .stream()
                            .max(Comparator.comparing(Custom_FolderModel::getSequenceNo))
                            .orElseThrow(NoSuchElementException::new);
                    maxSequence = maxSequenceFolder.getSequenceNo();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }

                Intent smartlFolderIntent = new Intent(mContext, AddSmartFolderActivity.class);
                smartlFolderIntent.putExtra("maxSequence", maxSequence);
                smartlFolderIntent.putExtra("addoredit", "1");
                startActivityForResult(smartlFolderIntent, 2);

            }
        });

        archivelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setArchiveList();
            }
        });

        trashlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTrashList();
            }
        });


        archive_documents = new APIQueries().getDocsByLocation("ARCHIVE");
        txtArchiveCount.setText("" + archive_documents.size());

        trash_documents = new APIQueries().getDocsByLocation("TRASH");
        txtTrashCount.setText("" + trash_documents.size());

    }

    FoldersAdapter folderListAdapter = null;


    public void setFoldersList() {
        folderListAdapter = new FoldersAdapter(mContext, custom_folderList,FolderFrag.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyleFolders.setLayoutManager(layoutManager);
        recyleFolders.setItemAnimator(new DefaultItemAnimator());
        recyleFolders.setAdapter(folderListAdapter);
    }


    public void makeFolderList() {
        custom_folderList.clear();
        BoxStore boxStore = MyApplication.getInstance().getBoxStore();
        Box<Folder> folderBox = boxStore.boxFor(Folder.class);
        List<Folder> temp_list1 = folderBox.getAll();
        List<Folder> temp_list = new ArrayList<>();
        temp_list.addAll(temp_list1.stream().sorted(Comparator.comparing(Folder::getSequenceNo)).collect(Collectors.toList()));


        Box<SmartFolder> smartFolderBox = boxStore.boxFor(SmartFolder.class);
        List<SmartFolder> temp_smart_list1 = smartFolderBox.getAll();
        List<SmartFolder> temp_smart_list = new ArrayList<>();
        temp_smart_list.addAll(temp_smart_list1.stream().sorted(Comparator.comparing(SmartFolder::getOrder)).collect(Collectors.toList()));


        for (int j = 0; j < temp_smart_list.size(); j++) {

            SmartFolder smartFolder = temp_smart_list.get(j);

            String folderId = smartFolder.getSmartFolderId();
            String folderName = smartFolder.getSmartFolderName();
            int sequenceNo = smartFolder.getOrder();
            boolean isSelected = false;
            boolean isSmartFolder = true;

            List<Document> smartDocs = smartFolderDocsFilter(smartFolder);
            int docCount = smartDocs.size();
            Custom_FolderModel custom_folderModel = new Custom_FolderModel(folderId, folderName, sequenceNo, isSelected, isSmartFolder, docCount);
            custom_folderModel.setSmartDocs(smartDocs);
            custom_folderList.add(custom_folderModel);

        }


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

    Dialog recipentsDailog = null;
    SortFoldersAdapter folderAdapter = null;

    public void sortFolderDialog() {
        sortFolderArrayList.clear();
        sortFolderArrayList.addAll(custom_folderList);
        recipentsDailog = new Dialog(mContext, R.style.AppTheme);
        recipentsDailog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        recipentsDailog.setContentView(R.layout.foldersortview);
        recipentsDailog.setCancelable(false);

        ImageView img_back = recipentsDailog.findViewById(R.id.img_back);
        ImageView img_save = recipentsDailog.findViewById(R.id.img_save);
        RecyclerView recyleSortFolders = recipentsDailog.findViewById(R.id.recyleSortFolders);

        folderAdapter = new SortFoldersAdapter(mContext, sortFolderArrayList, FolderFrag.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyleSortFolders.setLayoutManager(layoutManager);
        recyleSortFolders.setItemAnimator(new DefaultItemAnimator());

        DragNDropHelper dragNDropHelper = new DragNDropHelper(folderAdapter, mContext);
        ItemTouchHelper touchHelper = new ItemTouchHelper(dragNDropHelper);
        folderAdapter.setTouchHelper(touchHelper);
        recyleSortFolders.setAdapter(folderAdapter);
        touchHelper.attachToRecyclerView(recyleSortFolders);


        img_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                custom_folderList.clear();
                custom_folderList.addAll(sortFolderArrayList);
                folderListAdapter.notifyDataSetChanged();
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

    public void showFloatingMenu() {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) fab_1.getLayoutParams();
        layoutParams.rightMargin += (int) (fab_1.getWidth() * 1.9);
        layoutParams.bottomMargin += (int) (fab_1.getHeight() * 0.25);
        fab_1.setLayoutParams(layoutParams);
        fab_1.startAnimation(show_fab);
        fab_1.setClickable(true);
        fab_1.setVisibility(View.VISIBLE);


        FrameLayout.LayoutParams layoutParams1 = (FrameLayout.LayoutParams) fab_2.getLayoutParams();
        layoutParams1.rightMargin += (int) (fab_2.getWidth() * 1.1);
        layoutParams1.bottomMargin += (int) (fab_2.getHeight() * 1.5);
        fab_2.setLayoutParams(layoutParams1);
        fab_2.startAnimation(show_fab2);
        fab_2.setClickable(true);
        fab_2.setVisibility(View.VISIBLE);
    }

    public void hideFloatingMenu() {

        fab_1.startAnimation(hide_fab);
        fab_2.startAnimation(hide_fab2);


        hide_fab.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) fab_1.getLayoutParams();
                layoutParams.rightMargin -= (int) (fab_1.getWidth() * 1.9);
                layoutParams.bottomMargin -= (int) (fab_1.getHeight() * 0.25);
                fab_1.setLayoutParams(layoutParams);
                fab_1.setVisibility(View.INVISIBLE);
                fab_1.setClickable(false);

                FrameLayout.LayoutParams layoutParams1 = (FrameLayout.LayoutParams) fab_2.getLayoutParams();
                layoutParams1.rightMargin -= (int) (fab_2.getWidth() * 1.1);
                layoutParams1.bottomMargin -= (int) (fab_2.getHeight() * 1.5);
                fab_2.setLayoutParams(layoutParams1);
                fab_2.setClickable(false);
                fab_2.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    private void rotate(float fromdegree, float todegree) {
        AnimationSet animSet = new AnimationSet(true);
        animSet.setInterpolator(new DecelerateInterpolator());
        animSet.setFillAfter(true);
        animSet.setFillEnabled(true);

        final RotateAnimation animRotate = new RotateAnimation(fromdegree, todegree,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        animRotate.setDuration(100);
        animRotate.setFillAfter(true);
        animSet.addAnimation(animRotate);

        fabAdd.startAnimation(animSet);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 604) {
            makeFolderList();
            folderListAdapter.notifyDataSetChanged();
        } else if (resultCode == 605) {
            makeFolderList();
            folderListAdapter.notifyDataSetChanged();

            sortFolderArrayList.clear();
            sortFolderArrayList.addAll(custom_folderList);
            folderAdapter.notifyDataSetChanged();
        }

        else if (resultCode == 701) {
            sortFolderArrayList.clear();
            makeFolderList();
            initViews();
            setFoldersList();
            floatFlag = 0;

        }
    }


    public void removeFolder(String folderName, String consumerId, int sequenceNoInt, String folderID,boolean isSmartFolder) {
        final ProgressDialog[] progressDialog = {null};

        new AsyncTask<String, Void, String>() {

            String responce_Str = "";

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog[0] = new ProgressLoaderUtilities().getProgressDialog(mContext);
            }

            @Override
            protected String doInBackground(String... strings) {

                if(isSmartFolder)
                responce_Str = new AddSmartFolderManager(mContext).callRemoveFolderAPI(folderName, consumerId, sequenceNoInt, folderID);
                else  responce_Str = new AddNormalFolderManager(mContext).callRemoveFolderAPI(folderName, consumerId, sequenceNoInt, folderID);
                return responce_Str;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                progressDialog[0].dismiss();
                Log.i("res1321", "" + s);

                if (responce_Str.equals(Constant.SUCCESS_MSG)) {

                    new APIQueries().removeFolderByFolderID(folderID,isSmartFolder);

                    makeFolderList();
                    folderListAdapter.notifyDataSetChanged();

                    sortFolderArrayList.clear();
                    sortFolderArrayList.addAll(custom_folderList);
                    folderAdapter.notifyDataSetChanged();
                    Toast.makeText(mContext, "" + Constant.Message.FOLDER_REMOVE_SUCCESS, Toast.LENGTH_SHORT).show();


                } else {
                    Toast.makeText(mContext, "" + responce_Str, Toast.LENGTH_SHORT).show();
                }


            }

        }.execute();
    }


    public void editFolder(String folderName, int sequenceNoInt, String folderID,boolean isSmartFolder) {

        if(isSmartFolder){
            Intent normalFolderIntent = new Intent(mContext, AddSmartFolderActivity.class);
            normalFolderIntent.putExtra("maxSequence", sequenceNoInt);
            normalFolderIntent.putExtra("folderName", folderName);
            normalFolderIntent.putExtra("folderID", folderID);
            normalFolderIntent.putExtra("addoredit", "2");
            startActivityForResult(normalFolderIntent, 2);
        }

        else {
            Intent normalFolderIntent = new Intent(mContext, AddNormalFolderActivity.class);
            normalFolderIntent.putExtra("maxSequence", sequenceNoInt);
            normalFolderIntent.putExtra("folderName", folderName);
            normalFolderIntent.putExtra("folderID", folderID);
            normalFolderIntent.putExtra("addoredit", "2");
            startActivityForResult(normalFolderIntent, 2);
        }

    }


    public List<Document> smartFolderDocsFilter(SmartFolder temp_smart_folders) {

        List<Document> documents = new APIQueries().getDocsByLocation("INBOX");
        List<Document> smartDcosOutput = new ArrayList<>();


        List<DocTypes> tempModels1 = temp_smart_folders.docTypesToMany;
        List<Recipents> tempModels2 = temp_smart_folders.recipentsToMany;
        List<DocumentGroup> tempModels3 = temp_smart_folders.documentGroupToMany;


        List<Document> filteDocOut = new ArrayList<>();

        if (tempModels1.size() != 0 && tempModels2.size() != 0 && tempModels3.size() != 0) {
            List<Document> docTypeGPOutput = documents.stream()
                    .filter(e -> tempModels1.stream().map(DocTypes::getDocumentType).anyMatch(name -> name.equals(e.getDocumentCategroyId())))
                    .collect(Collectors.toList());


            List<Document> recipGPOutput = docTypeGPOutput.stream()
                    .filter(e -> tempModels2.stream().map(Recipents::getConsumerId).anyMatch(name -> name.equals(e.getRecipientCosumeId())))
                    .collect(Collectors.toList());


            List<Document> docGPGPOutput = recipGPOutput.stream()
                    .filter(e -> tempModels3.stream().map(DocumentGroup::getDocumentGroupId).anyMatch(name -> name.equals(e.getDocumentGroupId())))
                    .collect(Collectors.toList());

            filteDocOut.addAll(docGPGPOutput);
        } else if (tempModels1.size() != 0 && tempModels2.size() != 0) {

            List<Document> docTypeGPOutput = documents.stream()
                    .filter(e -> tempModels1.stream().map(DocTypes::getDocumentType).anyMatch(name -> name.equals(e.getDocumentCategroyId())))
                    .collect(Collectors.toList());


            List<Document> recipGPOutput = docTypeGPOutput.stream()
                    .filter(e -> tempModels2.stream().map(Recipents::getConsumerId).anyMatch(name -> name.equals(e.getRecipientCosumeId())))
                    .collect(Collectors.toList());


            filteDocOut.addAll(recipGPOutput);
        } else if (tempModels1.size() != 0 && tempModels3.size() != 0) {

            List<Document> docTypeGPOutput = documents.stream()
                    .filter(e -> tempModels1.stream().map(DocTypes::getDocumentType).anyMatch(name -> name.equals(e.getDocumentCategroyId())))
                    .collect(Collectors.toList());

            List<Document> docGPGPOutput = docTypeGPOutput.stream()
                    .filter(e -> tempModels3.stream().map(DocumentGroup::getDocumentGroupId).anyMatch(name -> name.equals(e.getDocumentGroupId())))
                    .collect(Collectors.toList());

            filteDocOut.addAll(docGPGPOutput);
        } else if (tempModels2.size() != 0 && tempModels3.size() != 0) {

            List<Document> recipGPOutput = documents.stream()
                    .filter(e -> tempModels2.stream().map(Recipents::getConsumerId).anyMatch(name -> name.equals(e.getRecipientCosumeId())))
                    .collect(Collectors.toList());


            List<Document> docGPGPOutput = recipGPOutput.stream()
                    .filter(e -> tempModels3.stream().map(DocumentGroup::getDocumentGroupId).anyMatch(name -> name.equals(e.getDocumentGroupId())))
                    .collect(Collectors.toList());

            filteDocOut.addAll(docGPGPOutput);
        } else if (tempModels1.size() != 0) {
            List<Document> docTypeGPOutput = documents.stream()
                    .filter(e -> tempModels1.stream().map(DocTypes::getDocumentType).anyMatch(name -> name.equals(e.getDocumentCategroyId())))
                    .collect(Collectors.toList());
            filteDocOut.addAll(docTypeGPOutput);
        } else if (tempModels2.size() != 0) {

            List<Document> recipGPOutput = documents.stream()
                    .filter(e -> tempModels2.stream().map(Recipents::getConsumerId).anyMatch(name -> name.equals(e.getRecipientCosumeId())))
                    .collect(Collectors.toList());

            filteDocOut.addAll(recipGPOutput);
        } else if (tempModels3.size() != 0) {

            List<Document> docGPGPOutput = documents.stream()
                    .filter(e -> tempModels3.stream().map(DocumentGroup::getDocumentGroupId).anyMatch(name -> name.equals(e.getDocumentGroupId())))
                    .collect(Collectors.toList());

            filteDocOut.addAll(docGPGPOutput);
        }


        List<Document> docDisplayOutput = filteDocOut.stream().filter(o -> o.getDisplayLocation().equals("INBOX")).collect(Collectors.toList());


        List<Folder> folderList = temp_smart_folders.folderToMany;

        if (folderList.size() != 0) {
            for (int p = 0; p < folderList.size(); p++) {
                Folder folder = folderList.get(p);
                List<Document> folderDocs = folder.documentToMany;
                List<Document> rtempPOutput = folderDocs.stream()
                        .filter(e -> docDisplayOutput.stream().map(Document::getDocumentId).anyMatch(name -> name.equals(e.getDocumentId())))
                        .collect(Collectors.toList());

                smartDcosOutput.addAll(rtempPOutput);
            }
        } else {
            smartDcosOutput.addAll(docDisplayOutput);
        }


        MyApplication.MyLogi("smart docs 4567", "" + smartDcosOutput.size());
        return smartDcosOutput;
    }

    List<Document> archive_documents = null;

    public void setArchiveList() {
        FoldersAdapter.temp_list.clear();
        FoldersAdapter.temp_list.addAll(archive_documents);
        Intent intent = new Intent(mContext, FolderDocsActivity.class);
        intent.putExtra("folderName", "Archive_false");
        startActivityForResult(intent,701);


    }

    List<Document> trash_documents = null;

    public void setTrashList() {
        FoldersAdapter.temp_list.clear();
        FoldersAdapter.temp_list.addAll(trash_documents);
        Intent intent = new Intent(mContext, FolderDocsActivity.class);
        intent.putExtra("folderName", "Trash_false");
        startActivityForResult(intent,701);


    }




}
