package com.sixd.ecomail.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sixd.ecomail.LoginActivity;
import com.sixd.ecomail.R;
import com.sixd.ecomail.Utility.SharedPreferencesManager;
import com.sixd.ecomail.model.Theme;

import java.util.ArrayList;

/**
 * Created by Praveen on 27-Mar-18.
 */

public class LoginSpinnerAdapter extends BaseAdapter {

   Context context;
   ArrayList<Theme> themes;
    LayoutInflater inflter;

    public LoginSpinnerAdapter(Context context,ArrayList<Theme> themes){
        this.context=context;
        this.themes=themes;
        inflter = (LayoutInflater.from(context));

    }

    @Override
    public int getCount() {
        return themes.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    ImageView lastSelected=null;
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        view = inflter.inflate(R.layout.spinner_item_view, null);
        LinearLayout prent_lay=view.findViewById(R.id.prent_lay);
        final ImageView imgChecked = view.findViewById(R.id.imgChecked);
        TextView txtThemeName = view.findViewById(R.id.txtThemeName);
        String themeName=themes.get(i).getThemeName();
        txtThemeName.setText(""+themeName);


       if(SharedPreferencesManager.getThemeName(context)!=null) {
           if (SharedPreferencesManager.getThemeName(context).equals(themeName)){
               imgChecked.setVisibility(View.VISIBLE);
               lastSelected=imgChecked;
           }

           else
               imgChecked.setVisibility(View.GONE);
       }

        prent_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(lastSelected!=null){
                    lastSelected.setVisibility(View.GONE);

                    imgChecked.setVisibility(View.VISIBLE);
                    lastSelected=imgChecked;
                }

               else {
                    imgChecked.setVisibility(View.VISIBLE);
                    lastSelected = imgChecked;
                }

                ((LoginActivity)context).selectedTheme(i);

            }
        });

        return view;
    }
}
