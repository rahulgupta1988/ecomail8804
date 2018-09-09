package com.sixd.ecomail.APIManager;

import android.content.Context;

import com.sixd.ecomail.Utility.Constant;
import com.sixd.ecomail.Utility.MyApplication;
import com.sixd.ecomail.Utility.ServerCall;
import com.sixd.ecomail.model.Folder;

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
 * Created by Rahul Gupta on 27-04-2018.
 */

public class FolderAPIManager {

    Context _mContext;
    Map responseString;

    public FolderAPIManager(Context context) {
        _mContext = context;

    }

    public String callFolderAPI(String consumerId) {

        String request_str = "<?xml version=\"1.0\"?>\n" +
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:q0=\"http://webservice.emx.ecomail.com/\">\n" +
                "  <soapenv:Body>\n" +
                "    <q0:getEMXResponse q0:type=\"emxService:getEMXResponse\">\n" +
                "      <emxRequest xsi:type=\"emxService:emxRequest\">\n" +
                "        <requestXml>&lt;emx:ConsumerRequest xmlns:emx=\"http://www.eco-mail.com/ConsumerRequest\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.eco-mail.com/ConsumerRequest/ConsumerRequestResponse.xsd\"&gt; &lt;emx:getAllFolders  methodId=\"getAllFolders\" consumerId=\"" + consumerId + "\"/&gt; &lt;/emx:ConsumerRequest&gt;</requestXml>\n" +
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
                    MyApplication.MyLogi("Folder resp", "" + object.toString());
                    if (object instanceof JSONObject) {
                        JSONObject jsonObject = new JSONObject(json_str);

                        JSONObject jsonObjectResponses = jsonObject.getJSONObject("con:AllFolderResponses");
                        if (jsonObjectResponses.has("consumerId")) {
                            BoxStore boxStore = MyApplication.getInstance().getBoxStore();
                            Box<Folder> folderBox = boxStore.boxFor(Folder.class);
                            folderBox.removeAll();
                            return_str = Constant.SUCCESS_MSG;
                            String consumerId = jsonObjectResponses.getString("consumerId");

                            JSONArray allFolderArray = jsonObjectResponses.getJSONArray("con:getAllFolderResponse");

                            MyApplication.MyLogi("Folder resp size", "" + allFolderArray.length());
                            for (int i = 0; i < allFolderArray.length(); i++) {
                                JSONObject folderJson = allFolderArray.getJSONObject(i);
                                String folderId = folderJson.getString("folderId");
                                String folderName = folderJson.getString("folderName");
                                int sequenceNo = folderJson.getInt("sequenceNo");

                                Folder folder = new Folder(folderId, folderName, sequenceNo, false);
                                folderBox.put(folder);
                            }


                        } else if (jsonObject.has("con:errorResponse")) {
                            JSONObject jsonObjectError = jsonObject.getJSONObject("con:errorResponse");

                            String err_msg = jsonObjectError.getString("errorMessage");
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
