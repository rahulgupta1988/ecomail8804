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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import io.objectbox.Box;
import io.objectbox.BoxStore;

/**
 * Created by Rahul Gupta on 13-07-2018.
 */
public class AddSmartFolderManager {
    Context _mContext;
    Map responseString;
    int sequenceNoInt;

    public AddSmartFolderManager(Context context) {
        _mContext = context;

    }

    public String callAddSmartFolderAPI(String smartFolderName, String consumerId, String queryJson,int order) {

        String sequenceNo = String.valueOf(order);


        String request_str = "<?xml version=\"1.0\"?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:q0=\"http://webservice.emx.ecomail.com/\"><soapenv:Body><q0:getEMXResponse q0:type=\"emxService:getEMXResponse\"><emxRequest xsi:type=\"emxService:emxRequest\"><requestXml>&lt;emx:ConsumerRequest xmlns:emx=\"http://www.eco-mail.com/ConsumerRequest\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.eco-mail.com/ConsumerRequest/ConsumerRequestResponse.xsd\"&gt;&lt;emx:createSmartFolders " +
                "consumerId=\"" + consumerId + "\" " +
                "methodId=\"createSmartFolders\"&gt;&lt;emx:smartFolderDetail " +
                "order=\"" + sequenceNo + "\" " +
                "smartFolderName=\"" +smartFolderName+ "\""+
                 "&gt;"+queryJson +
                "&lt;/emx:smartFolderDetail&gt;&lt;/emx:createSmartFolders&gt; &gt; &lt;/emx:ConsumerRequest&gt;</requestXml></emxRequest></q0:getEMXResponse></soapenv:Body></soapenv:Envelope>";


        MyApplication.MyLogi("request_str", "" + request_str.toString());

       responseString = ServerCall.makeConnection(Constant.BASEURL, request_str, _mContext);

        return parseResponce(responseString);

    }


