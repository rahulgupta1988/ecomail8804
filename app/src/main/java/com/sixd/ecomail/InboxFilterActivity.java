package com.sixd.ecomail;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sixd.ecomail.APIManager.APIQueries;
import com.sixd.ecomail.Utility.Constant;
import com.sixd.ecomail.Utility.MyApplication;
import com.sixd.ecomail.adapter.InboxCompanyFilterAdapter;
import com.sixd.ecomail.adapter.InboxDocTypeFilterAdapter;
import com.sixd.ecomail.adapter.InboxRecipientFilterAdapter;
import com.sixd.ecomail.model.DocTypes;
import com.sixd.ecomail.model.Producer;
import com.sixd.ecomail.model.Recipents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Rahul Gupta on 09-05-2018.
 */
public class InboxFilterActivity extends AppCompatActivity {

    Context mContext;
    RelativeLayout company_lay, doctype_lay, recip_lay;
    ImageView img_save, img_back;
    TextView txtCompanyCount, txtDocTypeCount, txtRecipintCount;
    Button btnClearfilters;

    List<Producer> producerList = null;
    List<DocTypes> documentTypeList = null;
    List<Recipents> recipentsArrayList = null;


    ArrayList<String> selCompanies = new ArrayList<>();
    ArrayList<String> selDocTypes = new ArrayList<>();
    ArrayList<String> selResips = new ArrayList<>();
    HashMap<String, List<String>> stringListMap = new HashMap<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int themeID=MyApplication.getInstance().getThemeID();
        setTheme(themeID);
        setContentView(R.layout.activity_inboxfilter);
        mContext = this;

        Intent intent=getIntent();
        if(intent !=null){
            selCompanies= (ArrayList<String>) intent.getSerializableExtra("selCompanies");
            selDocTypes= (ArrayList<String>) intent.getSerializableExtra("selDocTypes");
            selResips=(ArrayList<String>) intent.getSerializableExtra("selResips");

        }

        recipentsArrayList = new APIQueries().getRecipents();



        for(int k=0;k<selResips.size();k++){

            String str=selResips.get(k);
            for(int l=0;l<recipentsArrayList.size();l++) {
                if (str.equals(recipentsArrayList.get(l).getRecipientName())){
                    recipentsArrayList.get(l).setSelected(true);
                }
            }
        }



        /*List<Recipents> unavailable = recipentsArrayList.stream()
                .filter(e -> (selResips.stream()
                        .filter(d -> d.toString().equals(e));*/


