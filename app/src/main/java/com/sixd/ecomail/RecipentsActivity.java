package com.sixd.ecomail;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.sixd.ecomail.Utility.MyApplication;
import com.sixd.ecomail.adapter.RecipentsAdapter;
import com.sixd.ecomail.model.Folder;
import com.sixd.ecomail.model.Recipents;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;

/**
 * Created by Rahul Gupta on 18-04-2018.
 */

public class RecipentsActivity extends AppCompatActivity {

    Context mContext;
    RecyclerView recipentsList;
    FloatingActionButton fabAdd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipents);
        mContext=this;
        initViews();
        setRecipentsList();

    }

    public void initViews(){
        fabAdd=findViewById(R.id.fabAdd);
        recipentsList=findViewById(R.id.recipentsList);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recipentsAddDialog();
            }
        });
    }


    List<Recipents> recipentsArrayList=null;
    public void setRecipentsList(){
        BoxStore boxStore = MyApplication.getInstance().getBoxStore();
        Box<Recipents> folderBox = boxStore.boxFor(Recipents.class);
        recipentsArrayList=folderBox.getAll();


        RecipentsAdapter recipentsAdapter=new RecipentsAdapter(mContext,recipentsArrayList);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(mContext);
        recipentsList.setLayoutManager(layoutManager);
        recipentsList.setItemAnimator(new DefaultItemAnimator());
        recipentsList.setAdapter(recipentsAdapter);



        recipentsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fabAdd.getVisibility() == View.VISIBLE) {
                    fabAdd.hide();
                } else if (dy < 0 && fabAdd.getVisibility() != View.VISIBLE) {
                    fabAdd.show();
                }
            }
        });
    }


    public void recipentsAddDialog(){
        final Dialog recipentsDailog = new Dialog(this,R.style.AppTheme);
        recipentsDailog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        recipentsDailog.setContentView(R.layout.addrecipentsview);
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

    public void recipentsEditDialog(){
        final Dialog recipentsDailog = new Dialog(this,R.style.AppTheme);
        recipentsDailog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        recipentsDailog.setContentView(R.layout.editrecipentsview);
        recipentsDailog.setCancelable(false);

        ImageView img_back=recipentsDailog.findViewById(R.id.img_back);
        ImageView img_save=recipentsDailog.findViewById(R.id.img_save);
        final RadioButton radioMr=recipentsDailog.findViewById(R.id.radioMr);
        final RadioButton radioMrs=recipentsDailog.findViewById(R.id.radioMrs);
        final RadioButton radioDr=recipentsDailog.findViewById(R.id.radioDr);




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
