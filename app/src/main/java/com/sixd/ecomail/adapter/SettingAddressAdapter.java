package com.sixd.ecomail.adapter;

import android.content.Context;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.sixd.ecomail.R;
import com.sixd.ecomail.model.Address;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ram on 16-04-2018.
 */

public class SettingAddressAdapter extends RecyclerView.Adapter<SettingAddressAdapter.AddressHolder> {

    Context mContext;
    RecyclerView recyclerView = null;
    List<Address> addressList = new ArrayList();



    public SettingAddressAdapter(Context mContext) {
        this.mContext = mContext;
        Address address1 = new Address("Primary", "123 Main St. Anywhere, MA", false);
        Address address2 = new Address("Primary", "123 Main St. Anywhere, MA", false);
        addressList.add(address1);
        addressList.add(address2);
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public SettingAddressAdapter.AddressHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.settingaddresslist_item, parent, false);
        return new SettingAddressAdapter.AddressHolder(itemView);
    }





    int mExpandedPosition=-1;

    @Override
    public void onBindViewHolder(final SettingAddressAdapter.AddressHolder holder, final int position) {
        Address address = addressList.get(position);
        holder.txt_primary.setText("" + address.getIsPrimary());
        holder.txtAddress.setText("" + address.getAdressStr());
        holder.radiobtn.setChecked(address.isChecked());


        final boolean isExpanded = position==mExpandedPosition;
        holder.radiobtn.setChecked(isExpanded?isExpanded:isExpanded);


        holder.radiobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mExpandedPosition!=-1)
                addressList.get(mExpandedPosition).setChecked(false);
                mExpandedPosition = isExpanded ? -1:position;
                addressList.get(position).setChecked(true);
                notifyDataSetChanged();
            }
        });


        holder.img_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moreOptionDialog(holder.img_more,position);
            }
        });




       /* if (inboxSort.isChecked()) {
            holder.imgChecked.setBackgroundResource(R.drawable.checked_ic);
        } else {
            holder.imgChecked.setBackgroundResource(R.drawable.unchecked_ic);
        }
        holder.imgChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sortItemList.get(position).isChecked()) {
                    sortItemList.get(position).setChecked(false);
                } else {
                    sortItemList.get(position).setChecked(true);
                }

                notifyDataSetChanged();
            }
        });*/


    }


    @Override
    public int getItemCount() {
        return addressList.size();
    }


    public class AddressHolder extends RecyclerView.ViewHolder {

        RadioButton radiobtn;
        TextView txt_primary, txtAddress;
        ImageView img_more;

        public AddressHolder(final View itemView) {
            super(itemView);
            radiobtn = itemView.findViewById(R.id.radiobtn);
            txt_primary = itemView.findViewById(R.id.txt_primary);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            img_more = itemView.findViewById(R.id.img_more);


        }


    }

    public void moreOptionDialog(View anchoreView,int clicedPos){

        PopupMenu popup = new PopupMenu(mContext, anchoreView);
        popup.getMenuInflater().inflate(R.menu.adressmore_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(mContext,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        popup.show();



    }


}

