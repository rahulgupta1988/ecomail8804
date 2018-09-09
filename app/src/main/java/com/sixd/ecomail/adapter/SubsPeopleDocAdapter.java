package com.sixd.ecomail.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sixd.ecomail.R;

/**
 * Created by Rahul Gupta on 13-06-2018.
 */
public class SubsPeopleDocAdapter extends RecyclerView.Adapter<SubsPeopleDocAdapter.DocsHolder> {

    Context mContext;
    RecyclerView recyclerView = null;


    public SubsPeopleDocAdapter(Context mContext) {
        this.mContext = mContext;


    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public SubsPeopleDocAdapter.DocsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.subspeopledocadapter_view, parent, false);
        return new SubsPeopleDocAdapter.DocsHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final SubsPeopleDocAdapter.DocsHolder holder, final int position) {


    }

    @Override
    public void onViewRecycled(SubsPeopleDocAdapter.DocsHolder holder) {
        super.onViewRecycled(holder);


    }

    @Override
    public int getItemCount() {
        return 2;
    }


    public class DocsHolder extends RecyclerView.ViewHolder {

        TextView txt_address;
        ImageView img_selected;
        LinearLayout parentlay;


        public DocsHolder(final View itemView) {
            super(itemView);
            txt_address = itemView.findViewById(R.id.txt_address);
            img_selected = itemView.findViewById(R.id.img_selected);
            parentlay = itemView.findViewById(R.id.parentlay);

        }


    }


}




