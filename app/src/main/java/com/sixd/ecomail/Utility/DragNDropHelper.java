package com.sixd.ecomail.Utility;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by ram on 11-04-2018.
 */

public class DragNDropHelper extends ItemTouchHelper.Callback {

    Context mContext;
    private SwipeAndDragHelper.ActionCompletionContract contract;


    public DragNDropHelper(SwipeAndDragHelper.ActionCompletionContract contract, Context mContext) {
        this.contract = contract;
        this.mContext = mContext;
    }


    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        return makeMovementFlags(dragFlags, 0);


    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

        contract.onViewMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;


    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }


    public interface ActionCompletionContract {
        void onViewMoved(int oldPosition, int newPosition);

        void onViewSwiped(int position);
    }


}

