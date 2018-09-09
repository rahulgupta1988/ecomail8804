package com.sixd.ecomail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageView;

import com.sixd.ecomail.APIManager.APIQueries;
import com.sixd.ecomail.Utility.DragNDropHelper;
import com.sixd.ecomail.Utility.MyApplication;
import com.sixd.ecomail.Utility.SwipeAndDragHelper;
import com.sixd.ecomail.adapter.SortInboxAdapter;
import com.sixd.ecomail.model.InboxSort;
import com.sixd.ecomail.model.InboxSortOrder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;

/**
 * Created by Rahul Gupta on 10-04-2018.
 */

public class InboxSortActivity extends AppCompatActivity {

    Context mContext;
    ImageView img_back, img_save;
    RecyclerView sort_list;
    List<InboxSortOrder> sortInboxItemList=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int themeID=MyApplication.getInstance().getThemeID();
        setTheme(themeID);
        setContentView(R.layout.sortinboxdialogview);
        mContext = this;
        initView();
    }

    public void initView() {
        img_back = findViewById(R.id.img_back);
        img_save = findViewById(R.id.img_save);
        sort_list = findViewById(R.id.sort_list);

        setSortItemList();


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        img_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long rows= new APIQueries().removeSortOptionBycatName("Inbox");

                MyApplication.MyLogi("rows454",""+rows);

                if(rows>0){
                    BoxStore boxStore = MyApplication.getInstance().getBoxStore();
                    Box<InboxSortOrder> inboxSortOrderBox=boxStore.boxFor(InboxSortOrder.class);

                    for(int i=0;i<sortInboxItemList.size();i++){
                        InboxSortOrder inboxSortOrder=sortInboxItemList.get(i);
                        MyApplication.MyLogi("orderSave",""+inboxSortOrder.getDefaultOrder()+" "+inboxSortOrder.getCaption());
                        inboxSortOrder.setDefaultOrder(i+1);
                        inboxSortOrderBox.put(inboxSortOrder);
                    }
                }

                Intent intent = new Intent();
                setResult(602, intent);
                finish();//finishing activity

                //onBackPressed();

            }
        });
    }

    public void setSortItemList() {

        sortInboxItemList=new APIQueries().getSortOptionBycatName("Inbox");

        Collections.sort(sortInboxItemList, new Comparator<InboxSortOrder>() {
            @Override
            public int compare(InboxSortOrder inboxSortOrder, InboxSortOrder t1) {
                return (int) (inboxSortOrder.getDefaultOrder() - t1.getDefaultOrder());
            }
        });

        for(int i=0;i<sortInboxItemList.size();i++){
            InboxSortOrder inboxSortOrder=sortInboxItemList.get(i);
            MyApplication.MyLogi("orderfirst",""+inboxSortOrder.getDefaultOrder()+" "+inboxSortOrder.getCaption());
        }

        SortInboxAdapter sortInboxAdapter = new SortInboxAdapter(mContext,sortInboxItemList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        sort_list.setLayoutManager(layoutManager);
        sort_list.setItemAnimator(new DefaultItemAnimator());



        DragNDropHelper dragNDropHelper = new DragNDropHelper(sortInboxAdapter,mContext);
        ItemTouchHelper touchHelper = new ItemTouchHelper(dragNDropHelper);
        sortInboxAdapter.setTouchHelper(touchHelper);
        sort_list.setAdapter(sortInboxAdapter);
        touchHelper.attachToRecyclerView(sort_list);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}
