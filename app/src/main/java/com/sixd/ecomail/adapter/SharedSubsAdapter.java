package com.sixd.ecomail.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sixd.ecomail.R;
import com.sixd.ecomail.Utility.MyApplication;
import com.sixd.ecomail.model.DocumentGroup;
import com.sixd.ecomail.model.Producer;
import com.sixd.ecomail.model.Subscription;

import java.util.List;

/**
 * Created by ram on 30-04-2018.
 */

public class SharedSubsAdapter extends RecyclerView.Adapter<SharedSubsAdapter.MySubsHolder> {

    Context context;
    LayoutInflater layoutInflater;
    List<Subscription> mySubscriptionList = null;

    public SharedSubsAdapter(Context context, List<Subscription> mySubscriptionList) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.mySubscriptionList = mySubscriptionList;
    }

    @Override
    public SharedSubsAdapter.MySubsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.mysubsadapter_view, parent, false);
        return new SharedSubsAdapter.MySubsHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SharedSubsAdapter.MySubsHolder holder, int position) {

        Subscription subscription = mySubscriptionList.get(position);
        DocumentGroup documentGroup= subscription.documentGroupToOne.getTarget();
        Producer producer=documentGroup.producerToOne.getTarget();


        String producerName=producer.getProducerName();
        String papaeronOff=documentGroup.getPaperlessStatus();

        String groupName=documentGroup.getDocumentGroupName();
        String relationship=documentGroup.getRelationship();
        String documentGroupLogoLocation=documentGroup.getDocumentGroupLogoLocation();

        holder.txtTitle.setText(""+producerName);
        holder.txtStatement.setText(""+groupName);
        holder.txtBankAc.setText(""+relationship);
        MyApplication.MyLogi("documentGroups1111"," "+papaeronOff);
        if(papaeronOff.equals("TRUE")) {
            holder.txt_paperonoff.setText("Paperless ON");
            holder.paperOnOff.setImageDrawable(null);
            holder.paperOnOff.setImageDrawable(context.getDrawable(R.drawable.paperlesson_vector));

        }
        else {
            holder.txt_paperonoff.setText("Paperless OFF");
            holder.paperOnOff.setImageDrawable(null);
            holder.paperOnOff.setImageDrawable(context.getDrawable(R.drawable.paperlessoff_vector));

        }

        Glide.with(context).load(documentGroupLogoLocation)
                .fitCenter()
                .into((holder.imgDocLogo));


    }


    @Override
    public int getItemCount() {
        return mySubscriptionList.size();
    }

    class MySubsHolder extends RecyclerView.ViewHolder {

        ImageView imgDocLogo, paperOnOff;
        TextView txtTitle, txtStatement, txtBankAc, txt_paperonoff;

        public MySubsHolder(View itemView) {

            super(itemView);

            imgDocLogo = (ImageView) itemView.findViewById(R.id.imgDocLogo);
            paperOnOff = (ImageView) itemView.findViewById(R.id.paperOnOff);

            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            txtStatement = (TextView) itemView.findViewById(R.id.txtStatement);
            txtBankAc = (TextView) itemView.findViewById(R.id.txtBankAc);
            txt_paperonoff = (TextView) itemView.findViewById(R.id.txt_paperonoff);


        }
    }
}

