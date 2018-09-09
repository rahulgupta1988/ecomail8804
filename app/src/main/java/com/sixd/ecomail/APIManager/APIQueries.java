package com.sixd.ecomail.APIManager;

import com.sixd.ecomail.Utility.MyApplication;
import com.sixd.ecomail.model.DocTypes;
import com.sixd.ecomail.model.DocTypes_;
import com.sixd.ecomail.model.Document;
import com.sixd.ecomail.model.DocumentGroup;
import com.sixd.ecomail.model.DocumentGroup_;
import com.sixd.ecomail.model.Document_;
import com.sixd.ecomail.model.Folder;
import com.sixd.ecomail.model.Folder_;
import com.sixd.ecomail.model.FundingSources;
import com.sixd.ecomail.model.InboxSortOrder;
import com.sixd.ecomail.model.InboxSortOrder_;
import com.sixd.ecomail.model.Payment;
import com.sixd.ecomail.model.Payment_;
import com.sixd.ecomail.model.Producer;
import com.sixd.ecomail.model.Recipents;
import com.sixd.ecomail.model.Recipents_;
import com.sixd.ecomail.model.SmartFolder;
import com.sixd.ecomail.model.SmartFolder_;
import com.sixd.ecomail.model.Subscription;
import com.sixd.ecomail.model.Subscription_;

import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.query.QueryBuilder;

/**
 * Created by Rahul Gupta on 04-05-2018.
 */
public class APIQueries {

    public List<Document> getDocsByLocation(String displayLocation) {

        BoxStore boxStore = MyApplication.getInstance().getBoxStore();
        Box<Document> documentBox = boxStore.boxFor(com.sixd.ecomail.model.Document.class);
        QueryBuilder<Document> builder = documentBox.query();
        builder.equal(Document_.displayLocation, displayLocation);
        List<Document> documents = builder.build().find();
        return documents;
    }


    public List<Document> getDocsByPrimaryAction(String primaryAction,String displayLocation) {

        BoxStore boxStore = MyApplication.getInstance().getBoxStore();
        Box<Document> documentBox = boxStore.boxFor(com.sixd.ecomail.model.Document.class);
        QueryBuilder<Document> builder = documentBox.query();
        builder.equal(Document_.primaryAction, primaryAction);
        builder.notEqual(Document_.displayLocation,displayLocation);
        List<Document> documents = builder.build().find();
        return documents;
    }



    public String getDocsGroupNameByID(String GPId) {

        String gPName = "";
        BoxStore boxStore = MyApplication.getInstance().getBoxStore();
        Box<DocumentGroup> documentBox = boxStore.boxFor(DocumentGroup.class);
        QueryBuilder<DocumentGroup> builder = documentBox.query();
        builder.equal(DocumentGroup_.documentGroupId, GPId);
        List<DocumentGroup> documentGroups = builder.build().find();
        if (documentGroups.size() > 0)
            return documentGroups.get(0).getDocumentGroupName();

        return gPName;
    }

    public List<DocumentGroup> getDocsGroups() {

        String gPName = "";
        BoxStore boxStore = MyApplication.getInstance().getBoxStore();
        Box<DocumentGroup> documentBox = boxStore.boxFor(DocumentGroup.class);
        QueryBuilder<DocumentGroup> builder = documentBox.query();
        List<DocumentGroup> documentGroups = builder.build().find();
        return documentGroups;
    }


