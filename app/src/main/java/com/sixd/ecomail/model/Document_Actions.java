package com.sixd.ecomail.model;

import android.os.Parcel;
import android.os.Parcelable;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToOne;

/**
 * Created by Rahul Gupta on 04-05-2018.
 */
@Entity
public class Document_Actions implements Parcelable{
    @Id
    long id;

    public  ToOne<Document_Actions_ThirdPartyPayment> document_actions_thirdPartyPaymentToOne;
    public  ToOne<Document_Actions_Display> document_actions_displayToOne;
    public  ToOne<Document_Actions_Dispute> document_actions_disputeToOne;
    public  ToOne<Document_Actions_Payment> document_actions_paymentToOne;

    public Document_Actions() {
    }

    protected Document_Actions(Parcel in) {
        id = in.readLong();


    }

    public static final Creator<Document_Actions> CREATOR = new Creator<Document_Actions>() {
        @Override
        public Document_Actions createFromParcel(Parcel in) {
            return new Document_Actions(in);
        }

        @Override
        public Document_Actions[] newArray(int size) {
            return new Document_Actions[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeValue(document_actions_thirdPartyPaymentToOne);
        parcel.writeValue(document_actions_displayToOne);
        parcel.writeValue(document_actions_disputeToOne);
        parcel.writeValue(document_actions_paymentToOne);
    }
}
