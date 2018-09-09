package com.sixd.ecomail.Utility;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.TextView;

import com.sixd.ecomail.R;

/**
 * Created by Praveen on 27-Mar-18.
 */

public class ProgressLoaderUtilities {

    TextView txt_wait;
    public ProgressDialog getProgressDialog(Context context){

        ProgressDialog progressDialog = new ProgressDialog(context);
        Window window = progressDialog.getWindow();
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT);
        progressDialog.show();
        progressDialog.setContentView(R.layout.customprogressbar);
         txt_wait=progressDialog.getWindow().findViewById(R.id.txt_wait);

        return progressDialog;
    }

    public void setWaitingTxt(String msg){
        txt_wait.setText(""+msg);
    }
}
