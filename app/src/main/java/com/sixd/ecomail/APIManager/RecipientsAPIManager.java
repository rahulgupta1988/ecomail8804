package com.sixd.ecomail.APIManager;

import android.content.Context;

import com.sixd.ecomail.Utility.Constant;
import com.sixd.ecomail.Utility.MyApplication;
import com.sixd.ecomail.Utility.ServerCall;
import com.sixd.ecomail.model.Folder;
import com.sixd.ecomail.model.Recipents;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import io.objectbox.Box;
import io.objectbox.BoxStore;

/**
 * Created by Rahul Gupta on 30-04-2018.
 */

public class RecipientsAPIManager {

    Context _mContext;
    Map responseString;

    public  RecipientsAPIManager(Context context){
        _mContext=context;

    }

    public String callRecipentsAPI(String consumerId)
    {

        String request_str="  <soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:q0=\"http://webservice.emx.ecomail.com/\">\n" +
                "  <soapenv:Body>\n" +
                "    <q0:getEMXResponse q0:type=\"emxService:getEMXResponse\">\n" +
                "      <emxRequest xsi:type=\"emxService:emxRequest\">\n" +
                "        <requestXml>&lt;emx:ConsumerRequest xmlns:emx=\"http://www.eco-mail.com/ConsumerRequest\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.eco-mail.com/ConsumerRequest/ConsumerRequestResponse.xsd\"&gt; &lt;emx:getAllRecipients  methodId=\"getAllRecipients\" consumerId=\""+consumerId+"\" /&gt; &lt;/emx:ConsumerRequest&gt;</requestXml>\n" +
                "      </emxRequest>\n" +
                "    </q0:getEMXResponse>\n" +
                "  </soapenv:Body>\n" +
                "</soapenv:Envelope>";






            responseString= ServerCall.makeConnection(Constant.BASEURL,request_str,_mContext);

        return parseeResponce(responseString) ;

    }


    public String parseeResponce(Map responsemap){

        String return_str="";
        String responseStr="";
        if (responsemap.containsKey("code")) {
            Object value = responsemap.get("code");

            if(value.toString().equals("200")){

                responseStr= responsemap.get("responce").toString();

                XmlPullParserFactory pullParserFactory;
                try {

                    //https://www.studytutorial.in/android-xml-parsing-soap-response-parsing-tutorial-using-dom-parser

                    DocumentBuilder newDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                    Document parse = newDocumentBuilder.parse(new ByteArrayInputStream(responseStr.getBytes()));
                    String json_str=parse.getElementsByTagName("responseXml").item(0).getTextContent();

                    Object object = new JSONTokener(json_str).nextValue();
                    MyApplication.MyLogi("Recipents resp",""+object.toString());
                    if (object instanceof JSONObject) {
                        JSONObject jsonObject = new JSONObject(json_str);
                        if (jsonObject.has("con:getAllRecipientsResponses")) {
                            return_str = Constant.SUCCESS_MSG;
                            BoxStore boxStore = MyApplication.getInstance().getBoxStore();
                            Box<Recipents> recipBox = boxStore.boxFor(Recipents.class);
                            recipBox.removeAll();
                            JSONObject jsonObjectResponses = jsonObject.getJSONObject("con:getAllRecipientsResponses");

                            if (jsonObjectResponses.length() != 0) {

                                JSONArray allRecipentsArray = jsonObjectResponses.getJSONArray("con:recipient");

                                for (int i = 0; i < allRecipentsArray.length(); i++) {
                                    JSONObject recipentsJson = allRecipentsArray.getJSONObject(i);
                                    String createDate = recipentsJson.getString("createDate");
                                    String image = recipentsJson.getString("image");
                                    String consumerId = recipentsJson.getString("consumerId");
                                    String networkLoginId = recipentsJson.getString("networkLoginId");
                                    String recipientName = recipentsJson.getString("recipientName");
                                    String recipientType = recipentsJson.getString("recipientType");


                                    Recipents recipents = new Recipents(createDate, image, consumerId, networkLoginId, recipientName, recipientType);
                                    recipBox.put(recipents);
                                }
                            } else if (jsonObject.has("con:errorResponse")) {
                                JSONObject jsonObject1 = jsonObject.getJSONObject("con:errorResponse");

                                String err_msg = jsonObject1.getString("errorMessage");
                                return_str = err_msg;
                            }

                        }
                    }


                }  catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            else{
                return_str=value.toString();
            }
        }


        return  return_str;

    }
}
