package com.sixd.ecomail.APIManager;

import android.content.Context;

import com.sixd.ecomail.Utility.Constant;
import com.sixd.ecomail.Utility.MyApplication;
import com.sixd.ecomail.Utility.ServerCall;
import com.sixd.ecomail.model.Folder;

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
 * Created by Rahul Gupta on 22-05-2018.
 */
public class AddNormalFolderManager {

    Context _mContext;
    Map responseString;
    int sequenceNoInt;

    public AddNormalFolderManager(Context context) {
        _mContext = context;

    }

    public String callAddFolderAPI(String folderName, String consumerId, int sequenceNoInt) {
        this.sequenceNoInt=sequenceNoInt;
        String sequenceNo = String.valueOf(sequenceNoInt);


        String request_str = "<?xml version=\"1.0\"?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:q0=\"http://webservice.emx.ecomail.com/\"><soapenv:Body><q0:getEMXResponse q0:type=\"emxService:getEMXResponse\"><emxRequest xsi:type=\"emxService:emxRequest\"><requestXml>&lt;emx:ConsumerRequest xmlns:emx=\"http://www.eco-mail.com/ConsumerRequest\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.eco-mail.com/ConsumerRequest/ConsumerRequestResponse.xsd\"&gt; &lt;emx:folderModifications " +
                "folderId=\" \" " +
                "newTagName=\"" + folderName + "\" " +
                "methodId=\"folderModifications\" " +
                "sequenceNo=\"" + sequenceNo + "\" " +
                "consumerId=\"" + consumerId + "\" " +
                "tagRequestStatus=\"CREATETAG\"/&gt; " +
                "&lt;/emx:ConsumerRequest&gt;</requestXml></emxRequest></q0:getEMXResponse></soapenv:Body></soapenv:Envelope>";


        MyApplication.MyLogi("request_str", "" + request_str.toString());

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

                        if (jsonObject.has("con:FolderModificationResponseList")) {
                            JSONObject resPonceJson = jsonObject.getJSONObject("con:FolderModificationResponseList");
                            String messageContent = resPonceJson.getString("messageContent");
                            if (messageContent.equals("Success")) {
                                JSONObject folderJson = resPonceJson.getJSONObject("con:FolderModificationDetail");

                                BoxStore boxStore = MyApplication.getInstance().getBoxStore();
                                Box<Folder> folderBox = boxStore.boxFor(Folder.class);
                                return_str = Constant.SUCCESS_MSG;


                                String folderId = folderJson.getString("folderId");
                                String folderName = folderJson.getString("folderName");

                                Folder folder = new Folder(folderId, folderName, sequenceNoInt, false);
                                folderBox.put(folder);


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


    public String callRemoveFolderAPI(String folderName, String consumerId, int sequenceNoInt,String folderID) {
        this.sequenceNoInt=sequenceNoInt;
        String sequenceNo = String.valueOf(sequenceNoInt);


        String request_str = "<?xml version=\"1.0\"?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:q0=\"http://webservice.emx.ecomail.com/\"><soapenv:Body><q0:getEMXResponse q0:type=\"emxService:getEMXResponse\"><emxRequest xsi:type=\"emxService:emxRequest\"><requestXml>&lt;emx:ConsumerRequest xmlns:emx=\"http://www.eco-mail.com/ConsumerRequest\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.eco-mail.com/ConsumerRequest/ConsumerRequestResponse.xsd\"&gt; &lt;emx:folderModifications " +
                "folderId=\"" + folderID + "\" "  +
                "newTagName=\"" + folderName + "\" " +
                "methodId=\"folderModifications\" " +
                "sequenceNo=\"" + sequenceNo + "\" " +
                "consumerId=\"" + consumerId + "\" " +
                "tagRequestStatus=\"REMOVETAG\"/&gt; " +
                "&lt;/emx:ConsumerRequest&gt;</requestXml></emxRequest></q0:getEMXResponse></soapenv:Body></soapenv:Envelope>";


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
                MyApplication.MyLogi("remove Folder resp", "" + responseStr.toString());
                XmlPullParserFactory pullParserFactory;
                try {

                    //https://www.studytutorial.in/android-xml-parsing-soap-response-parsing-tutorial-using-dom-parser

                    DocumentBuilder newDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                    Document parse = newDocumentBuilder.parse(new ByteArrayInputStream(responseStr.getBytes()));
                    String json_str = parse.getElementsByTagName("responseXml").item(0).getTextContent();

                    Object object = new JSONTokener(json_str).nextValue();
                    MyApplication.MyLogi("remove Folder resp", "" + object.toString());
                    if (object instanceof JSONObject) {
                        JSONObject jsonObject = new JSONObject(json_str);

                        if (jsonObject.has("con:FolderModificationResponseList")) {
                            JSONObject resPonceJson = jsonObject.getJSONObject("con:FolderModificationResponseList");
                            String messageContent = resPonceJson.getString("messageContent");
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


    public String callEditFolderAPI(String folderName, String consumerId, int sequenceNoInt,String folderID) {
        this.sequenceNoInt=sequenceNoInt;
        String sequenceNo = String.valueOf(sequenceNoInt);


        String request_str = "<?xml version=\"1.0\"?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:q0=\"http://webservice.emx.ecomail.com/\"><soapenv:Body><q0:getEMXResponse q0:type=\"emxService:getEMXResponse\"><emxRequest xsi:type=\"emxService:emxRequest\"><requestXml>&lt;emx:ConsumerRequest xmlns:emx=\"http://www.eco-mail.com/ConsumerRequest\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.eco-mail.com/ConsumerRequest/ConsumerRequestResponse.xsd\"&gt; &lt;emx:folderModifications " +
                "folderId=\"" + folderID + "\" "  +
                "newTagName=\"" + folderName + "\" " +
                "methodId=\"folderModifications\" " +
                "sequenceNo=\"" + sequenceNo + "\" " +
                "consumerId=\"" + consumerId + "\" " +
                "tagRequestStatus=\"RENAMETAG\"/&gt; " +
                "&lt;/emx:ConsumerRequest&gt;</requestXml></emxRequest></q0:getEMXResponse></soapenv:Body></soapenv:Envelope>";


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

                        if (jsonObject.has("con:FolderModificationResponseList")) {
                            JSONObject resPonceJson = jsonObject.getJSONObject("con:FolderModificationResponseList");
                            String messageContent = resPonceJson.getString("messageContent");
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
