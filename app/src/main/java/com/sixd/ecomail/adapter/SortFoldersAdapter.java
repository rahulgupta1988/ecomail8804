package com.sixd.ecomail.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sixd.ecomail.AddNormalFolderActivity;
import com.sixd.ecomail.R;
import com.sixd.ecomail.Utility.SharedPreferencesManager;
import com.sixd.ecomail.Utility.SwipeAndDragHelper;
import com.sixd.ecomail.fragment.FolderFrag;
import com.sixd.ecomail.model.Custom_FolderModel;
import com.sixd.ecomail.model.Folder;
import com.sixd.ecomail.model.SmartFolder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 * Created by ram on 19-04-2018.
 */

public class SortFoldersAdapter extends RecyclerView.Adapter<SortFoldersAdapter.FolderHolder> implements
        SwipeAndDragHelper.ActionCompletionContract {

    Context mContext;
    FolderFrag folderFrag;
    RecyclerView recyclerView = null;
    ArrayList<Custom_FolderModel> folderArrayList = null;
    ItemTouchHelper touchHelper = null;

    public void setTouchHelper(ItemTouchHelper touchHelper) {
        this.touchHelper = touchHelper;
    }

    public SortFoldersAdapter(Context mContext, ArrayList<Custom_FolderModel> folderArrayList, FolderFrag folderFrag) {
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
    public SortFoldersAdapter.FolderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.foldersortingitem, parent, false);
        return new SortFoldersAdapter.FolderHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final SortFoldersAdapter.FolderHolder holder, final int position) {

        Custom_FolderModel folder = folderArrayList.get(position);

        holder.txtxfolderName.setText(folder.getFolderName());

        if(folder.isSmartFolder()){
            holder.imgInfo.setImageBitmap(null);
            holder.imgInfo.setBackgroundResource(R.drawable.smartfolder_ic);
        }

        else{
            holder.imgInfo.setImageBitmap(null);
            holder.imgInfo.setBackgroundResource(R.drawable.folder_light_vector);
        }


        holder.imgDrag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    touchHelper.startDrag(holder);
                }
                return false;
            }
        });

        holder.img_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moreOptionDialog(holder.img_more, position);
            }
        });

    }


    @Override
    public int getItemCount() {
        return folderArrayList.size();
    }

    @Override
    public void onViewMoved(int oldPosition, int newPosition) {
        Custom_FolderModel targetFolder = folderArrayList.get(oldPosition);
        //folderId,folderName,sequenceNo,isSelected,isSmartFolder,docCount
        Custom_FolderModel folderSort = new Custom_FolderModel(targetFolder.getFolderId(), targetFolder.getFolderName(), targetFolder.getSequenceNo(),
                targetFolder.isSelected(),targetFolder.isSmartFolder(),targetFolder.getDocCount());
        folderArrayList.remove(oldPosition);
        folderArrayList.add(newPosition, folderSort);
        notifyItemMoved(oldPosition, newPosition);
    }

    @Override
    public void onViewSwiped(int position) {

    }


    public class FolderHolder extends RecyclerView.ViewHolder {
        TextView txtxfolderName;
        ImageView imgDrag, img_more,imgInfo;

        public FolderHolder(View itemView) {
            super(itemView);
            txtxfolderName = itemView.findViewById(R.id.txtxfolderName);
            imgDrag = itemView.findViewById(R.id.imgDrag);
            img_more = itemView.findViewById(R.id.img_more);
            imgInfo = itemView.findViewById(R.id.imgInfo);

        }
    }


    public void moreOptionDialog(View anchoreView, int clicedPos) {

        PopupMenu popup = new PopupMenu(mContext, anchoreView);
        popup.getMenuInflater().inflate(R.menu.adressmore_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item)
            {

                if(item.getTitle().toString().equals("Delete")){
                    Custom_FolderModel folder = folderArrayList.get(clicedPos);
                    String folderName=folder.getFolderName();
                    String consumerId= SharedPreferencesManager.getConsumerID(mContext);
                    int sequenceNoInt=folder.getSequenceNo();
                    String folderID=folder.getFolderId();
                    folderFrag.removeFolder(folderName,consumerId,sequenceNoInt,folderID,folder.isSmartFolder());
                }

                else if(item.getTitle().toString().equals("Edit")){
                    Custom_FolderModel folder = folderArrayList.get(clicedPos);
                    String folderName=folder.getFolderName();
                    String consumerId= SharedPreferencesManager.getConsumerID(mContext);
                    int sequenceNoInt=folder.getSequenceNo();
                    String folderID=folder.getFolderId();
                    folderFrag.editFolder(folderName,sequenceNoInt,folderID,folder.isSmartFolder());
                }
                return true;
            }
        });

        popup.show();


    }
}


