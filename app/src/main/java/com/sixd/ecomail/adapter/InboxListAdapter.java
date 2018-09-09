package com.sixd.ecomail.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sixd.ecomail.APIManager.APIQueries;
import com.sixd.ecomail.R;
import com.sixd.ecomail.Utility.MyApplication;
import com.sixd.ecomail.model.Custom_InboxDataModel;
import com.sixd.ecomail.model.FundingSources;
import com.sixd.ecomail.model.Payment;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.objectbox.Box;
import io.objectbox.BoxStore;

/**
 * Created by ram on 09-04-2018.
 */

public class InboxListAdapter extends RecyclerView.Adapter<InboxListAdapter.InboxHolder> /*implements
        SwipeAndDragHelper.ActionCompletionContract, SwipeAndDragHelper.SwipeControllerActions*/ {

    Context mContext;
    RecyclerView recyclerView = null;
    List<Custom_InboxDataModel> documents;

    public InboxListAdapter(Context mContext, List<Custom_InboxDataModel> documents) {
        this.mContext = mContext;
        this.documents = documents;
        getColorAttaributes();
    }

    ItemTouchHelper touchHelper = null;

    public void setTouchHelper(ItemTouchHelper touchHelper) {
        this.touchHelper = touchHelper;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public InboxListAdapter.InboxHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.inboxlist_view, parent, false);
        return new InboxHolder(itemView);
    }


    int mExpandedPosition = -1;


    @Override
    public void onBindViewHolder(final InboxHolder holder, final int position) {

        final Custom_InboxDataModel document = documents.get(position);



        Log.i("doc name342345435","name  "+document.getDocumentName());
        List<Payment> paid_paymentList=new APIQueries().getpaymentsByTransmissionID(document.getTransmissionId());

        if(paid_paymentList!=null){
            holder.txtPreStatPay.setText("Previous Statements and Payments ("+paid_paymentList.size()+")");
            double total_amount=0.0;
            holder.docStatements_lay.removeAllViews();
            for(int q=0;q<paid_paymentList.size();q++){
                View item = LayoutInflater.from(mContext).inflate(R.layout.expandviewstatement_lay, null);

                TextView txtCurrentBalance = item.findViewById(R.id.txtCurrentBalance);
                TextView txtFundingBank = item.findViewById(R.id.txtFundingBank);

                TextView txtPreStatPayDate = item.findViewById(R.id.txtPreStatPayDate);
                TextView txtScheduleDate = item.findViewById(R.id.txtScheduleDate);

                double pay_amount=0.0;

                try {
                    pay_amount=Double.parseDouble(paid_paymentList.get(q).getPaymentAmount());
                    total_amount=total_amount+pay_amount;
                }

                catch (NumberFormatException e){e.printStackTrace();}


                txtPreStatPayDate.setText(""+statementDateConvert(document.getPreiodStart())+" - "+statementDateConvert(document.getPreiodEnd()));
                txtScheduleDate.setText("Scheduled on "+dateConvert(paid_paymentList.get(q).getScheduleDate()));
                txtCurrentBalance.setText(""+showPriceInUSD(pay_amount));
                txtFundingBank.setText(""+paid_paymentList.get(q).getBankName());
                holder.docStatements_lay.addView(item);
            }




        }



        String documentName = document.getDocumentName();
        String documentGroupName = new APIQueries().getDocsGroupNameByID(document.getDocumentGroupId());
        String primaryConsumerName = document.getPrimaryConsumerName();
        String contentCreationDate = document.getContentCreationDate();
        String logoUrl = document.getLogoUrl();
        String documentWebLocation = document.getDocumentWebLocation();
        String documentCategroyId = document.getDocumentCategroyId();
        String paymentStateId = document.getPaymentStateId();


        if (documents.get(position).getViewType().equals("1")) {
            double amountDue = 0;
            double minimumAmountDue = 0;
            String actionDueDate = documents.get(position).getActionDueDate();
            try {
                amountDue = Double.parseDouble(documents.get(position).getAmountDue());
                minimumAmountDue = Double.parseDouble(documents.get(position).getMinimumAmountDue());

                holder.txtDueDate.setText("" + dateConvert(actionDueDate));
                holder.txtDueAmout.setText(showPriceInUSD(amountDue));
                holder.txtTotalDue.setText(showPriceInUSD(amountDue));
                holder.txtMinimumDue.setText(showPriceInUSD(minimumAmountDue));
                holder.txtexp_DueDate.setText("" + dateConvert(actionDueDate));
                holder.txtpastdays.setText("" + getPastDays(actionDueDate));

            } catch (NumberFormatException e) {
                e.printStackTrace();
            }


            String actionID = documents.get(position).getActionID();
            if (actionID.equals("DISPLA")) {
                String message = documents.get(position).getMessage();
                holder.displayextraLay.setVisibility(View.VISIBLE);
                holder.txt_actionMSGDispla.setText("" + message);

                holder.displayextraLay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String link = documents.get(position).getLink();
                        Uri uri = Uri.parse(link);
                        Intent webIntent = new Intent(Intent.ACTION_VIEW, uri);
                        mContext.startActivity(webIntent);


                    }
                });

            } else {
                holder.displayextraLay.setVisibility(View.GONE);


            }


            holder.due_lay.setVisibility(View.VISIBLE);
            holder.txtSchedued.setVisibility(View.VISIBLE);
            holder.imgHistoryArrow_lay.setVisibility(View.VISIBLE);


        } else {
            holder.due_lay.setVisibility(View.INVISIBLE);
            holder.txtSchedued.setVisibility(View.GONE);
            holder.imgHistoryArrow_lay.setVisibility(View.INVISIBLE);
            holder.displayextraLay.setVisibility(View.GONE);


        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String contentLocation = documents.get(position).getContentLocation();

                try {
                    Uri uri = Uri.parse(contentLocation);
                    Intent webIntent = new Intent(Intent.ACTION_VIEW, uri);
                    mContext.startActivity(webIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });


        if (paymentStateId.equals("")) {
            holder.txtSchedued.setVisibility(View.GONE);
        } else {
            holder.txtSchedued.setVisibility(View.VISIBLE);
        }


        holder.txtSchedued.setText("" + paymentStateId);
        holder.txtTitle.setText("" + documentGroupName);
        holder.txtStatement.setText("" + documentName);
        holder.txt_recipientName.setText("" + primaryConsumerName);
        holder.txtSchDate.setText("" + dateConvert(contentCreationDate));


        Glide.with(mContext).load(logoUrl)
                .fitCenter()
                .into((holder.imgDocLogo));


        // expandViews
        holder.txtStatementSpand.setText("" + documentName);
        holder.txtBankWebLink.setText("" + documentWebLocation);


        BoxStore boxStore = MyApplication.getInstance().getBoxStore();
        Box<FundingSources> folderBox = boxStore.boxFor(FundingSources.class);

        List<FundingSources> fundingSources = folderBox.getAll();
        FundingSources fundingSource = fundingSources.get(0);
        String bankName = fundingSource.getBankName();
        long currentBalance = Long.parseLong(fundingSource.getCurrentBalance());

       // holder.txtFundingBank.setText("" + bankName);
        //holder.txtCurrentBalance.setText(showPriceInUSD(currentBalance));


        final boolean isExpanded = position == mExpandedPosition;
        holder.expand_view.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.imgHistoryArrowDown.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.imgHistoryArrow.setVisibility(isExpanded ? View.GONE : View.VISIBLE);

        holder.cardlay.setBackgroundColor(isExpanded ? cardactive : carddeactive);


        if (isExpanded) {
            holder.line1.setVisibility(View.GONE);

          /*  holder.line2.setBackgroundColor(baseline);
            holder.txtTitle.setTextColor(cardexpandfirsttxt);
            holder.txtStatement.setTextColor(cardexpandsecondtxt);
            holder.txtDue.setTextColor(cardexpandsecondtxt);
            holder.txtDueDate.setTextColor(cardexpandfirsttxt);
            holder.txtAmout.setTextColor(cardexpandsecondtxt);
            holder.txtDueAmout.setTextColor(cardexpandfirsttxt);
            holder.txtSchDate.setTextColor(cardexpandsecondtxt);*/

            setColorWhenExpandable(holder.line2, holder.txtTitle, holder.txtStatement, holder.txtDue,
                    holder.txtDueDate, holder.txtAmout, holder.txtDueAmout, holder.txtSchDate);

            /* holder.line2.setBackgroundColor(Color.parseColor("#4DFFFFFF"));
           holder.txtTitle.setTextColor(Color.parseColor("#FFFFFF"));
            holder.txtStatement.setTextColor(Color.parseColor("#99FFFFFF"));
            holder.txtDue.setTextColor(Color.parseColor("#99FFFFFF"));
            holder.txtAmout.setTextColor(Color.parseColor("#99FFFFFF"));
            holder.txtDueAmout.setTextColor(Color.parseColor("#FFFFFF"));
            holder.txtSchDate.setTextColor(Color.parseColor("#99FFFFFF"));*/


        } else {
            holder.line1.setVisibility(View.VISIBLE);
            /*holder.line2.setBackgroundColor(baseline1);
            holder.txtTitle.setTextColor(cardfirsttxt);
            holder.txtStatement.setTextColor(cardsectxt);
            holder.txtDue.setTextColor(cardsectxt);
            holder.txtDueDate.setTextColor(cardduedate);
            holder.txtAmout.setTextColor(cardsectxt);
            holder.txtDueAmout.setTextColor(cardfirsttxt);
            holder.txtSchDate.setTextColor(carddoccreatedatetxt);
*/
            setColorNotExpandable(holder.line2, holder.txtTitle, holder.txtStatement, holder.txtDue,
                    holder.txtDueDate, holder.txtAmout, holder.txtDueAmout, holder.txtSchDate);



            /*holder.line2.setBackgroundColor(Color.parseColor("#E1E6ED"));
            holder.txtTitle.setTextColor(Color.parseColor("#041C2C"));
            holder.txtStatement.setTextColor(Color.parseColor("#5F7296"));
            holder.txtDue.setTextColor(Color.parseColor("#5F7296"));
            holder.txtAmout.setTextColor(Color.parseColor("#5F7296"));
            holder.txtDueAmout.setTextColor(Color.parseColor("#041C2C"));
            holder.txtSchDate.setTextColor(Color.parseColor("#5F7296"));*/

        }

        holder.cardlay.setEnabled(isExpanded);
        holder.imgHistoryArrow_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandedPosition = isExpanded ? -1 : position;
                //TransitionManager.beginDelayedTransition(recyclerView);
                notifyDataSetChanged();
            }
        });

        holder.itemView.setTag(document.getDisplayLocation()+"-"+document.getPaymentStateId());


    }


    @Override
    public int getItemCount() {
        return documents.size();
    }

   /* @Override
    public void onViewMoved(int oldPosition, int newPosition) {

    }

    @Override
    public void onViewSwiped(int position) {

    }

    @Override
    public void onLeftClicked(int position) {
        Toast.makeText(mContext, "left clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRightClicked(int position) {
        Toast.makeText(mContext, "right clicked", Toast.LENGTH_SHORT).show();
    }*/


    public class InboxHolder extends RecyclerView.ViewHolder {

        View expand_view, line1, line2;
        RelativeLayout imgHistoryArrow_lay, cardlay, due_lay;
        LinearLayout expand_arr_lay;
        ImageView imgHistoryArrow, imgHistoryArrowDown, imgDocLogo;

        TextView txtTitle, txtStatement, txtDue, txtAmout, txtDueDate, txtDueAmout, txtSchDate, txt_recipientName, txtSchedued;

        // expandViews

        TextView txtStatementSpand, txtBankWebLink, txtTotalDue, txtMinimumDue, txtexp_DueDate, txtpastdays, txtCurrentBalance, txtFundingBank;

        RelativeLayout displayextraLay;
        TextView txt_actionMSGDispla;

        LinearLayout docStatements_lay;
        TextView txtPreStatPayDate, txtScheduleDate,txtPreStatPay;

        public InboxHolder(View itemView) {
            super(itemView);
            expand_view = itemView.findViewById(R.id.expand_view);
            imgHistoryArrow_lay = itemView.findViewById(R.id.imgHistoryArrow_lay);
            cardlay = itemView.findViewById(R.id.cardlay);
            due_lay = itemView.findViewById(R.id.due_lay);
            expand_arr_lay = itemView.findViewById(R.id.expand_arr_lay);
            imgHistoryArrow = itemView.findViewById(R.id.imgHistoryArrow);
            imgHistoryArrowDown = itemView.findViewById(R.id.imgHistoryArrowDown);
            imgDocLogo = itemView.findViewById(R.id.imgDocLogo);

            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtStatement = itemView.findViewById(R.id.txtStatement);
            txtDue = itemView.findViewById(R.id.txtDue);
            txtAmout = itemView.findViewById(R.id.txtAmout);
            txtDueDate = itemView.findViewById(R.id.txtDueDate);
            txtDueAmout = itemView.findViewById(R.id.txtDueAmout);
            txtSchDate = itemView.findViewById(R.id.txtSchDate);
            txt_recipientName = itemView.findViewById(R.id.txt_recipientName);
            txtSchedued = itemView.findViewById(R.id.txtSchedued);

            line1 = itemView.findViewById(R.id.line1);
            line2 = itemView.findViewById(R.id.line2);


            // expandViews
            txtStatementSpand = itemView.findViewById(R.id.txtStatementSpand);
            txtBankWebLink = itemView.findViewById(R.id.txtBankWebLink);
            txtTotalDue = itemView.findViewById(R.id.txtTotalDue);
            txtMinimumDue = itemView.findViewById(R.id.txtMinimumDue);
            txtexp_DueDate = itemView.findViewById(R.id.txtexp_DueDate);
            txtpastdays = itemView.findViewById(R.id.txtpastdays);


            displayextraLay = itemView.findViewById(R.id.displayextraLay);
            txt_actionMSGDispla = itemView.findViewById(R.id.txt_actionMSGDispla);

            docStatements_lay = itemView.findViewById(R.id.docStatements_lay);
            txtPreStatPay=itemView.findViewById(R.id.txtPreStatPay);
        }
    }


    public String dateConvert(String dateStr) {

        String[] dateStrs = dateStr.split(" ");
        String tempstr = dateStrs[0];

        String out = "";
        SimpleDateFormat month_date = new SimpleDateFormat("MMM dd", Locale.ENGLISH);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(tempstr);
            out = month_date.format(date);
            MyApplication.MyLogi("out utc 32", "" + out);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return out;
    }

    public String getPastDays(String actionDueDate) {
        String[] dateStrs = actionDueDate.split(" ");
        String tempstr = dateStrs[0];
        String dayspasetStr = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date todayDate = new Date();
            String modifiedDate = sdf.format(todayDate);
            Date formated_todayDate = sdf.parse(modifiedDate);
            Date formated_actionDueDate = sdf.parse(tempstr);
            long diff = formated_todayDate.getTime() - formated_actionDueDate.getTime();
            long daysLong = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            dayspasetStr = String.valueOf(daysLong);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dayspasetStr;


    }

    public static String showPriceInUSD(double balanceUSD) {
        // NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "in")); // for indian currency

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        String currencrStr = currencyFormat.format(balanceUSD);
        return currencrStr;

    }

    @ColorInt
    int cardactive, carddeactive, baseline, cardexpandfirsttxt, cardexpandsecondtxt,
            baseline1, cardfirsttxt, cardsectxt, cardduedate, carddoccreatedatetxt;

    public void getColorAttaributes() {

        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = mContext.getTheme();

        theme.resolveAttribute(R.attr.cardactive, typedValue, true);
        cardactive = typedValue.data;

        theme.resolveAttribute(R.attr.carddeactive, typedValue, true);
        carddeactive = typedValue.data;


        theme.resolveAttribute(R.attr.baseline, typedValue, true);
        baseline = typedValue.data;

        theme.resolveAttribute(R.attr.cardexpandfirsttxt, typedValue, true);
        cardexpandfirsttxt = typedValue.data;

        theme.resolveAttribute(R.attr.cardexpandsecondtxt, typedValue, true);
        cardexpandsecondtxt = typedValue.data;


        theme.resolveAttribute(R.attr.baseline, typedValue, true);
        baseline1 = typedValue.data;


        theme.resolveAttribute(R.attr.cardfirsttxt, typedValue, true);
        cardfirsttxt = typedValue.data;

        theme.resolveAttribute(R.attr.cardsectxt, typedValue, true);
        cardsectxt = typedValue.data;

        theme.resolveAttribute(R.attr.cardduedate, typedValue, true);
        cardduedate = typedValue.data;

        theme.resolveAttribute(R.attr.carddoccreatedatetxt, typedValue, true);
        carddoccreatedatetxt = typedValue.data;


    }


    public void setColorWhenExpandable(View line2, TextView txtTitle, TextView txtStatement, TextView txtDue,
                                       TextView txtDueDate, TextView txtAmout, TextView txtDueAmout, TextView txtSchDate) {

        line2.setBackgroundColor(baseline);
        txtTitle.setTextColor(cardexpandfirsttxt);
        txtStatement.setTextColor(cardexpandsecondtxt);
        txtDue.setTextColor(cardexpandsecondtxt);
        txtDueDate.setTextColor(cardexpandfirsttxt);
        txtAmout.setTextColor(cardexpandsecondtxt);
        txtDueAmout.setTextColor(cardexpandfirsttxt);
        txtSchDate.setTextColor(cardexpandsecondtxt);

    }


    public void setColorNotExpandable(View line2, TextView txtTitle, TextView txtStatement, TextView txtDue,
                                      TextView txtDueDate, TextView txtAmout, TextView txtDueAmout, TextView txtSchDate) {

        line2.setBackgroundColor(baseline1);
        txtTitle.setTextColor(cardfirsttxt);
        txtStatement.setTextColor(cardsectxt);
        txtDue.setTextColor(cardsectxt);
        txtDueDate.setTextColor(cardduedate);
        txtAmout.setTextColor(cardsectxt);
        txtDueAmout.setTextColor(cardfirsttxt);
        txtSchDate.setTextColor(carddoccreatedatetxt);

    }

    public String statementDateConvert(String dateStr) {

        String[] dateStrs = dateStr.split(" ");
        String tempstr = dateStrs[0];

        String out = "";
        SimpleDateFormat month_date = new SimpleDateFormat("MMM dd YYYY", Locale.ENGLISH);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(tempstr);
            out = month_date.format(date);
            MyApplication.MyLogi("out utc 32", "" + out);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return out;
    }


}
