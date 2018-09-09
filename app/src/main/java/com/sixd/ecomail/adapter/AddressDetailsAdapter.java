package com.sixd.ecomail.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sixd.ecomail.BillingAddressActivity;
import com.sixd.ecomail.R;
import com.sixd.ecomail.model.AddressDetails;

import java.util.List;

/**
 * Created by ram on 18-04-2018.
 */

public class AddressDetailsAdapter extends RecyclerView.Adapter<AddressDetailsAdapter.AddressDeatilsHolder> {

    Context mContext;
    RecyclerView recyclerView = null;
    String selectedAddress = "";

    List<AddressDetails> addressDetailsList = null;

    public AddressDetailsAdapter(Context mContext, String selectedAddress, List<AddressDetails> addressDetailsList) {
        this.mContext = mContext;
        this.addressDetailsList = addressDetailsList;
        this.selectedAddress = selectedAddress;

    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public AddressDetailsAdapter.AddressDeatilsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.addressdetailsitem, parent, false);
        return new AddressDetailsAdapter.AddressDeatilsHolder(itemView);
    }


    int mExpandedPosition = -1;


    @Override
    public void onBindViewHolder(final AddressDetailsAdapter.AddressDeatilsHolder holder, final int position) {
        AddressDetails addressDetails = addressDetailsList.get(position);
        holder.txt_address.setText("" + addressDetails.getStreetAddress());


        if (selectedAddress.equals(addressDetails.getStreetAddress())) {
            mExpandedPosition = position;
        }


        final boolean isExpanded = position == mExpandedPosition;
        holder.img_selected.setVisibility(isExpanded ? View.VISIBLE : View.GONE);


        holder.parentlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandedPosition = isExpanded ? -1 : position;
                notifyDataSetChanged();
                if (mExpandedPosition != -1)
                    selectedAddress = addressDetailsList.get(mExpandedPosition).getStreetAddress();
                else selectedAddress = "";

                ((BillingAddressActivity) mContext).setAddress(selectedAddress);
            }
        });


    }

    @Override
    public void onViewRecycled(AddressDetailsAdapter.AddressDeatilsHolder holder) {
        super.onViewRecycled(holder);


    }

    @Override
    public int getItemCount() {
        return addressDetailsList.size();
    }


    public class AddressDeatilsHolder extends RecyclerView.ViewHolder {

        TextView txt_address;
        ImageView img_selected;
        RelativeLayout parentlay;


        public AddressDeatilsHolder(final View itemView) {
            super(itemView);
            txt_address = itemView.findViewById(R.id.txt_address);
            img_selected = itemView.findViewById(R.id.img_selected);
            parentlay = itemView.findViewById(R.id.parentlay);

        }


    }


}


