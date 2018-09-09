package com.sixd.ecomail.Utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.view.MotionEvent;
import android.view.View;

import com.sixd.ecomail.R;

import static android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_SWIPE;
import static android.support.v7.widget.helper.ItemTouchHelper.LEFT;
import static android.support.v7.widget.helper.ItemTouchHelper.RIGHT;


enum ButtonsState1 {
    GONE,
    LEFT_VISIBLE,
    RIGHT_VISIBLE
}


public class SwipeController extends Callback {


    Context mContext;
    String leftTxt="";
    String rightTxt1="";
    String rightTxt2="";
    String rightTxt3="";

    private boolean swipeBack = false;

    private ButtonsState buttonShowedState = ButtonsState.GONE;

    private RectF buttonInstancePay = null;
    private RectF buttonInstanceTrash = null;
    private RectF buttonInstanceArchive = null;
    private RectF buttonInstanceInbox = null;
    private RectF buttonInstanceMore = null;

    private RecyclerView.ViewHolder currentItemViewHolder = null;

    private SwipeControllerActions buttonsActions = null;

    private static float buttonWidth = 200;

    public SwipeController(SwipeControllerActions buttonsActions, Context mContext, String leftTxt, String rightTxt1, String rightTxt2, String rightTxt3) {
        this.buttonsActions = buttonsActions;
        this.mContext = mContext;
        this.leftTxt = leftTxt;
        this.rightTxt1 = rightTxt1;
        this.rightTxt2 = rightTxt2;
        this.rightTxt3 = rightTxt3;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, LEFT | RIGHT);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        if (swipeBack) {
            swipeBack = buttonShowedState != ButtonsState.GONE;
            return 0;
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }


    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ACTION_STATE_SWIPE) {
            if (buttonShowedState != ButtonsState.GONE) {
                if (buttonShowedState == ButtonsState.LEFT_VISIBLE) dX = Math.max(dX, buttonWidth);
                if (buttonShowedState == ButtonsState.RIGHT_VISIBLE)
                    dX = Math.min(dX, -buttonWidth);
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

            } else {

                setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }

        if (buttonShowedState == ButtonsState.GONE) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
        currentItemViewHolder = viewHolder;
    }

    private void setTouchListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                buttonWidth = 200;


                swipeBack = event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP;
                if (swipeBack) {
                    if (dX < -buttonWidth) {
                        buttonWidth = 600;
                        buttonShowedState = ButtonsState.RIGHT_VISIBLE;


                    } else if (dX > buttonWidth) {
                        buttonWidth = 200;
                        buttonShowedState = ButtonsState.LEFT_VISIBLE;
                    }

                    if (buttonShowedState != ButtonsState.GONE) {
                        setTouchDownListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                        setItemsClickable(recyclerView, false, viewHolder.getAdapterPosition());
                    }
                }
                return false;
            }
        });
    }

    private void setTouchDownListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    setTouchUpListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
                return false;
            }
        });
    }

    private void setTouchUpListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    SwipeController.super.onChildDraw(c, recyclerView, viewHolder, 0F, dY, actionState, isCurrentlyActive);
                    recyclerView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return false;
                        }
                    });
                    setItemsClickable(recyclerView, true, viewHolder.getAdapterPosition());
                    swipeBack = false;

                    if (buttonsActions != null) {
                        if (buttonShowedState == ButtonsState.LEFT_VISIBLE && buttonInstancePay.contains(event.getX(), event.getY())) {
                            buttonsActions.onLeftClicked(viewHolder.getAdapterPosition(), leftTxt);
                        }

                        else if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) {


                            if (buttonInstanceMore!=null && (buttonInstanceMore.contains(event.getX(), event.getY()))) {
                                buttonsActions.onRightClicked(viewHolder.getAdapterPosition(), "More");

                            } else if (buttonInstanceArchive!=null && (buttonInstanceArchive.contains(event.getX(), event.getY()))) {
                                buttonsActions.onRightClicked(viewHolder.getAdapterPosition(), "Archive");
                            }

                            else if (buttonInstanceTrash!=null && buttonInstanceTrash.contains(event.getX(), event.getY())) {
                                buttonsActions.onRightClicked(viewHolder.getAdapterPosition(), "Trash");
                            }

                            else if (buttonInstanceInbox!=null && buttonInstanceInbox.contains(event.getX(), event.getY())) {
                                buttonsActions.onRightClicked(viewHolder.getAdapterPosition(), "Inbox");
                            }
                        }


                    }

                    buttonShowedState = ButtonsState.GONE;
                    currentItemViewHolder = null;
                }
                return false;
            }
        });
    }

    private void setItemsClickable(RecyclerView recyclerView, boolean isClickable, int pos) {

        for (int i = 0; i < recyclerView.getChildCount(); ++i) {
            recyclerView.getChildAt(i).setClickable(isClickable);
            View view1 = recyclerView.getChildAt(i).findViewById(R.id.imgHistoryArrow_lay);
            if (view1 != null)
                view1.setClickable(isClickable);

        }


    }

    private void drawButtons(Canvas c, RecyclerView.ViewHolder viewHolder) {

        String docLocation="";
        String paymentSatateID="";
        String payText="";

        String viewTag= viewHolder.itemView.getTag().toString();
        String tagArr[]=viewTag.split("-");
        if(tagArr.length==2) {
             docLocation = tagArr[0].toString();
             paymentSatateID = tagArr[1].toString();

             if(paymentSatateID.equals("SCHEDULED"))
                 payText="Modify";
             else  payText="Pay";


            MyApplication.MyLogi("idididi21", " " + docLocation);
            MyApplication.MyLogi("paymentSatateID12", "paymentSatateID12" + paymentSatateID);
        }

        else{
             docLocation =tagArr[0].toString();
            MyApplication.MyLogi("idididi2", " " + docLocation);
            payText="Pay";
        }


        float corners = 0;

        View itemView = viewHolder.itemView;
        Paint p = new Paint();

        float buttonWidthWithoutPadding = 0;
        if (buttonWidth != 200)
            buttonWidthWithoutPadding = 200;
        else buttonWidthWithoutPadding = 200;


        RectF leftButton = new RectF(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + buttonWidthWithoutPadding, itemView.getBottom());
        p.setColor(mContext.getColor(R.color.pay_color));
        c.drawRoundRect(leftButton, corners, corners, p);
        drawText(""+payText, c, leftButton, p);

        if (buttonWidth != 600)
            buttonWidthWithoutPadding = 200;
        else
            buttonWidthWithoutPadding = (float) ((buttonWidth / 3));


        RectF trashButton = null;
        RectF archiveButton = null;
        RectF inboxButton = null;
        if (docLocation.equals("INBOX")) {

            trashButton = new RectF(itemView.getRight() - buttonWidthWithoutPadding, itemView.getTop(), itemView.getRight(), itemView.getBottom());
            p.setColor(mContext.getColor(R.color.trash_color));
            c.drawRoundRect(trashButton, corners, corners, p);
            drawText("Trash", c, trashButton, p);


            archiveButton = new RectF(itemView.getRight() - buttonWidthWithoutPadding - buttonWidthWithoutPadding, itemView.getTop(), itemView.getRight() - buttonWidthWithoutPadding, itemView.getBottom());
            p.setColor(mContext.getColor(R.color.archive_color));
            c.drawRoundRect(archiveButton, corners, corners, p);
            drawText("Archive", c, archiveButton, p);


        } else if (docLocation.equals("ARCHIVE")) {

            trashButton = new RectF(itemView.getRight() - buttonWidthWithoutPadding, itemView.getTop(), itemView.getRight(), itemView.getBottom());
            p.setColor(mContext.getColor(R.color.trash_color));
            c.drawRoundRect(trashButton, corners, corners, p);
            drawText("Trash", c, trashButton, p);


            inboxButton = new RectF(itemView.getRight() - buttonWidthWithoutPadding - buttonWidthWithoutPadding, itemView.getTop(), itemView.getRight() - buttonWidthWithoutPadding, itemView.getBottom());
            p.setColor(mContext.getColor(R.color.inbox_color));
            c.drawRoundRect(inboxButton, corners, corners, p);
            drawText("Inbox", c, inboxButton, p);


        } else if (docLocation.equals("TRASH")) {

            inboxButton = new RectF(itemView.getRight() - buttonWidthWithoutPadding, itemView.getTop(), itemView.getRight(), itemView.getBottom());
            p.setColor(mContext.getColor(R.color.inbox_color));
            c.drawRoundRect(inboxButton, corners, corners, p);
            drawText("Inbox", c, inboxButton, p);

            archiveButton = new RectF(itemView.getRight() - buttonWidthWithoutPadding - buttonWidthWithoutPadding, itemView.getTop(), itemView.getRight() - buttonWidthWithoutPadding, itemView.getBottom());
            p.setColor(mContext.getColor(R.color.archive_color));
            c.drawRoundRect(archiveButton, corners, corners, p);
            drawText("Archive", c, archiveButton, p);
        }


        RectF moreButton = new RectF(itemView.getRight() - buttonWidthWithoutPadding - buttonWidthWithoutPadding - buttonWidthWithoutPadding, itemView.getTop(), itemView.getRight() - buttonWidthWithoutPadding - buttonWidthWithoutPadding, itemView.getBottom());
        p.setColor(mContext.getColor(R.color.more_color));
        c.drawRoundRect(moreButton, corners, corners, p);
        drawText("More", c, moreButton, p);


        buttonInstancePay = null;
        buttonInstanceTrash = null;
        buttonInstanceArchive = null;
        buttonInstanceInbox = null;
        buttonInstanceMore = null;
        if (buttonShowedState == ButtonsState.LEFT_VISIBLE) {
            buttonInstancePay = leftButton;
        } else if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) {
            buttonInstanceTrash = trashButton;
            buttonInstanceArchive = archiveButton;
            buttonInstanceMore = moreButton;
            buttonInstanceInbox = inboxButton;
        }
    }

    private void drawText(String text, Canvas c, RectF button, Paint p) {
        float textSize = 30;
        p.setColor(Color.WHITE);
        p.setAntiAlias(true);
        p.setTextSize(textSize);
        float textWidth = p.measureText(text);

        Bitmap bitmap = null;


        if (text.equals("Inbox")) {
            Bitmap icon = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.inbox_icon);
            bitmap = Bitmap.createScaledBitmap(icon, 50, 50, true);
            c.drawBitmap(bitmap, button.centerX() - bitmap.getWidth() / 2, button.centerY() - bitmap.getHeight(), p);
        }

        else if (text.equals("Trash")) {
            Bitmap icon = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.trash_ic);
            bitmap = Bitmap.createScaledBitmap(icon, 50, 50, true);
            c.drawBitmap(bitmap, button.centerX() - bitmap.getWidth() / 2, button.centerY() - bitmap.getHeight(), p);
        }

        else if (text.equals("Archive")) {
            Bitmap icon = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.archive_ic);
            bitmap = Bitmap.createScaledBitmap(icon, 50, 50, true);
            c.drawBitmap(bitmap, button.centerX() - bitmap.getWidth() / 2, button.centerY() - bitmap.getHeight(), p);
        }

        else if (text.equals("More")) {
            Bitmap icon = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.more_ic);
            bitmap = Bitmap.createScaledBitmap(icon, 50, 20, true);
            c.drawBitmap(bitmap, button.centerX() - bitmap.getWidth() / 2, button.centerY() - 30, p);
        }

        else if (text.equals("Pay") || text.equals("Modify")) {
            Bitmap icon = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.card_ic);
            bitmap = Bitmap.createScaledBitmap(icon, 50, 50, true);
            c.drawBitmap(bitmap, button.centerX() - bitmap.getWidth() / 2, button.centerY() - bitmap.getHeight(), p);

        }


        c.drawText(text, button.centerX() - (textWidth / 2), button.centerY() + 50, p);

    }


    public void onDraw(Canvas c) {
        if (currentItemViewHolder != null) {
            drawButtons(c, currentItemViewHolder);
        }
    }
}

