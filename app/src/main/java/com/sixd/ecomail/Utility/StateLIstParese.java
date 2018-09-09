package com.sixd.ecomail.Utility;

import android.content.Context;
import android.util.Log;

import com.sixd.ecomail.model.State;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ram on 16-04-2018.
 */

public class StateLIstParese {


    public String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("usastates.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }



    public ArrayList<State> getStateList(Context context) {
        ArrayList<State> stateList = new ArrayList<>();
        try {
            JSONArray m_jArry = new JSONArray(loadJSONFromAsset(context));



            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                String name = jo_inside.getString("name");
                String abbreviation = jo_inside.getString("abbreviation");
                String alph = jo_inside.getString("alph");
                String isFirst = jo_inside.getString("isFirst");
                //Add your values in your `ArrayList` as below:
                State state=new State(name,abbreviation,isFirst,alph);
                stateList.add(state);
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

        return stateList;

    }
}
