package com.sixd.ecomail.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.sixd.ecomail.R;
import com.sixd.ecomail.model.Folder;

import java.util.List;

/**
 * Created by ram on 23-04-2018.
 */

public class SmartFolderAdapter extends RecyclerView.Adapter<SmartFolderAdapter.FolderHolder> {

    Context mContext;
    RecyclerView recyclerView = null;
    List<Folder> folderArrayList = null;

    public SmartFolderAdapter(Context mContext, List<Folder> folderArrayList) {
        this.mContext = mContext;
        this.folderArrayList = folderArrayList;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public SmartFolderAdapter.FolderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.smartfolderlistitem, parent, false);
        return new SmartFolderAdapter.FolderHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final SmartFolderAdapter.FolderHolder holder, final int position) {

        Folder folder = folderArrayList.get(position);
        holder.txtxfolderName.setText(folder.getFolderName());

        if (folder.isSelected()) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isSelcted= folderArrayList.get(position).isSelected();
                folderArrayList.get(position).setSelected(!isSelcted);
                holder.checkBox.setChecked(!isSelcted);
                notifyDataSetChanged();
            }
        });

    }


    @Override
    public int getItemCount() {
        return folderArrayList.size();
    }


    public class FolderHolder extends RecyclerView.ViewHolder {
        TextView txtxfolderName;
        CheckBox checkBox;

        public FolderHolder(View itemView) {
            super(itemView);
            txtxfolderName = itemView.findViewById(R.id.txtxfolderName);
            checkBox = itemView.findViewById(R.id.checkBox);

        }
    }
}


