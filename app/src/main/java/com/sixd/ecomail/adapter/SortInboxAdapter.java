package com.sixd.ecomail.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sixd.ecomail.R;
import com.sixd.ecomail.Utility.MyApplication;
import com.sixd.ecomail.Utility.SwipeAndDragHelper;
import com.sixd.ecomail.model.InboxSort;
import com.sixd.ecomail.model.InboxSortOrder;

import java.util.List;

/**
 * Created by ram on 10-04-2018.
 */

public class SortInboxAdapter extends RecyclerView.Adapter<SortInboxAdapter.SortHolder> implements
        SwipeAndDragHelper.ActionCompletionContract {

    Context mContext;
    RecyclerView recyclerView = null;
    List<InboxSortOrder> sortInboxItemList = null;

    ItemTouchHelper touchHelper = null;

    public void setTouchHelper(ItemTouchHelper touchHelper) {
        this.touchHelper = touchHelper;
    }

    public SortInboxAdapter(Context mContext, List<InboxSortOrder> sortInboxItemList) {
        this.mContext = mContext;
        this.sortInboxItemList = sortInboxItemList;

    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public SortInboxAdapter.SortHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.sortlistinbox_item, parent, false);
        return new SortInboxAdapter.SortHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final SortInboxAdapter.SortHolder holder, final int position) {
        final InboxSortOrder inboxSortOrder = sortInboxItemList.get(position);
        holder.txtsortstr.setText("" + inboxSortOrder.getCaption());

        if (inboxSortOrder.isSelected()) {
            holder.checkCmp.setChecked(true);
        } else {
            holder.checkCmp.setChecked(false);
        }




        holder.checkCmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sortInboxItemList.get(position).isSelected()) {
                    sortInboxItemList.get(position).setSelected(false);
                } else {
                    sortInboxItemList.get(position).setSelected(true);
                }

                notifyDataSetChanged();
            }
        });


        holder.imgDrag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    touchHelper.startDrag(holder);
                }
                return false;
            }
        });

       /* holder.imgDrag.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                touchHelper.startDrag(holder);
                return true;
            }
        });*/

    }


    @Override
    public int getItemCount() {
        return sortInboxItemList.size();
    }



    @Override
    public void onViewMoved(int oldPosition, int newPosition) {

        InboxSortOrder targetinboxSort = sortInboxItemList.get(oldPosition);
        InboxSortOrder inboxSort = new InboxSortOrder(targetinboxSort.getAssociatedEntity(), targetinboxSort.getCaption(), targetinboxSort.getCategory(),
                targetinboxSort.getDbColumn(),targetinboxSort.getDefaultOrder(),targetinboxSort.isSelected(),targetinboxSort.getSortOrder());
        sortInboxItemList.remove(oldPosition);
        sortInboxItemList.add(newPosition, inboxSort);
        notifyItemMoved(oldPosition, newPosition);

    }

    @Override
    public void onViewSwiped(int position) {

    }


    public class SortHolder extends RecyclerView.ViewHolder {

        TextView txtsortstr;
        CheckBox checkCmp;
        ImageView imgDrag;
        LinearLayout parentlay;

        public SortHolder(final View itemView) {
            super(itemView);

            txtsortstr = itemView.findViewById(R.id.txtsortstr);
            checkCmp = itemView.findViewById(R.id.checkCmp);
            imgDrag = itemView.findViewById(R.id.imgDrag);
            parentlay = itemView.findViewById(R.id.parentlay);


        }


    }


}
