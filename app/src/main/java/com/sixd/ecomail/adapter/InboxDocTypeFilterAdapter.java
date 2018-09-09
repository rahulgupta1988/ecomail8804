package com.sixd.ecomail.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.sixd.ecomail.R;
import com.sixd.ecomail.model.DocTypes;
import com.sixd.ecomail.model.Document;

import org.w3c.dom.DocumentType;

import java.util.List;

/**
 * Created by Rahul Gupta on 09-05-2018.
 */
public class InboxDocTypeFilterAdapter extends RecyclerView.Adapter<InboxDocTypeFilterAdapter.DocTypeHolder> {

    Context mContext;
    RecyclerView recyclerView = null;
    List<DocTypes> documentTypeList = null;


    public InboxDocTypeFilterAdapter(Context mContext, List<DocTypes> documentTypeList) {
        this.mContext = mContext;
        this.documentTypeList = documentTypeList;

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public InboxDocTypeFilterAdapter.DocTypeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.doctypeitem, parent, false);
        return new InboxDocTypeFilterAdapter.DocTypeHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final InboxDocTypeFilterAdapter.DocTypeHolder holder, final int position) {

        DocTypes documentType = documentTypeList.get(position);
        holder.txtDocTpye.setText(""+documentType.getDocumentTypeName());
        if(documentType.isSelected())
            holder.checkBox.setChecked(true);
        else holder.checkBox.setChecked(false);

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSelcted= documentTypeList.get(position).isSelected();
                documentTypeList.get(position).setSelected(!isSelcted);
                holder.checkBox.setChecked(!isSelcted);
            }
        });

    }


    @Override
    public int getItemCount() {
        return documentTypeList.size();
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



