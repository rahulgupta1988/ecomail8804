package com.sixd.ecomail.APIManager;

import android.content.Context;

import com.sixd.ecomail.Utility.Constant;
import com.sixd.ecomail.Utility.MyApplication;
import com.sixd.ecomail.Utility.ServerCall;
import com.sixd.ecomail.model.DocTypes;
import com.sixd.ecomail.model.DocumentGroup;
import com.sixd.ecomail.model.Folder;
import com.sixd.ecomail.model.Recipents;
import com.sixd.ecomail.model.SmartFolder;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import io.objectbox.Box;
import io.objectbox.BoxStore;

/**
 * Created by Rahul Gupta on 30-04-2018.
 */

public class SmartFoldersAPIManager {

    Context _mContext;
    Map responseString;

    public SmartFoldersAPIManager(Context context) {
        _mContext = context;

    }

    public String callSmartFolderAPI(String consumerId) {

        String request_str = "<?xml version=\"1.0\"?>\n" +
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:q0=\"http://webservice.emx.ecomail.com/\">\n" +
                "  <soapenv:Body>\n" +
                "    <q0:getEMXResponse q0:type=\"emxService:getEMXResponse\">\n" +
                "      <emxRequest xsi:type=\"emxService:emxRequest\">\n" +
                "        <requestXml>&lt;emx:ConsumerRequest xmlns:emx=\"http://www.eco-mail.com/ConsumerRequest\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.eco-mail.com/ConsumerRequest/ConsumerRequestResponse.xsd\"&gt; &lt;emx:getSmartFolders consumerId=\"" + consumerId + "\" methodId=\"getSmartFolders\"/&gt; &lt;/emx:ConsumerRequest&gt;</requestXml>\n" +
                "      </emxRequest>\n" +
                "    </q0:getEMXResponse>\n" +
                "  </soapenv:Body>\n" +
                "</soapenv:Envelope>\n";


        responseString = ServerCall.makeConnection(Constant.BASEURL, request_str, _mContext);

        return parseeResponce(responseString);

    }


    public String parseeResponce(Map responsemap) {

        String return_str = "";
        String responseStr = "";
        if (responsemap.containsKey("code")) {
            Object value = responsemap.get("code");

            if (value.toString().equals("200")) {

                responseStr = responsemap.get("responce").toString();

                XmlPullParserFactory pullParserFactory;
                try {

                    //https://www.studytutorial.in/android-xml-parsing-soap-response-parsing-tutorial-using-dom-parser

                    DocumentBuilder newDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                    Document parse = newDocumentBuilder.parse(new ByteArrayInputStream(responseStr.getBytes()));
                    String json_str = parse.getElementsByTagName("responseXml").item(0).getTextContent();

                    Object object = new JSONTokener(json_str).nextValue();
                    MyApplication.MyLogi("smart folder resp", "" + object.toString());
                    if (object instanceof JSONObject) {
                        JSONObject jsonObject = new JSONObject(json_str);

                        if (jsonObject.has("con:getSmartFoldersResponse")) {
                            return_str = Constant.SUCCESS_MSG;
                            JSONObject jsonObject1 = jsonObject.getJSONObject("con:getSmartFoldersResponse");
                            if (jsonObject1.has("con:smartFolderDetail")) {
                                BoxStore boxStore = MyApplication.getInstance().getBoxStore();
                                Box<SmartFolder> smartFolderBox = boxStore.boxFor(SmartFolder.class);
                                smartFolderBox.removeAll();

                                JSONArray smartFolderArray = jsonObject1.getJSONArray("con:smartFolderDetail");



                                for (int k = 0; k < smartFolderArray.length(); k++) {

                                    JSONObject smartJson = smartFolderArray.getJSONObject(k);
                                    int order = smartJson.getInt("order");
                                    String createDate = smartJson.getString("createDate");
                                    String smartFolderId = smartJson.getString("smartFolderId");
                                    String smartFolderName = smartJson.getString("smartFolderName");
                                    String updateDate = smartJson.getString("updateDate");

                                    SmartFolder temp_check=new APIQueries().getSmartFolderByName(smartFolderName);

                                    if(temp_check!=null) continue;

                                    SmartFolder smartFolder = new SmartFolder(order, createDate, smartFolderId, smartFolderName, updateDate);

                                    JSONObject textJson = null;
                                    try {
                                        textJson = smartJson.getJSONObject("#text");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        continue;
                                    }


                                    if (textJson.has("DocumentGroups")) {

                                        JSONArray docGPArray = textJson.getJSONArray("DocumentGroups");
                                        for (int m = 0; m < docGPArray.length(); m++) {
                                            String docGPID = docGPArray.getString(m);
                                            DocumentGroup documentGroup = new APIQueries().getDocumentGroupByID(docGPID);
                                            smartFolder.documentGroupToMany.add(documentGroup);
                                        }

                                    }


                                    if (textJson.has("DocumentTypes")) {

                                        JSONArray docTypeArray = textJson.getJSONArray("DocumentTypes");
                                        for (int n = 0; n < docTypeArray.length(); n++) {
                                            String doctypeID = docTypeArray.getString(n);
                                            DocTypes docTypes = new APIQueries().getDocumentTypeByID(doctypeID);
                                            smartFolder.docTypesToMany.add(docTypes);
                                        }

                                    }


                                    if (textJson.has("Recipients")) {

                                        JSONArray recipArray = textJson.getJSONArray("Recipients");
                                        for (int p = 0; p < recipArray.length(); p++) {
                                            String recipID = recipArray.getString(p);
                                            Recipents recipents = new APIQueries().getRecipentsByID(recipID);
                                            smartFolder.recipentsToMany.add(recipents);
                                        }

                                    }





                                    if (textJson.has("Tags")) {
                                        JSONArray tagsArray = textJson.getJSONArray("Tags");
                                        for (int l = 0; l < tagsArray.length(); l++) {

                                            String folderid = tagsArray.getString(l);
                                            Folder folder = new APIQueries().getFolderByID(folderid);
                                            smartFolder.folderToMany.add(folder);
                                        }
                                    }


                                    smartFolderBox.put(smartFolder);
                                }


                            } else if (jsonObject.has("con:errorResponse")) {
                                JSONObject jsonObject_error = jsonObject.getJSONObject("con:errorResponse");
                                String err_msg = jsonObject_error.getString("errorMessage");
                                return_str = err_msg;
                            }


                        } else if (jsonObject.has("con:errorResponse")) {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("con:errorResponse");

                            String err_msg = jsonObject1.getString("errorMessage");
                            return_str = err_msg;
                        }

                    }


                } catch (UnsupportedEncodingException e) {
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

            } else {
                return_str = value.toString();
            }
        }


        return return_str;

    }
}