    public void initSortOptions() {
        BoxStore boxStore = MyApplication.getInstance().getBoxStore();
        Box<InboxSortOrder> inboxSortOrderBox = boxStore.boxFor(InboxSortOrder.class);

        // INBOX
        if (getSortOptionBycatName("Inbox").size() == 0) {
            InboxSortOrder inboxSortOrde1 = new InboxSortOrder("Document", "Document Type", "Inbox",
                    "contentCreationDate", 1, true, "DESC");

            InboxSortOrder inboxSortOrder2 = new InboxSortOrder("Document", "Document Received", "Inbox",
                    "documentCategoryId", 2, false, "ASC");

            InboxSortOrder inboxSortOrder3 = new InboxSortOrder("Document", "Company", "Inbox",
                    "producerName", 3, false, "ASC");

            InboxSortOrder inboxSortOrder4 = new InboxSortOrder("Document", "Document Date", "Inbox",
                    "documentDate", 4, false, "ASC");

            inboxSortOrderBox.put(inboxSortOrde1, inboxSortOrder2, inboxSortOrder3, inboxSortOrder4);
        }


        //PAYMENT

        if (getSortOptionBycatName("Payment").size() == 0) {
            InboxSortOrder payment1 = new InboxSortOrder("Payment", "Document Received", "Payment",
                    "documentDateReceived", 1, false, "DESC");

            InboxSortOrder payment2 = new InboxSortOrder("Payment", "Payment Date", "Payment",
                    "scheduleDate", 2, false, "DESC");

            InboxSortOrder payment3 = new InboxSortOrder("Payment", "Payment Amount", "Payment",
                    "paymentAmount", 3, false, "DESC");

            InboxSortOrder payment4 = new InboxSortOrder("Payment", "Document Type", "Payment",
                    "documentType", 4, false, "ASC");

            InboxSortOrder payment5 = new InboxSortOrder("Payment", "Company", "Payment",
                    "documentType", 5, false, "ASC");


            inboxSortOrderBox.put(payment1, payment2, payment3, payment4, payment5);
        }


        //Document

        if (getSortOptionBycatName("Archive").size() == 0) {

            InboxSortOrder document1 = new InboxSortOrder("Document", "Document Received", "Archive",
                    "contentCreationDate", 1, true, "DESC");

            InboxSortOrder document2 = new InboxSortOrder("Document", "Document Type", "Archive",
                    "documentCategoryId", 2, false, "ASC");

            InboxSortOrder document3 = new InboxSortOrder("Document", "Company", "Archive",
                    "documentCategoryId", 3, false, "ASC");

            InboxSortOrder document4 = new InboxSortOrder("Document", "Document Date", "Archive",
                    "documentDate", 4, false, "ASC");


            inboxSortOrderBox.put(document1, document2, document3, document4);
        }


    }

    public List<InboxSortOrder> getSortOptionBycatName(String catName) {
        BoxStore boxStore = MyApplication.getInstance().getBoxStore();
        Box<InboxSortOrder> inboxSortOrderBox = boxStore.boxFor(InboxSortOrder.class);
        QueryBuilder<InboxSortOrder> builder = inboxSortOrderBox.query();
        builder.equal(InboxSortOrder_.category, catName);
        List<InboxSortOrder> inboxSortOrders = builder.build().find();
        return inboxSortOrders;
    }

    public long removeSortOptionBycatName(String catName) {
        BoxStore boxStore = MyApplication.getInstance().getBoxStore();
        Box<InboxSortOrder> inboxSortOrderBox = boxStore.boxFor(InboxSortOrder.class);
        QueryBuilder<InboxSortOrder> builder = inboxSortOrderBox.query();
        builder.equal(InboxSortOrder_.category, catName);
        long rows = builder.build().remove();
        return rows;

    }

    public List<Producer> getProducers() {
        BoxStore boxStore = MyApplication.getInstance().getBoxStore();
        Box<Producer> producerBox = boxStore.boxFor(Producer.class);
        QueryBuilder<Producer> builder = producerBox.query();
        List<Producer> producerList = builder.build().find();
        return producerList;
    }

    public List<DocTypes> getDocumentTypes() {
        BoxStore boxStore = MyApplication.getInstance().getBoxStore();
        Box<DocTypes> documentTypeBox = boxStore.boxFor(DocTypes.class);
        QueryBuilder<DocTypes> builder = documentTypeBox.query();
        List<DocTypes> documentTypeList = builder.build().find();
        return documentTypeList;
    }

