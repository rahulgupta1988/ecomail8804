package com.sixd.ecomail.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sixd.ecomail.R;

import java.util.zip.Inflater;

/**
 * Created by ram on 26-04-2018.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder> {

    Context context;
    LayoutInflater layoutInflater;
    public NewsAdapter(Context context){
        this.context=context;
        layoutInflater= LayoutInflater.from(context);
    }

    @Override
    public NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView=layoutInflater.inflate(R.layout.newsitem,parent,false);
        return new NewsHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NewsHolder holder, int position) {

    }



    @Override
    public int getItemCount() {
        return 10;
    }

    class NewsHolder extends RecyclerView.ViewHolder{

        public NewsHolder(View itemView) {
            super(itemView);
        }
    }
}
