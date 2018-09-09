package com.sixd.ecomail;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.sixd.ecomail.Utility.Constant;
import com.sixd.ecomail.Utility.StateLIstParese;
import com.sixd.ecomail.adapter.SettingAddressAdapter;
import com.sixd.ecomail.adapter.StateAdapter;
import com.sixd.ecomail.model.State;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rahul Gupta on 16-04-2018.
 */

public class AddAddressActivity extends AppCompatActivity {
    Context mContext;
    ImageView img_back, img_save;
    EditText eState;
    List<State> stateListData = null;
    List<State> stateListDataSearch = new ArrayList<State>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addadress_view);
        mContext = this;
        stateListData=new StateLIstParese().getStateList(mContext);
        stateListDataSearch.addAll(stateListData);
        initViews();
    }

    public void initViews() {
        img_back = findViewById(R.id.img_back);
        img_save = findViewById(R.id.img_save);
        eState=findViewById(R.id.eState);


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        img_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        eState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stateListDataSearch.clear();
                stateListDataSearch.addAll(stateListData);
                stateView();
            }
        });

    }



    public void stateView(){
        stateName=eState.getText().toString().trim();
        final Dialog stateDailog = new Dialog(this,R.style.AppTheme);
        stateDailog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        stateDailog.setContentView(R.layout.stateview);
        stateDailog.setCancelable(false);

        ImageView img_back=stateDailog.findViewById(R.id.img_back);
        ImageView img_save=stateDailog.findViewById(R.id.img_save);
        RecyclerView stateList=stateDailog.findViewById(R.id.stateList);
        ImageView imgSearch=stateDailog.findViewById(R.id.imgSearch);
        ImageView imgRemoveText=stateDailog.findViewById(R.id.imgRemoveText);
        final ImageView imgSearcBack=stateDailog.findViewById(R.id.imgSearcBack);
        final View searchbar=stateDailog.findViewById(R.id.searchbar);
        final RelativeLayout headerview=stateDailog.findViewById(R.id.headerview);
        final EditText editSearch=stateDailog.findViewById(R.id.editSearch);


        imgRemoveText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editSearch.setText("");
            }
        });

        final StateAdapter stateAdapter = new StateAdapter(mContext,stateName,stateListDataSearch);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        stateList.setLayoutManager(layoutManager);
        stateList.setItemAnimator(new DefaultItemAnimator());
        stateList.setAdapter(stateAdapter);

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                headerview.setVisibility(View.GONE);
                searchbar.setVisibility(View.VISIBLE);
                editSearch.requestFocus();

                final InputMethodManager inputMethodManager =
                        (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInputFromWindow(
                        editSearch.getApplicationWindowToken(),
                        InputMethodManager.SHOW_FORCED, 0);


                imgSearcBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       /* stateAdapter.notifyDataSetChanged();
                        editSearch.setText("");*/
                        searchbar.setVisibility(View.GONE);
                        headerview.setVisibility(View.VISIBLE);
                        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);


                    }
                });

                editSearch.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        List<State> temp = new ArrayList();
                        for(State d: stateListData){
                            //or use .equal(text) with you want equal match
                            //use .toLowerCase() for better matches
                            if(d.getName().toLowerCase().contains(s.toString().trim().toLowerCase())){
                                temp.add(d);
                            }
                        }
                        //update recyclerview
                        stateListDataSearch.clear();
                        stateListDataSearch.addAll(temp);
                        stateAdapter.notifyDataSetChanged();
                    }
                });





            }
        });



        img_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(stateName.length()>0){
                    eState.setText(""+stateName);
                    stateDailog.dismiss();
                }

                else{
                    Toast.makeText(mContext,""+ Constant.Message.STATE_ERROR,Toast.LENGTH_SHORT).show();
                }
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stateDailog.dismiss();
            }
        });

        stateDailog.show();
    }


    String stateName="";
    public void setState(String stateName){
        this.stateName=stateName;
    }
}