    public List<Recipents> getRecipents() {
        BoxStore boxStore = MyApplication.getInstance().getBoxStore();
        Box<Recipents> recipentsBox = boxStore.boxFor(Recipents.class);
        QueryBuilder<Recipents> builder = recipentsBox.query();
        List<Recipents> recipentsList = builder.build().find();
        return recipentsList;
    }

    public Folder getFolderByID(String folderID) {

        BoxStore boxStore = MyApplication.getInstance().getBoxStore();
        Box<Folder> folderBox = boxStore.boxFor(Folder.class);
        QueryBuilder<Folder> builder = folderBox.query();
        builder.equal(Folder_.folderId, folderID);
        Folder folder = builder.build().findFirst();
        return folder;

    }

    public SmartFolder getSmartFolderByID(String folderID) {

        BoxStore boxStore = MyApplication.getInstance().getBoxStore();
        Box<SmartFolder> smartFolderBox = boxStore.boxFor(SmartFolder.class);
        QueryBuilder<SmartFolder> builder = smartFolderBox.query();
        builder.equal(SmartFolder_.smartFolderId, folderID);
        SmartFolder folder = builder.build().findFirst();
        return folder;

    }

    public SmartFolder getSmartFolderByName(String smartFolerName) {

        BoxStore boxStore = MyApplication.getInstance().getBoxStore();
        Box<SmartFolder> smartFolderBox = boxStore.boxFor(SmartFolder.class);
        QueryBuilder<SmartFolder> builder = smartFolderBox.query();
        builder.equal(SmartFolder_.smartFolderName, smartFolerName);
        SmartFolder folder = builder.build().findFirst();
        return folder;

    }

    public long removeFolderByFolderID(String folderid,boolean isSmrt) {
        if(isSmrt){
            BoxStore boxStore = MyApplication.getInstance().getBoxStore();
            Box<SmartFolder> folderBox = boxStore.boxFor(SmartFolder.class);
            QueryBuilder<SmartFolder> builder = folderBox.query();
            builder.equal(SmartFolder_.smartFolderId, folderid);
            long rows = builder.build().remove();
            return rows;
        }
        else {
            BoxStore boxStore = MyApplication.getInstance().getBoxStore();
            Box<Folder> folderBox = boxStore.boxFor(Folder.class);
            QueryBuilder<Folder> builder = folderBox.query();
            builder.equal(Folder_.folderId, folderid);
            long rows = builder.build().remove();
            return rows;
        }

    }

    public void updateFolderByFolderName(String folderID,String folderName) {
        BoxStore boxStore = MyApplication.getInstance().getBoxStore();
        Box<Folder> folderBox = boxStore.boxFor(Folder.class);
        QueryBuilder<Folder> builder = folderBox.query();
        builder.equal(Folder_.folderId, folderID);
        Folder folder = builder.build().findFirst();
        folder.setFolderName(folderName);
        folderBox.put(folder);

    }

    public void updateLocationByTransmissionId(String transmissionId,String displayLocation) {
        BoxStore boxStore = MyApplication.getInstance().getBoxStore();
        Box<Document> documentBox = boxStore.boxFor(Document.class);
        QueryBuilder<Document> builder = documentBox.query();
        builder.equal(Document_.transmissionId, transmissionId);
        Document document = builder.build().findFirst();
        document.setDisplayLocation(displayLocation);
        documentBox.put(document);

    }


    public String getpaymentStateIdByTransmissionID(String transmissionId) {

        MyApplication.MyLogi("tra3443",""+transmissionId);
        BoxStore boxStore = MyApplication.getInstance().getBoxStore();
        Box<Payment> paymentBox = boxStore.boxFor(Payment.class);
        QueryBuilder<Payment> builder = paymentBox.query();
        builder.equal(Payment_.transmissionId, transmissionId);
        Payment payment = builder.build().findFirst();
        if(payment!=null)
        return payment.getPaymentStateId();

        return "";

    }

