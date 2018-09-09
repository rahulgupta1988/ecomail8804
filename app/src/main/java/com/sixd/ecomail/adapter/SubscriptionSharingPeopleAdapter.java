package com.sixd.ecomail.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sixd.ecomail.ExistSubsCriptionActivity;
import com.sixd.ecomail.R;
import com.sixd.ecomail.model.Custom_FolderModel;

import java.util.List;

/**
 * Created by Rahul Gupta on 13-06-2018.
 */
public class SubscriptionSharingPeopleAdapter extends RecyclerView.Adapter<SubscriptionSharingPeopleAdapter.PeopleListHolder> {

    Context mContext;
    RecyclerView recyclerView = null;




    public SubscriptionSharingPeopleAdapter(Context mContext) {
        this.mContext = mContext;



    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public SubscriptionSharingPeopleAdapter.PeopleListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.subscriptionsharingpeople_view, parent, false);
        return new SubscriptionSharingPeopleAdapter.PeopleListHolder(itemView);
    }





    @Override
    public void onBindViewHolder(final SubscriptionSharingPeopleAdapter.PeopleListHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ExistSubsCriptionActivity)mContext).peopleharingDialog(position);
            }
        });


    }

    @Override
    public void onViewRecycled(SubscriptionSharingPeopleAdapter.PeopleListHolder holder) {
        super.onViewRecycled(holder);


    }

    @Override
    public int getItemCount() {
        return 2;
    }


    public class PeopleListHolder extends RecyclerView.ViewHolder {

        TextView txt_address;
        ImageView img_selected;
        LinearLayout parentlay;


        public PeopleListHolder(final View itemView) {
            super(itemView);
            txt_address = itemView.findViewById(R.id.txt_address);
            img_selected = itemView.findViewById(R.id.img_selected);
            parentlay = itemView.findViewById(R.id.parentlay);

        }


    }


}



