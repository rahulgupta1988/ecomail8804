package com.sixd.ecomail.adapter;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sixd.ecomail.R;
import com.sixd.ecomail.model.CardSectionModel;

import java.util.ArrayList;

/**
 * Created by ram on 17-04-2018.
 */

public class PaymentAccountAdapter extends RecyclerView.Adapter<PaymentAccountAdapter.SectionViewHolder> {


    class SectionViewHolder extends RecyclerView.ViewHolder {
        private TextView sectionLabel;
        private RecyclerView itemRecyclerView;

        public SectionViewHolder(View itemView) {
            super(itemView);
            sectionLabel = (TextView) itemView.findViewById(R.id.section_label);
            itemRecyclerView = (RecyclerView) itemView.findViewById(R.id.item_recycler_view);
        }
    }

    private Context context;
    private ArrayList<CardSectionModel> sectionModelArrayList;

    public PaymentAccountAdapter(Context context, ArrayList<CardSectionModel> sectionModelArrayList) {
        this.context = context;
        this.sectionModelArrayList = sectionModelArrayList;
    }

    @Override
    public SectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carditem_view, parent, false);
        return new SectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SectionViewHolder holder, int position) {
        final CardSectionModel sectionModel = sectionModelArrayList.get(position);
        holder.sectionLabel.setText(sectionModel.getSectionLabel());

        //recycler view for items
        holder.itemRecyclerView.setHasFixedSize(true);
        holder.itemRecyclerView.setNestedScrollingEnabled(false);

        /* set layout manager on basis of recyclerview enum type */
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        holder.itemRecyclerView.setLayoutManager(layoutManager);
        holder.itemRecyclerView.setItemAnimator(new DefaultItemAnimator());
        CardItemRecyclerViewAdapter adapter = new CardItemRecyclerViewAdapter(context, sectionModel.getCardList());
        holder.itemRecyclerView.setAdapter(adapter);



    }

    @Override
    public int getItemCount() {
        return sectionModelArrayList.size();
    }


}
