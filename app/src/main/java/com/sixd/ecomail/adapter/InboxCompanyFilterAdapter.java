package com.sixd.ecomail.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sixd.ecomail.R;
import com.sixd.ecomail.model.Producer;

import java.util.List;

/**
 * Created by Rahul Gupta on 09-05-2018.
 */
public class InboxCompanyFilterAdapter extends RecyclerView.Adapter<InboxCompanyFilterAdapter.CompanyHolder> {

    Context mContext;
    RecyclerView recyclerView = null;
    List<Producer> producerList = null;


    public InboxCompanyFilterAdapter(Context mContext, List<Producer> producerList) {
        this.mContext = mContext;
        this.producerList = producerList;


    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public InboxCompanyFilterAdapter.CompanyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.companyfilterview, parent, false);
        return new InboxCompanyFilterAdapter.CompanyHolder(itemView);
    }


    int mExpandedPosition = -1;


    @Override
    public void onBindViewHolder(final InboxCompanyFilterAdapter.CompanyHolder holder, final int position) {
        Producer producer = producerList.get(position);


        String firstCh = producer.getProducerName().substring(0, 1);
        holder.txt_alph.setText("" + firstCh);
        holder.txt_state.setText("" + producer.getProducerName());

        if(producerList.get(position).isSelected())
            holder.checkCmp.setChecked(true);
        else holder.checkCmp.setChecked(false);


        if (producer.getIsFirst().equals("0")) {
            holder.txt_alph.setVisibility(View.VISIBLE);
            holder.viewchange.setVisibility(View.GONE);
        } else if (producer.getIsFirst().equals("2")) {
            holder.viewchange.setVisibility(View.VISIBLE);
            holder.txt_alph.setVisibility(View.INVISIBLE);
        } else if (producer.getIsFirst().equals("02")) {
            holder.txt_alph.setVisibility(View.VISIBLE);
            holder.viewchange.setVisibility(View.VISIBLE);
        } else {
            holder.txt_alph.setVisibility(View.INVISIBLE);
            holder.viewchange.setVisibility(View.GONE);
        }


        holder.checkCmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               boolean isSelcted= producerList.get(position).isSelected();
               producerList.get(position).setSelected(!isSelcted);
                holder.checkCmp.setChecked(!isSelcted);
            }
        });


    }

    @Override
    public void onViewRecycled(InboxCompanyFilterAdapter.CompanyHolder holder) {
        super.onViewRecycled(holder);


    }

    @Override
    public int getItemCount() {
        return producerList.size();
    }


    public class CompanyHolder extends RecyclerView.ViewHolder {

        TextView txt_alph, txt_state;
        CheckBox checkCmp;
        LinearLayout parentlay;
        View viewchange;

        public CompanyHolder(final View itemView) {
            super(itemView);
            txt_alph = itemView.findViewById(R.id.txt_alph);
            txt_state = itemView.findViewById(R.id.txt_state);
            checkCmp = itemView.findViewById(R.id.checkCmp);
            parentlay = itemView.findViewById(R.id.parentlay);
            viewchange = itemView.findViewById(R.id.viewchange);
        }


    }


}



