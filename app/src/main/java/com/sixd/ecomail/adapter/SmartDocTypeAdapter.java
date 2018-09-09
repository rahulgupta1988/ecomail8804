package com.sixd.ecomail.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.sixd.ecomail.R;
import com.sixd.ecomail.Utility.MyApplication;
import com.sixd.ecomail.model.DocTypes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ram on 23-04-2018.
 */

public class SmartDocTypeAdapter extends RecyclerView.Adapter<SmartDocTypeAdapter.DocTypeHolder> {

    Context mContext;
    RecyclerView recyclerView = null;
    List<DocTypes> docTypesList=null;

    public SmartDocTypeAdapter(Context mContext, List<DocTypes> docTypesList) {
        this.mContext = mContext;
        this.docTypesList = docTypesList;
        MyApplication.MyLogi("size 34", "" + docTypesList.size());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public SmartDocTypeAdapter.DocTypeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.doctypeitem, parent, false);
        return new SmartDocTypeAdapter.DocTypeHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final SmartDocTypeAdapter.DocTypeHolder holder, final int position) {
        DocTypes documentType = docTypesList.get(position);
        holder.txtDocTpye.setText(""+documentType.getDocumentTypeName());
        if(documentType.isSelected())
            holder.checkBox.setChecked(true);
        else holder.checkBox.setChecked(false);

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSelcted= docTypesList.get(position).isSelected();
                docTypesList.get(position).setSelected(!isSelcted);
                holder.checkBox.setChecked(!isSelcted);
                notifyDataSetChanged();
            }
        });

    }


    @Override
    public int getItemCount() {
        return docTypesList.size();
    }


    public class DocTypeHolder extends RecyclerView.ViewHolder {
        TextView txtDocTpye;
        CheckBox checkBox;

        public DocTypeHolder(View itemView) {
            super(itemView);
            txtDocTpye = itemView.findViewById(R.id.txtDocTpye);
            checkBox = itemView.findViewById(R.id.checkBox);

        }
    }
}

