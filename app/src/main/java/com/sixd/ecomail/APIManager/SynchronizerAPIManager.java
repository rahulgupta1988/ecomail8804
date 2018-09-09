package com.sixd.ecomail.APIManager;

import android.content.Context;
import android.util.Log;

import com.sixd.ecomail.Utility.Constant;
import com.sixd.ecomail.Utility.MyApplication;
import com.sixd.ecomail.Utility.ServerCall;
import com.sixd.ecomail.model.DocTypes;
import com.sixd.ecomail.model.Document_Actions;
import com.sixd.ecomail.model.Document_Actions_Display;
import com.sixd.ecomail.model.Document_Actions_Dispute;
import com.sixd.ecomail.model.Document_Actions_Payment;
import com.sixd.ecomail.model.Document_Actions_ThirdPartyPayment;
import com.sixd.ecomail.model.Folder;
import com.sixd.ecomail.model.Payment;

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
 * Created by Rahul Gupta on 02-05-2018.
 */

public class SynchronizerAPIManager {

    Context _mContext;
    Map responseString;

    public SynchronizerAPIManager(Context context) {
        _mContext = context;

    }

    public String callSynchronizerAPI(String consumerId) {

        Date now = new Date();

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS ZZZ");
        String currentdateTimeStr = df.format(now);

        MyApplication.MyLogi("currentdateTimeStr343",""+currentdateTimeStr);


        String request_str = "<?xml version=\"1.0\"?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" " +
                "xmlns:q0=\"http://webservice.emx.ecomail.com/\"><soapenv:Body><q0:getEMXResponse q0:type=\"emxService:getEMXResponse\">" +
                "<emxRequest xsi:type=\"emxService:emxRequest\"><requestXml>&lt;emx:ConsumerRequest xmlns:emx=\"http://www.eco-mail.com/ConsumerRequest\" " +
                "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.eco-mail.com/ConsumerRequest/ConsumerRequestResponse.xsd\"&gt; &lt;emx:synchronizer " +
                "fromDate=\"2012-05-03 06:59:10.813 +0000\" " +
                "toDate=\"" + currentdateTimeStr + "\" " +
                "methodId=\"synchronizer\" synchronizeState=\"ALL\" synchronizeType=\"ALL\" consumerId=\"" + consumerId + "\"/&gt; &lt;/emx:ConsumerRequest&gt;" +
                "</requestXml></emxRequest></q0:getEMXResponse></soapenv:Body></soapenv:Envelope>\n";


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
                    MyApplication.MyLogi("synchronize resp", "" + object.toString());
                    if (object instanceof JSONObject) {
                        JSONObject jsonObject = new JSONObject(json_str);
                        if (jsonObject.has("con:synchronizerResponse")) {
                            return_str = Constant.SUCCESS_MSG;
                            BoxStore boxStore = MyApplication.getInstance().getBoxStore();
                            Box<com.sixd.ecomail.model.Document> documentBox = boxStore.boxFor(com.sixd.ecomail.model.Document.class);
                            documentBox.removeAll();

                            Box<DocTypes> docTypesBox = boxStore.boxFor(DocTypes.class);
                            docTypesBox.removeAll();

                            JSONObject jsonObjectResponses = jsonObject.getJSONObject("con:synchronizerResponse");


                            Box<Payment> paymentBox = boxStore.boxFor(Payment.class);
                            paymentBox.removeAll();

                            JSONObject paymentsSynchronizerJsonres = jsonObjectResponses.getJSONObject("con:paymentsSynchronizer");

                            JSONArray paymentArray=paymentsSynchronizerJsonres.getJSONArray("con:payment");





                            for(int p=0;p<paymentArray.length();p++){

                                JSONObject payJson=paymentArray.getJSONObject(p);
                                String consumerId=payJson.getString("consumerId");
                                String transmissionId=payJson.getString("transmissionId");
                                String paymentConfirmationNo=payJson.getString("paymentConfirmationNo");
                                String paymentAmount=payJson.getString("paymentAmount");
                                String bankId=payJson.getString("bankId");
                                String bankName=payJson.getString("bankName");
                                String bankAccount=payJson.getString("bankAccount");
                                String cardNumber=payJson.getString("cardNumber");
                                String scheduleDate=payJson.getString("scheduleDate");
                                String paymentTypeId=payJson.getString("paymentTypeId");
                                String paymentStateId=payJson.getString("paymentStateId");
                                String createDate=payJson.getString("createDate");
                                String paymentTrackerId=payJson.getString("paymentTrackerId");

                                Log.i("paymentStateId 678",""+paymentStateId);


                                Payment payment=new Payment( consumerId,  transmissionId,  paymentConfirmationNo,  paymentAmount,  bankId,  bankName,
                                         bankAccount,  cardNumber,  scheduleDate,  paymentTypeId,  paymentStateId,  createDate,  paymentTrackerId);

                                paymentBox.put(payment);
                            }




                            JSONObject contentJsonres = jsonObjectResponses.getJSONObject("con:contentSynchronizer");
                            JSONArray contentArray = contentJsonres.getJSONArray("con:content");

                            for (int i = 0; i < contentArray.length(); i++) {
                                JSONObject contentJson = contentArray.getJSONObject(i);
                                String producerId = contentJson.getString("producerId");
                                String producerName = contentJson.getString("producerName");
                                String contentDocumentId = contentJson.getString("documentId");
                                String documentName = contentJson.getString("documentName");
                                String recipientConsumerId = contentJson.getString("recipientConsumerId");
                                String recipientName = contentJson.getString("recipientName");
                                String recipientShortName = contentJson.getString("recipientShortName");
                                String contentDocumentCategoryId = contentJson.getString("documentCategoryId");
                                String categoryDescription = contentJson.getString("categoryDescription");
                                String contentLocation = contentJson.getString("contentLocation");
                                String transmissionId = contentJson.getString("transmissionId");
                                String stateTypeId = contentJson.getString("stateTypeId");
                                String subscriptionId = contentJson.getString("subscriptionId");
                                String displayLocation = contentJson.getString("displayLocation");
                                String primaryConsumerId = contentJson.getString("primaryConsumerId");
                                String documentGroup = contentJson.getString("documentGroup");
                                String primaryConsumerName = contentJson.getString("primaryConsumerName");
                                String createDate = contentJson.getString("createDate");
                                String documentLogoLocation = contentJson.getString("documentLogoLocation");
                                String documentWebLocation = contentJson.getString("documentWebLocation");

                                JSONObject publicDataMetaJson = contentJson.getJSONObject("publicMetaData");
                                String keyPackage = publicDataMetaJson.getString("keyPackage");
                                String actionRequired = publicDataMetaJson.getString("actionRequired");
                                String actionDueDate = publicDataMetaJson.getString("actionDueDate");
                                String contentCreateDate = createDate;//publicDataMetaJson.getString("contentCreateDate");


                                JSONObject privateDataMetaJson = contentJson.getJSONObject("privateMetaData");
                                JSONObject producerDetailsJson = privateDataMetaJson.getJSONObject("producerDetails");
                                String consumerId = producerDetailsJson.getString("consumerId");




                                String subscriptionType = "";
                                String ownerConsumerId = "";
                                String subscriptionCreateDate = "";
                                String block = "";
                                String mandatory = "";
                                String subscriptionStartDate = "";
                                String contentUpdateDat = "";
                                String documentGroupName = "";
                                String envelopeImage = "";
                                String paperlessStatus = "";
                                String primaryAction = "";



                                DocTypes docTypes_temp=new APIQueries().getDocumentTypeByID(contentDocumentCategoryId);
                                if(docTypes_temp==null) {
                                    DocTypes docTypes = new DocTypes(contentDocumentCategoryId, categoryDescription, false, false);
                                    docTypesBox.put(docTypes);
                                }


                                if (privateDataMetaJson.has("documentDetails")) {



                                    if (privateDataMetaJson.get("documentDetails") instanceof JSONArray) {
                                        JSONArray documentDetailsArray = privateDataMetaJson.getJSONArray("documentDetails");



                                        if (documentDetailsArray.length() == 0) {



                                            com.sixd.ecomail.model.Document document = new com.sixd.ecomail.model.Document(contentDocumentId, documentName, subscriptionId,
                                                    subscriptionType, ownerConsumerId, subscriptionCreateDate, block, mandatory, subscriptionStartDate, categoryDescription,
                                                    contentCreateDate, contentLocation, contentUpdateDat, "", "", displayLocation,
                                                    contentDocumentCategoryId, "", documentGroup, documentGroupName, documentLogoLocation, documentWebLocation, envelopeImage,
                                                    paperlessStatus, "", "", "", primaryAction, primaryConsumerId, primaryConsumerName, producerId,
                                                    "", producerName, recipientConsumerId, recipientName, recipientShortName, stateTypeId, transmissionId);


                                            long id = documentBox.put(document);


                                            if(contentJson.has("con:folderId")){
                                                Object confolderIdJson = contentJson.get("con:folderId");


                                                Box<Folder> folderBox = boxStore.boxFor(Folder.class);

                                                if(confolderIdJson instanceof JSONArray) {
                                                    JSONArray foldresIDs = contentJson.getJSONArray("con:folderId");


                                                    for(int n=0;n<foldresIDs.length();n++){
                                                        String folderid = foldresIDs.getString(n);
                                                        Folder folder=new APIQueries().getFolderByID(folderid);
                                                        folder.documentToMany.add(document);
                                                        folderBox.put(folder);

                                                        document.folderToOne.setTarget(folder);
                                                        documentBox.put(document);
                                                    }
                                                }

                                                else{

                                                    String folderid = contentJson.getString("con:folderId");


                                                    Folder folder=new APIQueries().getFolderByID(folderid);
                                                    folder.documentToMany.add(document);
                                                    folderBox.put(folder);
                                                    document.folderToOne.setTarget(folder);
                                                    documentBox.put(document);


                                                }


                                            }







                                            continue;

                                        }


                                        for (int j = 0; j < documentDetailsArray.length(); j++) {

                                            JSONObject documentDetailsJson = documentDetailsArray.getJSONObject(j);
                                            String customerIdentifierType = documentDetailsJson.getString("customerIdentifierType");
                                            String customerIdentifier = documentDetailsJson.getString("customerIdentifier");
                                            String documentCategoryId = documentDetailsJson.getString("documentCategoryId");
                                            String documentDate = documentDetailsJson.getString("documentDate");
                                            String documentId = documentDetailsJson.getString("documentId");
                                            String periodDescription = documentDetailsJson.getString("periodDescription");
                                            String periodEnd = documentDetailsJson.getString("periodEnd");
                                            String periodStart = documentDetailsJson.getString("periodStart");
                                            String producerMessage = documentDetailsJson.getString("producerMessage");


                                            // document actios


                                            Document_Actions document_actions = new Document_Actions();

                                            JSONArray actionArray = documentDetailsJson.getJSONArray("actions");



                                            for (int k = 0; k < actionArray.length(); k++) {

                                                JSONObject jsonObject1 = actionArray.getJSONObject(k);
                                                if (jsonObject1.has("thirdPartyPayment")) {
                                                    JSONObject thirdPartyPaymentJson = jsonObject1.getJSONObject("thirdPartyPayment");

                                                    String actionDescription = thirdPartyPaymentJson.getString("actionDescription");
                                                    String beneficiaryAccount = thirdPartyPaymentJson.getString("beneficiaryAccount");
                                                    String beneficiaryName = thirdPartyPaymentJson.getString("beneficiaryName");
                                                    String minimumAmountDue = thirdPartyPaymentJson.getString("minimumAmountDue");
                                                    String actionPriority = thirdPartyPaymentJson.getString("actionPriority");
                                                    String routingNo = thirdPartyPaymentJson.getString("routingNo");
                                                    String thirdPartyPayment_actionDueDate = thirdPartyPaymentJson.getString("actionDueDate");
                                                    String actionId = thirdPartyPaymentJson.getString("actionId");
                                                    String amountDue = thirdPartyPaymentJson.getString("amountDue");
                                                    String referenceInfo = thirdPartyPaymentJson.getString("referenceInfo");
                                                    String message = thirdPartyPaymentJson.getString("message");
                                                    String link = thirdPartyPaymentJson.getString("link");
                                                    String expirationDate = thirdPartyPaymentJson.getString("expirationDate");

                                                    Document_Actions_ThirdPartyPayment document_actions_thirdPartyPayment = new Document_Actions_ThirdPartyPayment(actionDescription,
                                                            beneficiaryAccount, beneficiaryName, minimumAmountDue, actionPriority, routingNo,
                                                            actionDueDate, actionId, amountDue,
                                                            referenceInfo, message, link, expirationDate);

                                                    document_actions.document_actions_thirdPartyPaymentToOne.setTarget(document_actions_thirdPartyPayment);
                                                    // document_actions.setDocument_actions_thirdPartyPayment(document_actions_thirdPartyPayment);

                                                } else if (jsonObject1.has("display")) {
                                                    JSONObject displayJson = jsonObject1.getJSONObject("display");
                                                    String actionDescription = displayJson.getString("actionDescription");
                                                    String actionPriority = displayJson.getString("actionPriority");
                                                    String display_actionDueDate = displayJson.getString("display_actionDueDate");
                                                    String actionId = displayJson.getString("actionId");
                                                    String message = displayJson.getString("message");
                                                    String link = displayJson.getString("link");
                                                    String expirationDate = displayJson.getString("expirationDate");

                                                    Document_Actions_Display document_actions_display = new Document_Actions_Display(actionDescription, actionPriority, actionDueDate,
                                                            actionId, message, link, expirationDate);

                                                    document_actions.document_actions_displayToOne.setTarget(document_actions_display);
                                                    //  document_actions.setDocument_actions_display(document_actions_display);

                                                } else if (jsonObject1.has("dispute")) {
                                                    JSONObject disputeJson = jsonObject1.getJSONObject("dispute");
                                                    String actionDescription = disputeJson.getString("actionDescription");
                                                    String actionPriority = disputeJson.getString("actionDescription");
                                                    String dispute_actionDueDate = disputeJson.getString("actionDueDate");
                                                    String actionId = disputeJson.getString("actionId");
                                                    String message = disputeJson.getString("message");
                                                    String link = disputeJson.getString("link");
                                                    String expirationDate = disputeJson.getString("expirationDate");

                                                    Document_Actions_Dispute document_actions_dispute = new Document_Actions_Dispute(actionDescription, actionPriority, actionDueDate,
                                                            actionId, message, link, expirationDate);

                                                    document_actions.document_actions_disputeToOne.setTarget(document_actions_dispute);


                                                } else if (jsonObject1.has("payment")) {
                                                    JSONObject paymentJson = jsonObject1.getJSONObject("payment");
                                                    String actionDescription = paymentJson.getString("actionDescription");
                                                    String minimumAmountDue = paymentJson.getString("minimumAmountDue");
                                                    ;
                                                    String actionPriority = paymentJson.getString("actionPriority");
                                                    String autoPaid = paymentJson.getString("autoPaid");
                                                    String payment_actionDueDate = paymentJson.getString("actionDueDate");
                                                    String amountDue = paymentJson.getString("amountDue");
                                                    String actionId = paymentJson.getString("actionId");
                                                    primaryAction=actionId;
                                                    String message = paymentJson.getString("message");
                                                    String link = paymentJson.getString("link");
                                                    String expirationDate = paymentJson.getString("expirationDate");

                                                    Document_Actions_Payment document_actions_payment = new Document_Actions_Payment(actionDescription, minimumAmountDue, actionPriority,
                                                            autoPaid, actionDueDate, amountDue, actionId, message, link, expirationDate);

                                                    document_actions.document_actions_paymentToOne.setTarget(document_actions_payment);

                                                }

                                            }


                                            com.sixd.ecomail.model.Document document = new com.sixd.ecomail.model.Document(documentId, documentName, subscriptionId,
                                                    subscriptionType, ownerConsumerId, subscriptionCreateDate, block, mandatory, subscriptionStartDate, categoryDescription,
                                                    contentCreateDate, contentLocation, contentUpdateDat, customerIdentifier, customerIdentifierType, displayLocation,
                                                    documentCategoryId, documentDate, documentGroup, documentGroupName, documentLogoLocation, documentWebLocation, envelopeImage,
                                                    paperlessStatus, periodDescription, periodEnd, periodStart, primaryAction, primaryConsumerId, primaryConsumerName, producerId,
                                                    producerMessage, producerName, recipientConsumerId, recipientName, recipientShortName, stateTypeId, transmissionId);
                                            document.document_actionsToOne.setTarget(document_actions);
                                            documentBox.put(document);


                                            if(contentJson.has("con:folderId")){
                                                Object confolderIdJson = contentJson.get("con:folderId");


                                                Box<Folder> folderBox = boxStore.boxFor(Folder.class);

                                                if(confolderIdJson instanceof JSONArray) {
                                                    JSONArray foldresIDs = contentJson.getJSONArray("con:folderId");


                                                    for(int n=0;n<foldresIDs.length();n++){
                                                        String folderid = foldresIDs.getString(n);
                                                        Folder folder=new APIQueries().getFolderByID(folderid);
                                                        folder.documentToMany.add(document);
                                                        folderBox.put(folder);
                                                        document.folderToOne.setTarget(folder);
                                                        documentBox.put(document);

                                                    }
                                                }

                                                else{

                                                    String folderid = contentJson.getString("con:folderId");


                                                    Folder folder=new APIQueries().getFolderByID(folderid);
                                                    folder.documentToMany.add(document);
                                                    folderBox.put(folder);
                                                    document.folderToOne.setTarget(folder);
                                                    documentBox.put(document);


                                                }


                                            }



                                        }

                                    } else {
                                        JSONObject documentDetailsJson = privateDataMetaJson.getJSONObject("documentDetails");




                                        if (documentDetailsJson.length() == 0) {

                                            com.sixd.ecomail.model.Document document = new com.sixd.ecomail.model.Document(contentDocumentId, documentName, subscriptionId,
                                                    subscriptionType, ownerConsumerId, subscriptionCreateDate, block, mandatory, subscriptionStartDate, categoryDescription,
                                                    contentCreateDate, contentLocation, contentUpdateDat, "", "", displayLocation,
                                                    "", "", documentGroup, documentGroupName, documentLogoLocation, documentWebLocation, envelopeImage,
                                                    paperlessStatus, "", "", "", primaryAction, primaryConsumerId, primaryConsumerName, producerId,
                                                    "", producerName, recipientConsumerId, recipientName, recipientShortName, stateTypeId, transmissionId);

                                            documentBox.put(document);

                                            if(contentJson.has("con:folderId")){
                                                Object confolderIdJson = contentJson.get("con:folderId");


                                                Box<Folder> folderBox = boxStore.boxFor(Folder.class);

                                                if(confolderIdJson instanceof JSONArray) {
                                                    JSONArray foldresIDs = contentJson.getJSONArray("con:folderId");


                                                    for(int n=0;n<foldresIDs.length();n++){
                                                        String folderid = foldresIDs.getString(n);
                                                        Folder folder=new APIQueries().getFolderByID(folderid);
                                                        folder.documentToMany.add(document);
                                                        folderBox.put(folder);
                                                        document.folderToOne.setTarget(folder);
                                                        documentBox.put(document);
                                                    }
                                                }

                                                else{

                                                    String folderid = contentJson.getString("con:folderId");


                                                    Folder folder=new APIQueries().getFolderByID(folderid);
                                                    folder.documentToMany.add(document);
                                                    folderBox.put(folder);

                                                    document.folderToOne.setTarget(folder);
                                                    documentBox.put(document);


                                                }


                                            }



                                            long id = documentBox.put(document);

                                            continue;
                                        }


                                        String customerIdentifierType = documentDetailsJson.getString("customerIdentifierType");
                                        String customerIdentifier = documentDetailsJson.getString("customerIdentifier");
                                        String documentCategoryId = documentDetailsJson.getString("documentCategoryId");
                                        String documentDate = documentDetailsJson.getString("documentDate");
                                        String documentId = documentDetailsJson.getString("documentId");
                                        String periodDescription = documentDetailsJson.getString("periodDescription");
                                        String periodEnd = documentDetailsJson.getString("periodEnd");
                                        String periodStart = documentDetailsJson.getString("periodStart");
                                        String producerMessage = documentDetailsJson.getString("producerMessage");


                                        // document actios


                                        Document_Actions document_actions = new Document_Actions();

                                        Object action_Object = documentDetailsJson.get("actions");

                                        if (action_Object instanceof JSONArray) {
                                            JSONArray actionArray = documentDetailsJson.getJSONArray("actions");
                                            for (int k = 0; k < actionArray.length(); k++) {
                                                JSONObject jsonObject1 = actionArray.getJSONObject(k);
                                                if (jsonObject1.has("thirdPartyPayment")) {
                                                    JSONObject thirdPartyPaymentJson = jsonObject1.getJSONObject("thirdPartyPayment");

                                                    String actionDescription = thirdPartyPaymentJson.getString("actionDescription");
                                                    String beneficiaryAccount = thirdPartyPaymentJson.getString("beneficiaryAccount");
                                                    String beneficiaryName = thirdPartyPaymentJson.getString("beneficiaryName");
                                                    String minimumAmountDue = thirdPartyPaymentJson.getString("minimumAmountDue");
                                                    String actionPriority = thirdPartyPaymentJson.getString("actionPriority");
                                                    String routingNo = thirdPartyPaymentJson.getString("routingNo");
                                                    String thirdPartyPayment_actionDueDate = thirdPartyPaymentJson.getString("actionDueDate");
                                                    String actionId = thirdPartyPaymentJson.getString("actionId");
                                                    String amountDue = thirdPartyPaymentJson.getString("amountDue");
                                                    String referenceInfo = thirdPartyPaymentJson.getString("referenceInfo");
                                                    String message = thirdPartyPaymentJson.getString("message");
                                                    String link = thirdPartyPaymentJson.getString("link");
                                                    String expirationDate = thirdPartyPaymentJson.getString("expirationDate");

                                                    Document_Actions_ThirdPartyPayment document_actions_thirdPartyPayment = new Document_Actions_ThirdPartyPayment(actionDescription,
                                                            beneficiaryAccount, beneficiaryName, minimumAmountDue, actionPriority, routingNo,
                                                            thirdPartyPayment_actionDueDate, actionId, amountDue,
                                                            referenceInfo, message, link, expirationDate);

                                                    document_actions.document_actions_thirdPartyPaymentToOne.setTarget(document_actions_thirdPartyPayment);


                                                } else if (jsonObject1.has("display")) {
                                                    JSONObject displayJson = jsonObject1.getJSONObject("display");
                                                    String actionDescription = displayJson.getString("actionDescription");
                                                    String actionPriority = displayJson.getString("actionPriority");
                                                    String display_actionDueDate = displayJson.getString("actionDueDate");
                                                    String actionId = displayJson.getString("actionId");
                                                    String message = displayJson.getString("message");
                                                    String link = displayJson.getString("link");
                                                    String expirationDate = displayJson.getString("expirationDate");

                                                    Document_Actions_Display document_actions_display = new Document_Actions_Display(actionDescription, actionPriority, display_actionDueDate,
                                                            actionId, message, link, expirationDate);
                                                    document_actions.document_actions_displayToOne.setTarget(document_actions_display);


                                                } else if (jsonObject1.has("dispute")) {
                                                    JSONObject disputeJson = jsonObject1.getJSONObject("dispute");
                                                    String actionDescription = disputeJson.getString("actionDescription");
                                                    String actionPriority = disputeJson.getString("actionDescription");
                                                    String dispute_actionDueDate = disputeJson.getString("actionDueDate");
                                                    String actionId = disputeJson.getString("actionId");
                                                    String message = disputeJson.getString("message");
                                                    String link = disputeJson.getString("link");
                                                    String expirationDate = disputeJson.getString("expirationDate");

                                                    Document_Actions_Dispute document_actions_dispute = new Document_Actions_Dispute(actionDescription, actionPriority, dispute_actionDueDate,
                                                            actionId, message, link, expirationDate);

                                                    document_actions.document_actions_disputeToOne.setTarget(document_actions_dispute);


                                                } else if (jsonObject1.has("payment")) {
                                                    JSONObject paymentJson = jsonObject1.getJSONObject("payment");
                                                    String actionDescription = paymentJson.getString("actionDescription");
                                                    String minimumAmountDue = paymentJson.getString("minimumAmountDue");
                                                    ;
                                                    String actionPriority = paymentJson.getString("actionPriority");
                                                    String autoPaid = paymentJson.getString("autoPaid");
                                                    String payment_actionDueDate = paymentJson.getString("actionDueDate");
                                                    String amountDue = paymentJson.getString("amountDue");
                                                    String actionId = paymentJson.getString("actionId");
                                                    primaryAction=actionId;
                                                    String message = paymentJson.getString("message");
                                                    String link = paymentJson.getString("link");
                                                    String expirationDate = paymentJson.getString("expirationDate");

                                                    Document_Actions_Payment document_actions_payment = new Document_Actions_Payment(actionDescription, minimumAmountDue, actionPriority,
                                                            autoPaid, payment_actionDueDate, amountDue, actionId, message, link, expirationDate);

                                                    document_actions.document_actions_paymentToOne.setTarget(document_actions_payment);


                                                }

                                            }
                                        } else {

                                            JSONObject jsonObject1 = documentDetailsJson.getJSONObject("actions");
                                            if (jsonObject1.has("thirdPartyPayment")) {
                                                JSONObject thirdPartyPaymentJson = jsonObject1.getJSONObject("thirdPartyPayment");

                                                String actionDescription = thirdPartyPaymentJson.getString("actionDescription");
                                                String beneficiaryAccount = thirdPartyPaymentJson.getString("beneficiaryAccount");
                                                String beneficiaryName = thirdPartyPaymentJson.getString("beneficiaryName");
                                                String minimumAmountDue = thirdPartyPaymentJson.getString("minimumAmountDue");
                                                String actionPriority = thirdPartyPaymentJson.getString("actionPriority");
                                                String routingNo = thirdPartyPaymentJson.getString("routingNo");
                                                String thirdPartyPayment_actionDueDate = thirdPartyPaymentJson.getString("actionDueDate");
                                                String actionId = thirdPartyPaymentJson.getString("actionId");
                                                String amountDue = thirdPartyPaymentJson.getString("amountDue");
                                                String referenceInfo = thirdPartyPaymentJson.getString("referenceInfo");
                                                String message = thirdPartyPaymentJson.getString("message");
                                                String link = thirdPartyPaymentJson.getString("link");
                                                String expirationDate = thirdPartyPaymentJson.getString("expirationDate");

                                                Document_Actions_ThirdPartyPayment document_actions_thirdPartyPayment = new Document_Actions_ThirdPartyPayment(actionDescription,
                                                        beneficiaryAccount, beneficiaryName, minimumAmountDue, actionPriority, routingNo,
                                                        thirdPartyPayment_actionDueDate, actionId, amountDue,
                                                        referenceInfo, message, link, expirationDate);
                                                document_actions.document_actions_thirdPartyPaymentToOne.setTarget(document_actions_thirdPartyPayment);


                                            } else if (jsonObject1.has("display")) {
                                                JSONObject displayJson = jsonObject1.getJSONObject("display");
                                                String actionDescription = displayJson.getString("actionDescription");
                                                String actionPriority = displayJson.getString("actionPriority");
                                                String display_actionDueDate = displayJson.getString("actionDueDate");
                                                String actionId = displayJson.getString("actionId");
                                                String message = displayJson.getString("message");
                                                String link = displayJson.getString("link");
                                                String expirationDate = displayJson.getString("expirationDate");

                                                Document_Actions_Display document_actions_display = new Document_Actions_Display(actionDescription, actionPriority, display_actionDueDate,
                                                        actionId, message, link, expirationDate);
                                                document_actions.document_actions_displayToOne.setTarget(document_actions_display);


                                            } else if (jsonObject1.has("dispute")) {
                                                JSONObject disputeJson = jsonObject1.getJSONObject("dispute");
                                                String actionDescription = disputeJson.getString("actionDescription");
                                                String actionPriority = disputeJson.getString("actionDescription");
                                                String dispute_actionDueDate = disputeJson.getString("actionDueDate");
                                                String actionId = disputeJson.getString("actionId");
                                                String message = disputeJson.getString("message");
                                                String link = disputeJson.getString("link");
                                                String expirationDate = disputeJson.getString("expirationDate");

                                                Document_Actions_Dispute document_actions_dispute = new Document_Actions_Dispute(actionDescription, actionPriority, dispute_actionDueDate,
                                                        actionId, message, link, expirationDate);
                                                document_actions.document_actions_disputeToOne.setTarget(document_actions_dispute);


                                            } else if (jsonObject1.has("payment")) {
                                                JSONObject paymentJson = jsonObject1.getJSONObject("payment");
                                                String actionDescription = paymentJson.getString("actionDescription");
                                                String minimumAmountDue = paymentJson.getString("minimumAmountDue");
                                                ;
                                                String actionPriority = paymentJson.getString("actionPriority");
                                                String autoPaid = paymentJson.getString("autoPaid");
                                                String payment_actionDueDate = paymentJson.getString("actionDueDate");
                                                String amountDue = paymentJson.getString("amountDue");
                                                String actionId = paymentJson.getString("actionId");
                                                primaryAction=actionId;
                                                String message = paymentJson.getString("message");
                                                String link = paymentJson.getString("link");
                                                String expirationDate = paymentJson.getString("expirationDate");

                                                Document_Actions_Payment document_actions_payment = new Document_Actions_Payment(actionDescription, minimumAmountDue, actionPriority,
                                                        autoPaid, payment_actionDueDate, amountDue, actionId, message, link, expirationDate);
                                                document_actions.document_actions_paymentToOne.setTarget(document_actions_payment);


                                            }

                                        }


                                        com.sixd.ecomail.model.Document document = new com.sixd.ecomail.model.Document(documentId, documentName, subscriptionId,
                                                subscriptionType, ownerConsumerId, subscriptionCreateDate, block, mandatory, subscriptionStartDate, categoryDescription,
                                                contentCreateDate, contentLocation, contentUpdateDat, customerIdentifier, customerIdentifierType, displayLocation,
                                                documentCategoryId, documentDate, documentGroup, documentGroupName, documentLogoLocation, documentWebLocation, envelopeImage,
                                                paperlessStatus, periodDescription, periodEnd, periodStart, primaryAction, primaryConsumerId, primaryConsumerName, producerId,
                                                producerMessage, producerName, recipientConsumerId, recipientName, recipientShortName, stateTypeId, transmissionId);
                                        document.document_actionsToOne.setTarget(document_actions);
                                        documentBox.put(document);


                                        if(contentJson.has("con:folderId")){
                                            Object confolderIdJson = contentJson.get("con:folderId");


                                            Box<Folder> folderBox = boxStore.boxFor(Folder.class);

                                            if(confolderIdJson instanceof JSONArray) {
                                                JSONArray foldresIDs = contentJson.getJSONArray("con:folderId");


                                                for(int n=0;n<foldresIDs.length();n++){
                                                    String folderid = foldresIDs.getString(n);
                                                    Folder folder=new APIQueries().getFolderByID(folderid);
                                                    folder.documentToMany.add(document);
                                                    folderBox.put(folder);

                                                    document.folderToOne.setTarget(folder);
                                                    documentBox.put(document);
                                                }
                                            }

                                            else{

                                                String folderid = contentJson.getString("con:folderId");


                                                Folder folder=new APIQueries().getFolderByID(folderid);
                                                folder.documentToMany.add(document);
                                                folderBox.put(folder);

                                                document.folderToOne.setTarget(folder);
                                                documentBox.put(document);


                                            }


                                        }



                                    }
                                } else {

                                    com.sixd.ecomail.model.Document document = new com.sixd.ecomail.model.Document(contentDocumentId, documentName, subscriptionId,
                                            subscriptionType, ownerConsumerId, subscriptionCreateDate, block, mandatory, subscriptionStartDate, categoryDescription,
                                            contentCreateDate, contentLocation, contentUpdateDat, "", "", displayLocation,
                                            "", "", documentGroup, documentGroupName, documentLogoLocation, documentWebLocation, envelopeImage,
                                            paperlessStatus, "", "", "", primaryAction, primaryConsumerId, primaryConsumerName, producerId,
                                            "", producerName, recipientConsumerId, recipientName, recipientShortName, stateTypeId, transmissionId);

                                    documentBox.put(document);

                                    if(contentJson.has("con:folderId")){
                                        Object confolderIdJson = contentJson.get("con:folderId");


                                        Box<Folder> folderBox = boxStore.boxFor(Folder.class);

                                        if(confolderIdJson instanceof JSONArray) {
                                            JSONArray foldresIDs = contentJson.getJSONArray("con:folderId");


                                            for(int n=0;n<foldresIDs.length();n++){
                                                String folderid = foldresIDs.getString(n);
                                                Folder folder=new APIQueries().getFolderByID(folderid);
                                                folder.documentToMany.add(document);
                                                folderBox.put(folder);

                                                document.folderToOne.setTarget(folder);
                                                documentBox.put(document);
                                            }
                                        }

                                        else{

                                            String folderid = contentJson.getString("con:folderId");


                                            Folder folder=new APIQueries().getFolderByID(folderid);
                                            folder.documentToMany.add(document);
                                            folderBox.put(folder);

                                            document.folderToOne.setTarget(folder);
                                            documentBox.put(document);


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