    public String parseResponce(Map responsemap) {

        String return_str = "";
        String responseStr = "";
        if (responsemap.containsKey("code")) {
            Object value = responsemap.get("code");

            if (value.toString().equals("200")) {

                responseStr = responsemap.get("responce").toString();
                MyApplication.MyLogi("Add Folder resp", "" + responseStr.toString());
                XmlPullParserFactory pullParserFactory;
                try {

                    //https://www.studytutorial.in/android-xml-parsing-soap-response-parsing-tutorial-using-dom-parser

                    DocumentBuilder newDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                    Document parse = newDocumentBuilder.parse(new ByteArrayInputStream(responseStr.getBytes()));
                    String json_str = parse.getElementsByTagName("responseXml").item(0).getTextContent();

                    Object object = new JSONTokener(json_str).nextValue();
                    MyApplication.MyLogi("Add Folder resp", "" + object.toString());
                   if (object instanceof JSONObject) {
                       JSONObject jsonObject = new JSONObject(json_str);

                       if (jsonObject.has("con:createSmartFoldersResponse")) {
                           JSONObject jsonObject1 = jsonObject.getJSONObject("con:createSmartFoldersResponse");
                           if (jsonObject1.has("con:smartFolderDetail")) {
                               BoxStore boxStore = MyApplication.getInstance().getBoxStore();
                               Box<SmartFolder> smartFolderBox = boxStore.boxFor(SmartFolder.class);




                               if(jsonObject1.has("con:smartFolderDetail")){

                                   JSONObject smartJson=jsonObject1.getJSONObject("con:smartFolderDetail");
                                   String messageType=smartJson.getString("messageType");

                                   if(messageType.equals("Success")){
                                       return_str = Constant.SUCCESS_MSG;
                                       int order = smartJson.getInt("order");
                                       String createDate = smartJson.getString("createDate");
                                       String smartFolderId = smartJson.getString("smartFolderId");
                                       String smartFolderName = smartJson.getString("smartFolderName");

                                       SmartFolder smartFolder = new SmartFolder(order, createDate, smartFolderId, smartFolderName, "");

                                       JSONObject textJson = smartJson.getJSONObject("#text");

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


                                   else {
                                       return_str = "Error";
                                   }


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


    public String callRemoveFolderAPI(String folderName, String consumerId, int sequenceNoInt,String folderID) {
        this.sequenceNoInt=sequenceNoInt;
        String sequenceNo = String.valueOf(sequenceNoInt);

        Date now = new Date();

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS ZZZ");
        String currentdateTimeStr = df.format(now);

   /*     String request_str = "<?xml version=\"1.0\"?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:q0=\"http://webservice.emx.ecomail.com/\"><soapenv:Body><q0:getEMXResponse q0:type=\"emxService:getEMXResponse\"><emx:updateSmartFolders " +
                "consumerId=\"" + consumerId + "\" " +
                "methodId=\"updateSmartFolders\">" +
                "<emx:smartFolderDetail active=\"0\" createDate=\"\" " +
                "order=\"" + sequenceNo + "\" " +
                "smartFolderId=\"" + folderID + "\" "  +
                "smartFolderName=\"" + folderName + "\" " +
                "updateDate=\"" + currentdateTimeStr + "\"></emx:smartFolderDetail></emx:updateSmartFolders>";*/


        String request_str="<?xml version=\"1.0\"?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:q0=\"http://webservice.emx.ecomail.com/\"><soapenv:Body><q0:getEMXResponse q0:type=\"emxService:getEMXResponse\"><emxRequest xsi:type=\"emxService:emxRequest\"><requestXml>&lt;emx:ConsumerRequest xmlns:emx=\"http://www.eco-mail.com/ConsumerRequest\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.eco-mail.com/ConsumerRequest/ConsumerRequestResponse.xsd\"&gt;&lt;emx:updateSmartFolders " +
                "consumerId=\"" + consumerId + "\" " +
                "methodId=\"updateSmartFolders\"&gt;&lt;emx:smartFolderDetail active=\"0\" createDate=\"\" " +
                "order=\"" + sequenceNo + "\" " +
                "smartFolderId=\"" + folderID + "\" "  +
                "smartFolderName=\"" + folderName + "\" " +
                "updateDate=\"" + currentdateTimeStr + "\" " +
                "&gt;&lt;/emx:smartFolderDetail&gt;&lt;/emx:updateSmartFolders&gt; &gt; &lt;/emx:ConsumerRequest&gt;</requestXml></emxRequest></q0:getEMXResponse></soapenv:Body></soapenv:Envelope>";


        MyApplication.MyLogi("request_str", "" + request_str.toString());

        responseString = ServerCall.makeConnection(Constant.BASEURL, request_str, _mContext);

        return parseeRemoveResponce(responseString);

    }

    public String parseeRemoveResponce(Map responsemap) {

        String return_str = "";
        String responseStr = "";
        if (responsemap.containsKey("code")) {
            Object value = responsemap.get("code");

            if (value.toString().equals("200")) {

                responseStr = responsemap.get("responce").toString();
                MyApplication.MyLogi("remove smart Folder resp", "" + responseStr.toString());
                XmlPullParserFactory pullParserFactory;
                try {

                    //https://www.studytutorial.in/android-xml-parsing-soap-response-parsing-tutorial-using-dom-parser

                    DocumentBuilder newDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                    Document parse = newDocumentBuilder.parse(new ByteArrayInputStream(responseStr.getBytes()));
                    String json_str = parse.getElementsByTagName("responseXml").item(0).getTextContent();

                    Object object = new JSONTokener(json_str).nextValue();
                    MyApplication.MyLogi("remove smart Folder resp", "" + object.toString());
                    if (object instanceof JSONObject) {
                        JSONObject jsonObject = new JSONObject(json_str);

                        if (jsonObject.has("con:updateSmartFoldersResponse")) {
                            JSONObject resPonceJson = jsonObject.getJSONObject("con:updateSmartFoldersResponse");
                            String messageType = resPonceJson.getString("messageType");
                            if (messageType.equals("Success")) {
                                return_str = Constant.SUCCESS_MSG;
                            }
                        }

                        else if(jsonObject.has("Failure")) {
                            return_str="Failure";
                        }

                        else if(jsonObject.has("con:errorResponse")){
                            JSONObject jsonObject1=jsonObject.getJSONObject("con:errorResponse");

                            String err_msg=jsonObject1.getString("errorMessage");
                            return_str=err_msg;
                        }


                    }

                    else{

                        return_str="Server not responding";
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


    public String callEditFolderAPI(String smartFolderName, String consumerId, String queryJson,int order,String folderID,String createDate) {
        this.sequenceNoInt=sequenceNoInt;
        String sequenceNo = String.valueOf(sequenceNoInt);


        Date now = new Date();

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS ZZZ");
        String currentdateTimeStr = df.format(now);

        String request_str ="<?xml version=\"1.0\"?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:q0=\"http://webservice.emx.ecomail.com/\"><soapenv:Body><q0:getEMXResponse q0:type=\"emxService:getEMXResponse\"><emxRequest xsi:type=\"emxService:emxRequest\"><requestXml>&lt;emx:ConsumerRequest xmlns:emx=\"http://www.eco-mail.com/ConsumerRequest\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.eco-mail.com/ConsumerRequest/ConsumerRequestResponse.xsd\"&gt;&lt;emx:updateSmartFolders " +
                "consumerId=\"" + consumerId + "\" " +
                "methodId=\"updateSmartFolders\"&gt;&lt;emx:smartFolderDetail active=\"1\" " +
                "createDate=\"" + createDate + "\" " +
                "order=\"" + order + "\" " +
                "smartFolderId=\"" + folderID + "\" " +
                "smartFolderName=\"" + smartFolderName + "\" " +
                "updateDate=\"" + currentdateTimeStr + "\" " +
                "&gt;"+queryJson +
                "&lt;/emx:smartFolderDetail&gt;&lt;/emx:updateSmartFolders&gt; &gt; &lt;/emx:ConsumerRequest&gt;</requestXml></emxRequest></q0:getEMXResponse></soapenv:Body></soapenv:Envelope>";


                MyApplication.MyLogi("request_str", "" + request_str.toString());

        responseString = ServerCall.makeConnection(Constant.BASEURL, request_str, _mContext);

        return parseEditResponce(responseString);

    }

    public String parseEditResponce(Map responsemap) {

        String return_str = "";
        String responseStr = "";
        if (responsemap.containsKey("code")) {
            Object value = responsemap.get("code");

            if (value.toString().equals("200")) {

                responseStr = responsemap.get("responce").toString();
                MyApplication.MyLogi("edit smart Folder resp", "" + responseStr.toString());
                XmlPullParserFactory pullParserFactory;
                try {

                    //https://www.studytutorial.in/android-xml-parsing-soap-response-parsing-tutorial-using-dom-parser

                    DocumentBuilder newDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                    Document parse = newDocumentBuilder.parse(new ByteArrayInputStream(responseStr.getBytes()));
                    String json_str = parse.getElementsByTagName("responseXml").item(0).getTextContent();

                    Object object = new JSONTokener(json_str).nextValue();
                    MyApplication.MyLogi("edit smart Folder resp", "" + object.toString());
                    if (object instanceof JSONObject) {
                        JSONObject jsonObject = new JSONObject(json_str);

                        if (jsonObject.has("con:updateSmartFoldersResponse")) {
                            JSONObject resPonceJson = jsonObject.getJSONObject("con:updateSmartFoldersResponse");
                            String messageContent = resPonceJson.getString("messageType");
                            if (messageContent.equals("Success")) {
                                return_str = Constant.SUCCESS_MSG;
                            }
                        }

                        else if(jsonObject.has("Failure")) {
                            return_str="Failure";
                        }

                        else if(jsonObject.has("con:errorResponse")){
                            JSONObject jsonObject1=jsonObject.getJSONObject("con:errorResponse");

                            String err_msg=jsonObject1.getString("errorMessage");
                            return_str=err_msg;
                        }


                    }

                    else{

                        return_str="Server not responding";
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
