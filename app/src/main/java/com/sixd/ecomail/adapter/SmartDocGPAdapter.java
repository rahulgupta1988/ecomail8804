package com.sixd.ecomail.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.sixd.ecomail.R;
import com.sixd.ecomail.model.DocGroups;
import com.sixd.ecomail.model.DocumentGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ram on 23-04-2018.
 */

public class SmartDocGPAdapter extends RecyclerView.Adapter<SmartDocGPAdapter.DocGPHolder> {

    Context mContext;
    RecyclerView recyclerView = null;
    List<DocumentGroup> docGroupsList = null;

    public SmartDocGPAdapter(Context mContext, List<DocumentGroup> docGroupsList) {
        this.mContext = mContext;
        this.docGroupsList = docGroupsList;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public SmartDocGPAdapter.DocGPHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.doctypeitem, parent, false);
        return new SmartDocGPAdapter.DocGPHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final SmartDocGPAdapter.DocGPHolder holder, final int position) {
        DocumentGroup docGroups = docGroupsList.get(position);

        holder.txtDocTpye.setText(docGroups.getDocumentGroupName());
        if (docGroups.isSelected()) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isSelcted= docGroupsList.get(position).isSelected();
                docGroupsList.get(position).setSelected(!isSelcted);
                holder.checkBox.setChecked(!isSelcted);
                notifyDataSetChanged();
            }
        });

    }


    @Override
    public int getItemCount() {
        return docGroupsList.size();
    }


    public class DocGPHolder extends RecyclerView.ViewHolder {
        TextView txtDocTpye;
        CheckBox checkBox;

        public DocGPHolder(View itemView) {
            super(itemView);
            txtDocTpye = itemView.findViewById(R.id.txtDocTpye);
            checkBox = itemView.findViewById(R.id.checkBox);

        }
    }
}

