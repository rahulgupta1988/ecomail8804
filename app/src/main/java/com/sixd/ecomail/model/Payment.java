package com.sixd.ecomail.model;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by Rahul Gupta on 24-05-2018.
 */
@Entity
public class Payment {

    @Id
    long id;

    String consumerId="";
    String transmissionId="";
    String paymentConfirmationNo="";
    String paymentAmount="";
    String bankId="";
    String bankName="";
    String bankAccount="";
    String cardNumber="";
    String scheduleDate="";
    String paymentTypeId="";
    String paymentStateId="";
    String createDate="";
    String paymentTrackerId="";


    public Payment() {
    }

    public Payment(String consumerId, String transmissionId, String paymentConfirmationNo, String paymentAmount, String bankId, String bankName, String bankAccount, String cardNumber, String scheduleDate, String paymentTypeId, String paymentStateId, String createDate, String paymentTrackerId) {
        this.consumerId = consumerId;
        this.transmissionId = transmissionId;
        this.paymentConfirmationNo = paymentConfirmationNo;
        this.paymentAmount = paymentAmount;
        this.bankId = bankId;
        this.bankName = bankName;
        this.bankAccount = bankAccount;
        this.cardNumber = cardNumber;
        this.scheduleDate = scheduleDate;
        this.paymentTypeId = paymentTypeId;
        this.paymentStateId = paymentStateId;
        this.createDate = createDate;
        this.paymentTrackerId = paymentTrackerId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public String getTransmissionId() {
        return transmissionId;
    }

    public void setTransmissionId(String transmissionId) {
        this.transmissionId = transmissionId;
    }

    public String getPaymentConfirmationNo() {
        return paymentConfirmationNo;
    }

    public void setPaymentConfirmationNo(String paymentConfirmationNo) {
        this.paymentConfirmationNo = paymentConfirmationNo;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(String scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public String getPaymentTypeId() {
        return paymentTypeId;
    }

    public void setPaymentTypeId(String paymentTypeId) {
        this.paymentTypeId = paymentTypeId;
    }

    public String getPaymentStateId() {
        return paymentStateId;
    }

    public void setPaymentStateId(String paymentStateId) {
        this.paymentStateId = paymentStateId;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getPaymentTrackerId() {
        return paymentTrackerId;
    }

    public void setPaymentTrackerId(String paymentTrackerId) {
        this.paymentTrackerId = paymentTrackerId;
    }
}
