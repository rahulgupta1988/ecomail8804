package com.sixd.ecomail;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.sixd.ecomail.adapter.NewsAdapter;

/**
 * Created by Rahul Gupta on 26-04-2018.
 */

public class NewsNavigationActivity extends AppCompatActivity {

    Context mContext;
    RecyclerView newsList;
    ImageView img_back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsnavigation);
        mContext=this;
        initViews();
        setNewsList();
    }


    public void initViews(){
        newsList=findViewById(R.id.newsList);
        img_back=findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public  void setNewsList(){
        NewsAdapter newsAdapter=new NewsAdapter(mContext);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(mContext);
        newsList.setItemAnimator(new DefaultItemAnimator());
        newsList.setLayoutManager(layoutManager);
        newsList.setAdapter(newsAdapter);

    }
}
