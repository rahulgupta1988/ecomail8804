package com.sixd.ecomail.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sixd.ecomail.R;
import com.sixd.ecomail.RecipentsActivity;
import com.sixd.ecomail.model.Recipents;

import java.nio.charset.StandardCharsets;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ram on 18-04-2018.
 */

public class RecipentsAdapter extends RecyclerView.Adapter<RecipentsAdapter.RecipentsHolder> {

    Context mContext;
    RecyclerView recyclerView = null;
    List<Recipents> recipentsArrayList = null;


    public RecipentsAdapter(Context mContext, List<Recipents> recipentsArrayList) {
        this.mContext = mContext;
        this.recipentsArrayList = recipentsArrayList;

    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public RecipentsAdapter.RecipentsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.recipents_itemview, parent, false);
        return new RecipentsAdapter.RecipentsHolder(itemView);
    }


    int mExpandedPosition = -1;


    @Override
    public void onBindViewHolder(final RecipentsAdapter.RecipentsHolder holder, final int position) {


        Recipents recipents = recipentsArrayList.get(position);

        String createDate = recipents.getCreateDate();
        String image = recipents.getImage();
        String consumerId = recipents.getConsumerId();
        String networkLoginId = recipents.getNetworkLoginId();
        String recipientName = recipents.getRecipientName();
        String recipientType = recipents.getRecipientType();

        byte[] data = Base64.decode(image, Base64.DEFAULT);
        String base64Str = new String(data, StandardCharsets.UTF_8);
        byte[] decodedString = Base64.decode(base64Str, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        holder.imgRecipent.setImageBitmap(decodedByte);
        holder.txtRecipentName.setText(""+recipientName);

        holder.img_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moreOptionDialog(holder.img_more,position);
            }
        });

    }

    @Override
    public void onViewRecycled(RecipentsAdapter.RecipentsHolder holder) {
        super.onViewRecycled(holder);


    }

    @Override
    public int getItemCount() {
        return recipentsArrayList.size();
    }


    public class RecipentsHolder extends RecyclerView.ViewHolder {

        TextView txtRecipentName, txtNickName;
        CircleImageView imgRecipent;
        ImageView img_more;
        RelativeLayout parentlay;


        public RecipentsHolder(final View itemView) {
            super(itemView);
            txtRecipentName = itemView.findViewById(R.id.txtRecipentName);
            txtNickName = itemView.findViewById(R.id.txtNickName);
            imgRecipent = itemView.findViewById(R.id.imgRecipent);
            img_more = itemView.findViewById(R.id.img_more);
            parentlay = itemView.findViewById(R.id.parentlay);

        }


    }

    public void moreOptionDialog(View anchoreView, int clicedPos) {

        PopupMenu popup = new PopupMenu(mContext, anchoreView);
        popup.getMenuInflater().inflate(R.menu.adressmore_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(mContext, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();

                if (item.getItemId() == R.id.txt_edit) {
                    ((RecipentsActivity) mContext).recipentsEditDialog();
                } else if (item.getItemId() == R.id.txt_delete) {

                }

                return true;
            }
        });

        popup.show();


    }


}



