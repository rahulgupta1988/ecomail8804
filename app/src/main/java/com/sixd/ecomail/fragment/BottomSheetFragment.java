package com.sixd.ecomail.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sixd.ecomail.R;
import com.sixd.ecomail.Utility.MyApplication;

/**
 * Created by Rahul Gupta on 01-06-2018.
 */
public class BottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener{

    Context mContext;
    View view;
    TextView txtActions,txtSubscription,txtRules,txtAlerts,txtWebsite,txtCancel;


    public BottomSheetFragment() {
        super.setCancelable(false);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_bottom_sheet_dialog, container, false);
        initViews();
        return view;
    }



    public void initViews() {
        txtActions=view.findViewById(R.id.txtActions);
        txtSubscription=view.findViewById(R.id.txtSubscription);
        txtRules=view.findViewById(R.id.txtRules);
        txtAlerts=view.findViewById(R.id.txtAlerts);
        txtWebsite=view.findViewById(R.id.txtWebsite);
        txtCancel=view.findViewById(R.id.txtCancel);

        txtActions.setOnClickListener(this);
        txtSubscription.setOnClickListener(this);
        txtRules.setOnClickListener(this);
        txtAlerts.setOnClickListener(this);
        txtWebsite.setOnClickListener(this);
        txtCancel.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.txtActions:
                Toast.makeText(mContext,"Action",Toast.LENGTH_SHORT).show();
                break;

            case R.id.txtSubscription:
                Toast.makeText(mContext,"txtSubscription",Toast.LENGTH_SHORT).show();
                break;

            case R.id.txtRules:
                Toast.makeText(mContext,"txtRules",Toast.LENGTH_SHORT).show();
                break;

            case R.id.txtAlerts:
                Toast.makeText(mContext,"txtAlerts",Toast.LENGTH_SHORT).show();
                break;

            case R.id.txtWebsite:
                Toast.makeText(mContext,"txtWebsite",Toast.LENGTH_SHORT).show();
                break;

            case R.id.txtCancel:
                dismiss();
                break;
        }

    }
}