    public Payment getpaymentByTransmissionID(String transmissionId) {

        MyApplication.MyLogi("tra3443",""+transmissionId);
        BoxStore boxStore = MyApplication.getInstance().getBoxStore();
        Box<Payment> paymentBox = boxStore.boxFor(Payment.class);
        QueryBuilder<Payment> builder = paymentBox.query();
        builder.equal(Payment_.transmissionId, transmissionId);
        Payment payment = builder.build().findFirst();
        if(payment!=null)
            return payment;

        return null;

    }

    public List<Payment> getpaymentsByTransmissionID(String transmissionId) {

        MyApplication.MyLogi("tra3443",""+transmissionId);
        BoxStore boxStore = MyApplication.getInstance().getBoxStore();
        Box<Payment> paymentBox = boxStore.boxFor(Payment.class);
        QueryBuilder<Payment> builder = paymentBox.query();
        builder.equal(Payment_.transmissionId, transmissionId);
        List<Payment> payment = builder.build().find();
        if(payment!=null)
            return payment;

        return null;

    }


    public Document getDocumentByID(String documentId) {
        BoxStore boxStore = MyApplication.getInstance().getBoxStore();
        Box<Document> documentBox = boxStore.boxFor(Document.class);
        QueryBuilder<Document> builder = documentBox.query();
        builder.equal(Document_.documentId, documentId);
        Document document = builder.build().findFirst();
        return document;

    }

    public DocumentGroup getDocumentGroupByID(String documentGroupId) {
        BoxStore boxStore = MyApplication.getInstance().getBoxStore();
        Box<DocumentGroup> documentGroupBox = boxStore.boxFor(DocumentGroup.class);
        QueryBuilder<DocumentGroup> builder = documentGroupBox.query();
        builder.equal(DocumentGroup_.documentGroupId, documentGroupId);
        DocumentGroup documentGroup = builder.build().findFirst();
        return documentGroup;

    }


    public DocTypes getDocumentTypeByID(String cocumentCategroyId) {
        BoxStore boxStore = MyApplication.getInstance().getBoxStore();
        Box<DocTypes> docTypesBox = boxStore.boxFor(DocTypes.class);
        QueryBuilder<DocTypes> builder = docTypesBox.query();
        builder.equal(DocTypes_.documentType, cocumentCategroyId);
        DocTypes docTypes = builder.build().findFirst();
        return docTypes;

    }

    public Recipents getRecipentsByID(String recipID) {
        BoxStore boxStore = MyApplication.getInstance().getBoxStore();
        Box<Recipents> recipentsBox = boxStore.boxFor(Recipents.class);
        QueryBuilder<Recipents> builder = recipentsBox.query();
        builder.equal(Recipents_.consumerId, recipID);
        Recipents recipent = builder.build().findFirst();
        return recipent;
    }


    public Subscription getSubscriptionByID(String subscriptionID) {

        BoxStore boxStore = MyApplication.getInstance().getBoxStore();
        Box<Subscription> subscriptionBox = boxStore.boxFor(Subscription.class);
        QueryBuilder<Subscription> builder = subscriptionBox.query();
        builder.equal(Subscription_.documentId, subscriptionID);
        Subscription Subscription = builder.build().findFirst();

        return Subscription;
    }



    public List<DocumentGroup> getDocumentGroup() {

        BoxStore boxStore = MyApplication.getInstance().getBoxStore();
        Box<DocumentGroup> documentGroupBox = boxStore.boxFor(DocumentGroup.class);
        QueryBuilder<DocumentGroup> builder = documentGroupBox.query();
        List<DocumentGroup> documentGroups = builder.build().find();

        return documentGroups;
    }


    public List<FundingSources> getFundingSources() {

        BoxStore boxStore = MyApplication.getInstance().getBoxStore();
        Box<FundingSources> fundingSourcesBox = boxStore.boxFor(FundingSources.class);
        QueryBuilder<FundingSources> builder = fundingSourcesBox.query();
        List<FundingSources> fundingSources = builder.build().find();

        return fundingSources;
    }



}
