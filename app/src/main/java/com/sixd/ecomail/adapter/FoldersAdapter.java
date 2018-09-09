package com.sixd.ecomail.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sixd.ecomail.APIManager.APIQueries;
import com.sixd.ecomail.FolderDocsActivity;
import com.sixd.ecomail.HomeActivity;
import com.sixd.ecomail.R;
import com.sixd.ecomail.Utility.MyApplication;
import com.sixd.ecomail.fragment.FolderFrag;
import com.sixd.ecomail.model.Custom_FolderModel;
import com.sixd.ecomail.model.Document;
import com.sixd.ecomail.model.Folder;
import com.sixd.ecomail.model.SmartFolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ram on 19-04-2018.
 */

public class FoldersAdapter extends RecyclerView.Adapter<FoldersAdapter.FolderHolder> {

    Context mContext;
    RecyclerView recyclerView = null;
    FolderFrag folderFrag;

    public static List<Document> temp_list=new ArrayList<>();
    List<Custom_FolderModel> folderArrayList = null;

    public FoldersAdapter(Context mContext, List<Custom_FolderModel> folderArrayList,FolderFrag folderFrag) {
        this.mContext = mContext;
        this.folderArrayList = folderArrayList;
        this.folderFrag=folderFrag;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public FoldersAdapter.FolderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.folderlistitem, parent, false);
        return new FoldersAdapter.FolderHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final FoldersAdapter.FolderHolder holder, final int position) {


        Custom_FolderModel custom_folderModel=folderArrayList.get(position);

        holder.txtxfolderName.setText(custom_folderModel.getFolderName());
        holder.txtContentCount.setText("" + custom_folderModel.getDocCount());

        if(custom_folderModel.isSmartFolder()){
            holder.imgInfo.setImageBitmap(null);
            holder.imgInfo.setBackgroundResource(R.drawable.smartfolder_ic);
        }

        else{
            holder.imgInfo.setImageBitmap(null);
            holder.imgInfo.setBackgroundResource(R.drawable.folder_light_vector);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                MyApplication.MyLogi("7455dfd",""+folderArrayList.get(position).getSmartDocs().size());
                    String folderName = folderArrayList.get(position).getFolderName();
                    temp_list.clear();
                    temp_list.addAll(folderArrayList.get(position).getSmartDocs());
                    Intent intent = new Intent(mContext, FolderDocsActivity.class);
                    intent.putExtra("folderName", folderName+"_"+folderArrayList.get(position).isSmartFolder());
                intent.putExtra("folderID", folderArrayList.get(position).getFolderId());
                folderFrag.startActivityForResult(intent,701);



            }
        });

    }


    @Override
    public int getItemCount() {
        return folderArrayList.size();
    }


    public class FolderHolder extends RecyclerView.ViewHolder {
        TextView txtxfolderName, txtContentCount;
        ImageView imgInfo;

        public FolderHolder(View itemView) {
            super(itemView);
            txtxfolderName = itemView.findViewById(R.id.txtxfolderName);
            txtContentCount = itemView.findViewById(R.id.txtContentCount);
            imgInfo=itemView.findViewById(R.id.imgInfo);

        }
    }
}

