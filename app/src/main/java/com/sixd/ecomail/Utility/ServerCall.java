package com.sixd.ecomail.Utility;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Praveen on 26-Mar-18.
 */

public class ServerCall {

    Context context;
    public static Map makeConnection(String baseUrl,String requestBody,Context context) {
        String response = "";
      URL url;
        StringBuilder sb = new StringBuilder();
        Map<String,String> res_map=new HashMap<String,String>();



      try {



           url = new URL(baseUrl);
           HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //HttpURLConnection conn = setUpHttpsConnection("https://ec2-52-13-46-94.us-west-2.compute.amazonaws.com:8443/webservice/emxService/");
            conn.setRequestProperty("Content-Type", "text/xml");
            conn.setRequestProperty("Content-Language", "en-US");
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
             conn.setConnectTimeout(20000);
             conn.setReadTimeout(20000);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(requestBody);
            writer.flush();
            writer.close();
            os.close();

            int responseCode = conn.getResponseCode();
            MyApplication.MyLogi("codeq324",""+responseCode);
            res_map.put("code",String.valueOf(responseCode));
            switch (responseCode) {

                case HttpURLConnection.HTTP_OK:
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    sb = new StringBuilder();
                    while ((response = br.readLine()) != null) {
                        sb.append(response);}
                    break;
                case HttpURLConnection.HTTP_GATEWAY_TIMEOUT:
                    response = "Gateway Timeout.";
                    sb.append(response);
                    break;
                case HttpURLConnection.	HTTP_CLIENT_TIMEOUT:
                    response = "Request Time-Out.";
                    sb.append(response);
                    break;

                case HttpURLConnection.HTTP_INTERNAL_ERROR:
                    response = "Internal Server Error.";
                    sb.append(response);
                    break;

                case HttpURLConnection.HTTP_UNAVAILABLE:
                    response = "Service Temporarily Unavailable.";
                    sb.append(response);
                    break;

                default:
                    response = "Some error occurred, please try again.";
                    sb.append(response);
                    break;
            }


        }

        catch (Exception e) {
            response = "Some error occurred, please try again.";
            sb.append(response);
            e.printStackTrace();
            res_map.put("responce",sb.toString());
            return res_map;

        }
        res_map.put("responce",sb.toString());
        return res_map;


    }


    private static String getPostDataString(HashMap<String, String> params) {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            try {
                MyApplication.MyLogi(""+entry.getKey(),""+entry.getValue());
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return result.toString();
    }








}
