package com.sixd.ecomail.APIManager;

import android.content.Context;

import com.sixd.ecomail.Utility.Constant;
import com.sixd.ecomail.Utility.MyApplication;
import com.sixd.ecomail.Utility.ServerCall;
import com.sixd.ecomail.model.Actions;
import com.sixd.ecomail.model.DocumentGroup;
import com.sixd.ecomail.model.Producer;
import com.sixd.ecomail.model.Subscription;
import com.sixd.ecomail.model.SubscriptionRecipient;

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

public class SubscriptionsAPIManager {
    Context _mContext;
    Map responseString;

    public SubscriptionsAPIManager(Context context) {
        _mContext = context;

    }

    public String callSubscriptionsAPI(String consumerId) {

        String request_str = "<?xml version=\"1.0\"?>\n" +
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:q0=\"http://webservice.emx.ecomail.com/\">\n" +
                "  <soapenv:Body>\n" +
                "    <q0:getEMXResponse q0:type=\"emxService:getEMXResponse\">\n" +
                "      <emxRequest xsi:type=\"emxService:emxRequest\">\n" +
                "        <requestXml>&lt;emx:ConsumerRequest xmlns:emx=\"http://www.eco-mail.com/ConsumerRequest\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.eco-mail.com/ConsumerRequest/ConsumerRequestResponse.xsd\"&gt; &lt;emx:getSubscriptions consumerId=\"" + consumerId + "\" methodId=\"getSubscriptions\" subscriptionFilter=\"AllProducers\" producerId=\"(null)\"/&gt; &lt;/emx:ConsumerRequest&gt;</requestXml>\n" +
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
                    MyApplication.MyLogi("Subscriptions resp", "" + object.toString());
                    if (object instanceof JSONObject) {
                        JSONObject jsonObject = new JSONObject(json_str);
                        if (jsonObject.has("con:getsubscriptionsResponses")) {
                            return_str = Constant.SUCCESS_MSG;
                            BoxStore boxStore = MyApplication.getInstance().getBoxStore();
                            Box<Producer> producerBox = boxStore.boxFor(Producer.class);
                            producerBox.removeAll();

                            Box<DocumentGroup> documentGroupBox = boxStore.boxFor(DocumentGroup.class);
                            documentGroupBox.removeAll();


                            Box<Actions> actionBox = boxStore.boxFor(Actions.class);
                            actionBox.removeAll();

                            Box<Subscription> subscriptionBox = boxStore.boxFor(Subscription.class);
                            subscriptionBox.removeAll();

                            Box<SubscriptionRecipient> subscriptionRecipientBox = boxStore.boxFor(SubscriptionRecipient.class);
                            subscriptionRecipientBox.removeAll();

                            JSONObject jsonObjectResponses = jsonObject.getJSONObject("con:getsubscriptionsResponses");
                            JSONObject producersJson = jsonObjectResponses.getJSONObject("con:producers");
                            JSONArray producerArray = producersJson.getJSONArray("con:Producer");



                            // producer
                            for (int i = 0; i < producerArray.length(); i++) {
                                JSONObject itemJson = producerArray.getJSONObject(i);
                                String producerId = itemJson.getString("producerId");
                                String producerName = itemJson.getString("producerName");
                                String producerLogoLocation = itemJson.getString("producerLogoLocation");


                                Producer producer = new Producer(false, producerId, producerName, producerLogoLocation,"");
                                producerBox.put(producer);



                                // documentGroup

                                JSONObject documentGroupsJson = itemJson.getJSONObject("con:DocumentGroups");
                                Object jsonObjectGP = documentGroupsJson.get("con:documentGroup");

                                if (jsonObjectGP instanceof JSONObject) {

                                    JSONObject docGPJson = documentGroupsJson.getJSONObject("con:documentGroup");
                                    String documentGroupId = docGPJson.getString("documentGroupId");
                                    String documentGroupName = docGPJson.getString("documentGroupName");
                                    String gracePeriod = docGPJson.getString("gracePeriod");
                                    String relationship = docGPJson.getString("relationship");
                                    String documentGroupLogoLocation = docGPJson.getString("documentGroupLogoLocation");

                                    String paperlessStatus = "";
                                    if (docGPJson.has("paperlessStatus"))
                                        paperlessStatus = docGPJson.getString("paperlessStatus");

                                    DocumentGroup documentGroup = new DocumentGroup(documentGroupId, documentGroupName, gracePeriod, relationship, documentGroupLogoLocation, paperlessStatus);



                                    //  Documents
                                    JSONObject documentJson = docGPJson.getJSONObject("con:Documents");
                                    Object documentObj = documentJson.get("con:document");
                                    if (documentObj instanceof JSONObject) {
                                        JSONObject docJson = documentJson.getJSONObject("con:document");
                                        String documentId = docJson.getString("documentId");
                                        String documentName = docJson.getString("documentName");
                                        String subscriptionId = docJson.getString("subscriptionId");
                                        String subscriptionType = docJson.getString("subscriptionType");
                                        String ownerConsumerId = docJson.getString("ownerConsumerId");
                                        String subscriptionCreateDate = docJson.getString("subscriptionCreateDate");
                                        String block = "";
                                        if (docJson.has("block"))
                                            block = docJson.getString("block");
                                        String mandatory = docJson.getString("mandatory");

                                        String subscriptionStartDate = "";
                                        if (docJson.has("subscriptionStartDate"))
                                            subscriptionStartDate = docJson.getString("subscriptionStartDate");


                                        // create subscription

                                        Subscription subscription_temp=new APIQueries().getSubscriptionByID(subscriptionId);
                                        if(subscription_temp==null) {
                                            Subscription subscription = new Subscription(block, documentGroupId, documentId, documentName, mandatory, ownerConsumerId, subscriptionId, subscriptionStartDate, subscriptionType);
                                            subscription.documentGroupToOne.setTarget(documentGroup);
                                            subscriptionBox.put(subscription);

                                            documentGroup.subscriptionToMany.add(subscription);
                                            documentGroup.producerToOne.setTarget(producer);
                                            documentGroupBox.put(documentGroup);
                                        }

                                        else{
                                            documentGroup.producerToOne.setTarget(producer);
                                            documentGroupBox.put(documentGroup);
                                        }

                                        //create subscriptionRecipient
                                        if (!docJson.isNull("con:recipients")) {

                                            if (docJson.get("con:recipients") instanceof JSONObject) {
                                                JSONObject subsresp = docJson.getJSONObject("con:recipients");
                                                String recipientConsumerId = "";
                                                String recipientType = "";
                                                String recipientCreateDate = "";
                                                String actionAllowed = "";
                                                String actionId = "";

                                                if (subsresp.has("recipientConsumerId"))
                                                    recipientConsumerId = subsresp.getString("recipientConsumerId");
                                                if (subsresp.has("recipientType"))
                                                    recipientType = subsresp.getString("recipientType");
                                                if (subsresp.has("recipientCreateDate"))
                                                    recipientCreateDate = subsresp.getString("recipientCreateDate");
                                                if (subsresp.has("actionAllowed"))
                                                    actionAllowed = subsresp.getString("actionAllowed");
                                                if (subsresp.has("actionId"))
                                                    actionId = subsresp.getString("actionId");

                                                SubscriptionRecipient subscriptionRecipient = new SubscriptionRecipient(actionAllowed, actionId, recipientConsumerId, recipientType, subscriptionId);
                                                subscriptionRecipientBox.put(subscriptionRecipient);

                                            } else {
                                                JSONArray subsrespArray = docJson.getJSONArray("con:recipients");
                                                for (int m = 0; m < subsrespArray.length(); m++) {
                                                    JSONObject subsresp = subsrespArray.getJSONObject(m);
                                                    String recipientConsumerId = "";
                                                    String recipientType = "";
                                                    String recipientCreateDate = "";
                                                    String actionAllowed = "";
                                                    String actionId = "";

                                                    if (subsresp.has("recipientConsumerId"))
                                                        recipientConsumerId = subsresp.getString("recipientConsumerId");
                                                    if (subsresp.has("recipientType"))
                                                        recipientType = subsresp.getString("recipientType");
                                                    if (subsresp.has("recipientCreateDate"))
                                                        recipientCreateDate = subsresp.getString("recipientCreateDate");
                                                    if (subsresp.has("actionAllowed"))
                                                        actionAllowed = subsresp.getString("actionAllowed");
                                                    if (subsresp.has("actionId"))
                                                        actionId = subsresp.getString("actionId");

                                                    SubscriptionRecipient subscriptionRecipient = new SubscriptionRecipient(actionAllowed, actionId, recipientConsumerId, recipientType, subscriptionId);
                                                    subscriptionRecipientBox.put(subscriptionRecipient);

                                                }
                                            }


                                        }


                                        //Action
                                        if (!docJson.isNull("con:actions")) {
                                            JSONObject actionsJson = docJson.getJSONObject("con:actions");
                                            Object actionObject = actionsJson.get("con:action");
                                            if (actionObject instanceof JSONObject) {
                                                JSONObject actionJson = actionsJson.getJSONObject("con:action");
                                                String actionId = actionJson.getString("actionId");
                                                String actionDescription = actionJson.getString("actionDescription");
                                                Actions actions = new Actions(actionId, actionDescription);
                                                actionBox.put(actions);
                                            } else {
                                                JSONArray actionArray = actionsJson.getJSONArray("con:action");
                                                for (int l = 0; l < actionArray.length(); l++) {
                                                    JSONObject actionJson = actionArray.getJSONObject(l);
                                                    String actionId = actionJson.getString("actionId");
                                                    String actionDescription = actionJson.getString("actionDescription");
                                                    Actions actions = new Actions(actionId, actionDescription);
                                                    actionBox.put(actions);

                                                }
                                            }

                                        }

                                    }


                                    else {
                                        JSONArray documentArray = documentJson.getJSONArray("con:document");
                                        for (int k = 0; k < documentArray.length(); k++) {

                                            JSONObject docJson = documentArray.getJSONObject(k);
                                            String documentId = docJson.getString("documentId");
                                            String documentName = docJson.getString("documentName");
                                            String subscriptionId = docJson.getString("subscriptionId");
                                            String subscriptionType = docJson.getString("subscriptionType");
                                            String ownerConsumerId = docJson.getString("ownerConsumerId");
                                            String subscriptionCreateDate = docJson.getString("subscriptionCreateDate");
                                            String block = "";
                                            if (docJson.has("block"))
                                                block = docJson.getString("block");
                                            String mandatory = docJson.getString("mandatory");

                                            String subscriptionStartDate = "";
                                            if (docJson.has("subscriptionStartDate"))
                                                subscriptionStartDate = docJson.getString("subscriptionStartDate");

                                            Subscription subscription_temp=new APIQueries().getSubscriptionByID(subscriptionId);
                                            if(subscription_temp==null) {
                                                // create subscription
                                                Subscription subscription = new Subscription(block, documentGroupId, documentId, documentName, mandatory, ownerConsumerId, subscriptionId, subscriptionStartDate, subscriptionType);

                                                subscription.documentGroupToOne.setTarget(documentGroup);
                                                subscriptionBox.put(subscription);


                                                documentGroup.subscriptionToMany.add(subscription);
                                                documentGroup.producerToOne.setTarget(producer);
                                                documentGroupBox.put(documentGroup);
                                            }
                                            else{
                                                documentGroup.producerToOne.setTarget(producer);
                                                documentGroupBox.put(documentGroup);
                                            }

                                            //create subscriptionRecipient
                                            if (!docJson.isNull("con:recipients")) {
                                                if (docJson.get("con:recipients") instanceof JSONObject) {
                                                    JSONObject subsresp = docJson.getJSONObject("con:recipients");

                                                    String recipientConsumerId = "";
                                                    String recipientType = "";
                                                    String recipientCreateDate = "";
                                                    String actionAllowed = "";
                                                    String actionId = "";

                                                    if (subsresp.has("recipientConsumerId"))
                                                        recipientConsumerId = subsresp.getString("recipientConsumerId");
                                                    if (subsresp.has("recipientType"))
                                                        recipientType = subsresp.getString("recipientType");
                                                    if (subsresp.has("recipientCreateDate"))
                                                        recipientCreateDate = subsresp.getString("recipientCreateDate");
                                                    if (subsresp.has("actionAllowed"))
                                                        actionAllowed = subsresp.getString("actionAllowed");
                                                    if (subsresp.has("actionId"))
                                                        actionId = subsresp.getString("actionId");

                                                    SubscriptionRecipient subscriptionRecipient = new SubscriptionRecipient(actionAllowed, actionId, recipientConsumerId, recipientType, subscriptionId);
                                                    subscriptionRecipientBox.put(subscriptionRecipient);

                                                } else {
                                                    JSONArray subsrespArray = docJson.getJSONArray("con:recipients");
                                                    for (int m = 0; m < subsrespArray.length(); m++) {
                                                        JSONObject subsresp = subsrespArray.getJSONObject(m);
                                                        String recipientConsumerId = "";
                                                        String recipientType = "";
                                                        String recipientCreateDate = "";
                                                        String actionAllowed = "";
                                                        String actionId = "";

                                                        if (subsresp.has("recipientConsumerId"))
                                                            recipientConsumerId = subsresp.getString("recipientConsumerId");
                                                        if (subsresp.has("recipientType"))
                                                            recipientType = subsresp.getString("recipientType");
                                                        if (subsresp.has("recipientCreateDate"))
                                                            recipientCreateDate = subsresp.getString("recipientCreateDate");
                                                        if (subsresp.has("actionAllowed"))
                                                            actionAllowed = subsresp.getString("actionAllowed");
                                                        if (subsresp.has("actionId"))
                                                            actionId = subsresp.getString("actionId");

                                                        SubscriptionRecipient subscriptionRecipient = new SubscriptionRecipient(actionAllowed, actionId, recipientConsumerId, recipientType, subscriptionId);
                                                        subscriptionRecipientBox.put(subscriptionRecipient);

                                                    }
                                                }


                                            }


                                            //Action
                                            if (!docJson.isNull("con:actions")) {
                                                JSONObject actionsJson = docJson.getJSONObject("con:actions");
                                                Object actionObject = actionsJson.get("con:action");
                                                if (actionObject instanceof JSONObject) {
                                                    JSONObject actionJson = actionsJson.getJSONObject("con:action");
                                                    String actionId = actionJson.getString("actionId");
                                                    String actionDescription = actionJson.getString("actionDescription");
                                                    Actions actions = new Actions(actionId, actionDescription);
                                                    actionBox.put(actions);
                                                } else {
                                                    JSONArray actionArray = actionsJson.getJSONArray("con:action");
                                                    for (int l = 0; l < actionArray.length(); l++) {
                                                        JSONObject actionJson = actionArray.getJSONObject(l);
                                                        String actionId = actionJson.getString("actionId");
                                                        String actionDescription = actionJson.getString("actionDescription");
                                                        Actions actions = new Actions(actionId, actionDescription);
                                                        actionBox.put(actions);

                                                    }
                                                }
                                            }


                                        }
                                    }


                                }




                                else {
                                    JSONArray documentGroupsArray = documentGroupsJson.getJSONArray("con:documentGroup");
                                    for (int j = 0; j < documentGroupsArray.length(); j++) {

                                        JSONObject docGPJson = documentGroupsArray.getJSONObject(j);
                                        String documentGroupId = docGPJson.getString("documentGroupId");
                                        String documentGroupName = docGPJson.getString("documentGroupName");
                                        String gracePeriod = docGPJson.getString("gracePeriod");
                                        String relationship = docGPJson.getString("relationship");
                                        String documentGroupLogoLocation = docGPJson.getString("documentGroupLogoLocation");
                                        String paperlessStatus = "";
                                        if (docGPJson.has("paperlessStatus"))
                                            paperlessStatus = docGPJson.getString("paperlessStatus");

                                        DocumentGroup documentGroup = new DocumentGroup(documentGroupId, documentGroupName, gracePeriod, relationship, documentGroupLogoLocation, paperlessStatus);
                                       // documentGroupBox.put(documentGroup);


                                        //  Documents
                                        JSONObject documentJson = docGPJson.getJSONObject("con:Documents");
                                        Object documentObj = documentJson.get("con:document");
                                        if (documentObj instanceof JSONObject) {
                                            JSONObject docJson = documentJson.getJSONObject("con:document");
                                            String documentId = docJson.getString("documentId");
                                            String documentName = docJson.getString("documentName");
                                            String subscriptionId = docJson.getString("subscriptionId");
                                            String subscriptionType = docJson.getString("subscriptionType");
                                            String ownerConsumerId = docJson.getString("ownerConsumerId");
                                            String subscriptionCreateDate = docJson.getString("subscriptionCreateDate");
                                            String block = "";
                                            if (docJson.has("block"))
                                                block = docJson.getString("block");
                                            String mandatory = docJson.getString("mandatory");

                                            String subscriptionStartDate = "";
                                            if (docJson.has("subscriptionStartDate"))
                                                subscriptionStartDate = docJson.getString("subscriptionStartDate");

                                            Subscription subscription_temp=new APIQueries().getSubscriptionByID(subscriptionId);
                                            if(subscription_temp==null) {
                                                // create subscription
                                                Subscription subscription = new Subscription(block, documentGroupId, documentId, documentName, mandatory, ownerConsumerId, subscriptionId, subscriptionStartDate, subscriptionType);

                                                subscription.documentGroupToOne.setTarget(documentGroup);
                                                subscriptionBox.put(subscription);


                                                documentGroup.subscriptionToMany.add(subscription);
                                                documentGroup.producerToOne.setTarget(producer);
                                                documentGroupBox.put(documentGroup);
                                            }

                                            else{
                                                documentGroup.producerToOne.setTarget(producer);
                                                documentGroupBox.put(documentGroup);
                                            }

                                            //Action
                                            if (!docJson.isNull("con:actions")) {
                                                JSONObject actionsJson = docJson.getJSONObject("con:actions");
                                                Object actionObject = actionsJson.get("con:action");
                                                if (actionObject instanceof JSONObject) {
                                                    JSONObject actionJson = actionsJson.getJSONObject("con:action");
                                                    String actionId = actionJson.getString("actionId");
                                                    String actionDescription = actionJson.getString("actionDescription");
                                                    Actions actions = new Actions(actionId, actionDescription);
                                                    actionBox.put(actions);
                                                } else {
                                                    JSONArray actionArray = actionsJson.getJSONArray("con:action");
                                                    for (int l = 0; l < actionArray.length(); l++) {
                                                        JSONObject actionJson = actionArray.getJSONObject(l);
                                                        String actionId = actionJson.getString("actionId");
                                                        String actionDescription = actionJson.getString("actionDescription");
                                                        Actions actions = new Actions(actionId, actionDescription);
                                                        actionBox.put(actions);

                                                    }
                                                }

                                            }

                                        } else {
                                            JSONArray documentArray = documentJson.getJSONArray("con:document");
                                            for (int k = 0; k < documentArray.length(); k++) {

                                                JSONObject docJson = documentArray.getJSONObject(k);
                                                String documentId = docJson.getString("documentId");
                                                String documentName = docJson.getString("documentName");
                                                String subscriptionId = docJson.getString("subscriptionId");
                                                String subscriptionType = docJson.getString("subscriptionType");
                                                String ownerConsumerId = docJson.getString("ownerConsumerId");
                                                String subscriptionCreateDate = docJson.getString("subscriptionCreateDate");
                                                String block = "";
                                                if (docJson.has("block"))
                                                    block = docJson.getString("block");
                                                String mandatory = docJson.getString("mandatory");

                                                String subscriptionStartDate = "";
                                                if (docJson.has("subscriptionStartDate"))
                                                    subscriptionStartDate = docJson.getString("subscriptionStartDate");

                                                Subscription subscription_temp=new APIQueries().getSubscriptionByID(subscriptionId);
                                                if(subscription_temp==null) {
                                                    // create subscription
                                                    Subscription subscription = new Subscription(block, documentGroupId, documentId, documentName, mandatory, ownerConsumerId, subscriptionId, subscriptionStartDate, subscriptionType);

                                                    subscription.documentGroupToOne.setTarget(documentGroup);
                                                    subscriptionBox.put(subscription);


                                                    documentGroup.subscriptionToMany.add(subscription);
                                                    documentGroup.producerToOne.setTarget(producer);
                                                    documentGroupBox.put(documentGroup);
                                                }

                                                else{
                                                    documentGroup.producerToOne.setTarget(producer);
                                                    documentGroupBox.put(documentGroup);
                                                }

                                                //Action
                                                if (!docJson.isNull("con:actions")) {
                                                    JSONObject actionsJson = docJson.getJSONObject("con:actions");
                                                    Object actionObject = actionsJson.get("con:action");
                                                    if (actionObject instanceof JSONObject) {
                                                        JSONObject actionJson = actionsJson.getJSONObject("con:action");
                                                        String actionId = actionJson.getString("actionId");
                                                        String actionDescription = actionJson.getString("actionDescription");
                                                        Actions actions = new Actions(actionId, actionDescription);
                                                        actionBox.put(actions);
                                                    } else {
                                                        JSONArray actionArray = actionsJson.getJSONArray("con:action");
                                                        for (int l = 0; l < actionArray.length(); l++) {
                                                            JSONObject actionJson = actionArray.getJSONObject(l);
                                                            String actionId = actionJson.getString("actionId");
                                                            String actionDescription = actionJson.getString("actionDescription");
                                                            Actions actions = new Actions(actionId, actionDescription);
                                                            actionBox.put(actions);

                                                        }
                                                    }
                                                }


                                            }
                                        }


                                    }
                                }

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
