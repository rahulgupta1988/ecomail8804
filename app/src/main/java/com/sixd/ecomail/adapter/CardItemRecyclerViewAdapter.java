package com.sixd.ecomail.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sixd.ecomail.AddAccountActivity;
import com.sixd.ecomail.R;
import com.sixd.ecomail.Utility.MyApplication;
import com.sixd.ecomail.model.Card;

import java.util.ArrayList;

/**
 * Created by ram on 17-04-2018.
 */

public class CardItemRecyclerViewAdapter extends RecyclerView.Adapter<CardItemRecyclerViewAdapter.ItemViewHolder> {

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView itemLabel,txt_primary;
        private ImageView img_more;

        public ItemViewHolder(View itemView) {
            super(itemView);
            itemLabel = itemView.findViewById(R.id.item_label);
            txt_primary= itemView.findViewById(R.id.txt_primary);
            img_more=  itemView.findViewById(R.id.img_more);
        }
    }

    private Context context;
    private ArrayList<Card> arrayList;

    public CardItemRecyclerViewAdapter(Context context, ArrayList<Card> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        MyApplication.MyLogi("Size section",""+arrayList.size());
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_row_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        holder.itemLabel.setText(arrayList.get(position).getName());
        holder.txt_primary.setText(arrayList.get(position).getIsPrimary());

        holder.img_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moreOptionDialog(holder.img_more,position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }




    public void moreOptionDialog(View anchoreView,int clicedPos){

        PopupMenu popup = new PopupMenu(context, anchoreView);
        popup.getMenuInflater().inflate(R.menu.payaccountmore_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(context,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();

                if(item.getItemId()==R.id.txt_details){
                    Intent addAccountIntent=new Intent(context,AddAccountActivity.class);
                    context.startActivity(addAccountIntent);
                }

                return true;
            }
        });

        popup.show();



    }
}

