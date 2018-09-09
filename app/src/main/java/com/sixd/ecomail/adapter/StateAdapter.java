package com.sixd.ecomail.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sixd.ecomail.AddAddressActivity;
import com.sixd.ecomail.R;
import com.sixd.ecomail.Utility.MyApplication;
import com.sixd.ecomail.model.State;

import java.util.List;

/**
 * Created by ram on 16-04-2018.
 */

public class StateAdapter extends RecyclerView.Adapter<StateAdapter.StateHolder> {

    Context mContext;
    RecyclerView recyclerView = null;
    List<State> stateList = null;
    String selectedState = "";

    public StateAdapter(Context mContext, String selectedState, List<State> stateList) {
        this.mContext = mContext;
        this.stateList = stateList;
        this.selectedState = selectedState;

    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public StateAdapter.StateHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.stateitem, parent, false);
        return new StateAdapter.StateHolder(itemView);
    }


    int mExpandedPosition = -1;


    @Override
    public void onBindViewHolder(final StateAdapter.StateHolder holder, final int position) {
        State state = stateList.get(position);
        holder.txt_alph.setText("" + state.getAlph());
        holder.txt_state.setText("" + state.getName() + ", " + state.getAbbreviation());


        if (selectedState.equals(state.getName())) {
            mExpandedPosition = position;
        }

        MyApplication.MyLogi("sized1343", "" + state.getIsFirst());

        if (state.getIsFirst().equals("1")) {
            holder.txt_alph.setVisibility(View.VISIBLE);
            holder.viewchange.setVisibility(View.GONE);
        } else if (state.getIsFirst().equals("2")) {
            holder.viewchange.setVisibility(View.VISIBLE);
            holder.txt_alph.setVisibility(View.INVISIBLE);
        } else if (state.getIsFirst().equals("12")) {
            holder.txt_alph.setVisibility(View.VISIBLE);
            holder.viewchange.setVisibility(View.VISIBLE);
        } else {
            holder.txt_alph.setVisibility(View.INVISIBLE);
            holder.viewchange.setVisibility(View.GONE);
        }


        final boolean isExpanded = position == mExpandedPosition;
        holder.img_selected.setVisibility(isExpanded ? View.VISIBLE : View.GONE);


        holder.parentlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandedPosition = isExpanded ? -1 : position;
                notifyDataSetChanged();
                if (mExpandedPosition != -1)
                    selectedState = stateList.get(mExpandedPosition).getName();
                else selectedState = "";

                ((AddAddressActivity) mContext).setState(selectedState);
            }
        });


    }

    @Override
    public void onViewRecycled(StateHolder holder) {
        super.onViewRecycled(holder);


    }

    @Override
    public int getItemCount() {
        return stateList.size();
    }


    public class StateHolder extends RecyclerView.ViewHolder {

        TextView txt_alph, txt_state;
        ImageView img_selected;
        LinearLayout parentlay;
        View viewchange;

        public StateHolder(final View itemView) {
            super(itemView);
            txt_alph = itemView.findViewById(R.id.txt_alph);
            txt_state = itemView.findViewById(R.id.txt_state);
            img_selected = itemView.findViewById(R.id.img_selected);
            parentlay = itemView.findViewById(R.id.parentlay);
            viewchange = itemView.findViewById(R.id.viewchange);
        }


    }


}


