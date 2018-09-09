package com.sixd.ecomail.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sixd.ecomail.R;
import com.sixd.ecomail.model.Custom_FolderModel;

import java.util.List;

/**
 * Created by Rahul Gupta on 11-06-2018.
 */
public class FolderListDialogAdapter extends RecyclerView.Adapter<FolderListDialogAdapter.FolderListHolder> {

    Context mContext;
    RecyclerView recyclerView = null;
    String selectedAddress = "";

    List<Custom_FolderModel> custom_folderList = null;

    public FolderListDialogAdapter(Context mContext, List<Custom_FolderModel> custom_folderList) {
        this.mContext = mContext;
        this.custom_folderList = custom_folderList;


    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public FolderListDialogAdapter.FolderListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.folderlistdialog_item, parent, false);
        return new FolderListDialogAdapter.FolderListHolder(itemView);
    }


    int mExpandedPosition = -1;


    @Override
    public void onBindViewHolder(final FolderListDialogAdapter.FolderListHolder holder, final int position) {
        Custom_FolderModel custom_folderModel = custom_folderList.get(position);
        holder.txt_address.setText("" + custom_folderModel.getFolderName());

        if (custom_folderModel.isSelected()) {
            holder.img_selected.setVisibility(View.VISIBLE);
        } else {
            holder.img_selected.setVisibility(View.GONE);
        }


        holder.parentlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                custom_folderList.get(position).setSelected(!custom_folderList.get(position).isSelected());
                notifyDataSetChanged();
            }
        });


    }

    @Override
    public void onViewRecycled(FolderListDialogAdapter.FolderListHolder holder) {
        super.onViewRecycled(holder);


    }

    @Override
    public int getItemCount() {
        return custom_folderList.size();
    }


    public class FolderListHolder extends RecyclerView.ViewHolder {

        TextView txt_address;
        ImageView img_selected;
        RelativeLayout parentlay;


        public FolderListHolder(final View itemView) {
            super(itemView);
            txt_address = itemView.findViewById(R.id.txt_address);
            img_selected = itemView.findViewById(R.id.img_selected);
            parentlay = itemView.findViewById(R.id.parentlay);

        }


    }


}