        createCompanyLIst();
        createDocTypeList();
        initViews();


    }

    public void initViews() {
        btnClearfilters=findViewById(R.id.btnClearfilters);
        txtCompanyCount = findViewById(R.id.txtCompanyCount);
        txtDocTypeCount = findViewById(R.id.txtDocTypeCount);
        txtRecipintCount = findViewById(R.id.txtRecipintCount);
        img_save = findViewById(R.id.img_save);
        img_back = findViewById(R.id.img_back);
        company_lay = findViewById(R.id.company_lay);
        doctype_lay = findViewById(R.id.doctype_lay);
        recip_lay = findViewById(R.id.recip_lay);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        img_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(selCompanies.size()!=0 || selDocTypes.size()!=0 || selResips.size()!=0) {
                    stringListMap.put("selCompanies", selCompanies);
                    stringListMap.put("selDocTypes", selDocTypes);
                    stringListMap.put("selResips", selResips);

                    Intent intent = new Intent();
                    intent.putExtra("selFilters", stringListMap);
                    setResult(601, intent);
                    finish();//finishing activity
                }

                else{

                    Toast.makeText(mContext,""+ Constant.Message.FILTER_MSG,Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnClearfilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stringListMap.clear();
                Intent intent = new Intent();
                intent.putExtra("selFilters", stringListMap);
                setResult(601, intent);
                finish();//finishing activity
            }
        });

        company_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                companyFilterDialog();
            }
        });
        doctype_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docTypeFilterDialog();
            }
        });

        recip_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recipentFilterDialog();
            }
        });

        txtCompanyCount.setText("" + selCompanies.size()+"/"+producerList.size());
        txtDocTypeCount.setText("" + selDocTypes.size()+"/"+documentTypeList.size());
        txtRecipintCount.setText("" + selResips.size()+"/"+recipentsArrayList.size());




    }


    public void companyFilterDialog() {
        int themeID=MyApplication.getInstance().getThemeID();
        final Dialog companyDailog = new Dialog(mContext, themeID);
        companyDailog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        companyDailog.setContentView(R.layout.companyfilterdialogview);
        companyDailog.setCancelable(true);

        ImageView img_back = companyDailog.findViewById(R.id.img_back);
        ImageView img_save = companyDailog.findViewById(R.id.img_save);
        RecyclerView recyleCompanyList = companyDailog.findViewById(R.id.recyleCompanyList);

        InboxCompanyFilterAdapter folderAdapter = new InboxCompanyFilterAdapter(mContext, producerList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyleCompanyList.setLayoutManager(layoutManager);
        recyleCompanyList.setItemAnimator(new DefaultItemAnimator());
        recyleCompanyList.setAdapter(folderAdapter);


        img_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selCompanies.clear();
                // foer copanies
                /*for(int p=0;p<producerList.size();p++) {
                    MyApplication.MyLogi("90887",""+producerList.get(p).isSelected());
                    if (producerList.get(p).isSelected())
                        selCompanies.add(producerList.get(p).getProducerName());
                }*/

                List<String> temp_selCompanies =producerList.stream().filter(o -> o.isSelected()==true).map(Producer::getProducerName).collect(Collectors.toList());
                selCompanies.addAll(temp_selCompanies);


                txtCompanyCount.setText("" + selCompanies.size()+"/"+producerList.size());
                companyDailog.dismiss();
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                companyDailog.dismiss();
            }
        });

        companyDailog.show();
    }


    public void docTypeFilterDialog() {

        int themeID=MyApplication.getInstance().getThemeID();
        final Dialog companyDailog = new Dialog(mContext, themeID);
        companyDailog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        companyDailog.setContentView(R.layout.doctypfilterdialogview);
        companyDailog.setCancelable(true);

        ImageView img_back = companyDailog.findViewById(R.id.img_back);
        ImageView img_save = companyDailog.findViewById(R.id.img_save);
        RecyclerView recyleDocTypList = companyDailog.findViewById(R.id.recyleDocTypList);

        InboxDocTypeFilterAdapter folderAdapter = new InboxDocTypeFilterAdapter(mContext, documentTypeList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyleDocTypList.setLayoutManager(layoutManager);
        recyleDocTypList.setItemAnimator(new DefaultItemAnimator());
        recyleDocTypList.setAdapter(folderAdapter);


        img_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selDocTypes.clear();
                // foer DocTypes
                /*for(int p=0;p<documentTypeList.size();p++) {
                    if (documentTypeList.get(p).isSelected())
                        selDocTypes.add(documentTypeList.get(p).getDocumentType());
                }*/

                List<String> temp_selDocTypes =documentTypeList.stream().filter(o -> o.isSelected()==true).map(DocTypes::getDocumentType).collect(Collectors.toList());
                selDocTypes.addAll(temp_selDocTypes);

                txtDocTypeCount.setText("" + selDocTypes.size()+"/"+documentTypeList.size());
                companyDailog.dismiss();
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                companyDailog.dismiss();
            }
        });

        companyDailog.show();
    }


    public void recipentFilterDialog() {
        int themeID=MyApplication.getInstance().getThemeID();
        final Dialog companyDailog = new Dialog(mContext, themeID);
        companyDailog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        companyDailog.setContentView(R.layout.recipfilterdialogview);
        companyDailog.setCancelable(true);

        ImageView img_back = companyDailog.findViewById(R.id.img_back);
        ImageView img_save = companyDailog.findViewById(R.id.img_save);
        RecyclerView recyleRecipList = companyDailog.findViewById(R.id.recyleRecipList);

        InboxRecipientFilterAdapter inboxRecipientFilterAdapter = new InboxRecipientFilterAdapter(mContext, recipentsArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyleRecipList.setLayoutManager(layoutManager);
        recyleRecipList.setItemAnimator(new DefaultItemAnimator());
        recyleRecipList.setAdapter(inboxRecipientFilterAdapter);


        img_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selResips.clear();
                // foer Recipents
              /*  for(int p=0;p<recipentsArrayList.size();p++)
                    if(recipentsArrayList.get(p).isSelected()) selResips.add(recipentsArrayList.get(p).getRecipientName());
*/
                List<String> temp_selResips =recipentsArrayList.stream().filter(o -> o.isSelected()==true).map(Recipents::getRecipientName).collect(Collectors.toList());
                selResips.addAll(temp_selResips);



                txtRecipintCount.setText("" + selResips.size()+"/"+recipentsArrayList.size());
                companyDailog.dismiss();
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                companyDailog.dismiss();
            }
        });

        companyDailog.show();
    }

    public void createDocTypeList() {
        documentTypeList = new ArrayList<>();
        List<DocTypes> documentTypeList_temp = new APIQueries().getDocumentTypes();

        for(int k=0;k<selDocTypes.size();k++){

            String str=selDocTypes.get(k);
            for(int l=0;l<documentTypeList_temp.size();l++) {
                if (str.equals(documentTypeList_temp.get(l).getDocumentType())){
                    documentTypeList_temp.get(l).setSelected(true);
                }
            }
        }



        Collections.sort(documentTypeList_temp, new Comparator<DocTypes>() {
            @Override
            public int compare(DocTypes docTypes, DocTypes t1) {
                return docTypes.getDocumentTypeName().compareTo(t1.getDocumentTypeName());
            }
        });


        for (int m = 0; m < documentTypeList_temp.size(); m++) {

            if (m != documentTypeList_temp.size() - 1) {

                if (!(documentTypeList_temp.get(m).getDocumentTypeName().equals(documentTypeList_temp.get(m + 1).getDocumentTypeName()))) {
                    documentTypeList.add(documentTypeList_temp.get(m));
                }
            }
        }
    }


    public void createCompanyLIst() {
        producerList = new APIQueries().getProducers();



        for(int k=0;k<selCompanies.size();k++){

            String str=selCompanies.get(k);
            for(int l=0;l<producerList.size();l++) {
                if (str.equals(producerList.get(l).getProducerName())){
                    producerList.get(l).setSelected(true);
                }
            }
        }



        Collections.sort(producerList, new Comparator<Producer>() {
            @Override
            public int compare(Producer producer, Producer t1) {
                return producer.getProducerName().compareTo(t1.getProducerName());
            }
        });


        String lastCh = "";
        for (int i = 0; i < producerList.size(); i++) {

            String isFirst = "";
            String firstCh = producerList.get(i).getProducerName().substring(0, 1);
            MyApplication.MyLogi("3998 bool", "" + lastCh.isEmpty());

            if (!lastCh.isEmpty()) {
                if (lastCh.equals(firstCh)) {
                    isFirst = "1";
                    lastCh = firstCh;

                } else {

                    String firstCh1 = producerList.get(i).getProducerName().substring(0, 1);
                    String firstCh2 = "";
                    String firstCh3 = "";
                    String firstCh4 = "";
                    if (producerList.size() >= (i - 1))
                        firstCh2 = producerList.get(i - 1).getProducerName().substring(0, 1);

                    if (producerList.size() >= (i + 2))
                        firstCh3 = producerList.get(i + 1).getProducerName().substring(0, 1);

                    if (producerList.size() >= (i - 2))
                        firstCh4 = producerList.get(i - 2).getProducerName().substring(0, 1);


                    if (producerList.size() >= (i + 2)) {
                        if (firstCh1.equals(firstCh3)) {

                            isFirst = "0";
                        } else {
                            isFirst = "02";
                        }
                    } else {
                        isFirst = "02";
                    }


                    if (producerList.size() >= (i - 1) && producerList.size() >= (i - 2)) {
                        if (firstCh2.equals(firstCh4)) {
                            producerList.get(i - 1).setIsFirst("2");
                        }
                    }

                    lastCh = firstCh;


                }
            } else {
                isFirst = "0";
                lastCh = firstCh;

            }

            producerList.get(i).setIsFirst(isFirst);


        }




    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
