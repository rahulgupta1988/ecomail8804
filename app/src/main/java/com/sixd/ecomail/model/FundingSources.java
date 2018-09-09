package com.sixd.ecomail.model;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by Rahul Gupta on 02-05-2018.
 */

@Entity
public class FundingSources {
    @Id
    long ID;
    String consumerId = "";
    String bankId = "";
    String bankName = "";
    String accountTypeId = "";
    String accountName = "";
    String accountNumber = "";
    String routingNumber = "";
    String currentBalance = "";
    String ledgerBalance = "";

    public FundingSources(){}

    public FundingSources(String consumerId, String bankId, String bankName, String accountTypeId, String accountName, String accountNumber, String routingNumber, String currentBalance, String ledgerBalance) {
        this.consumerId = consumerId;
        this.bankId = bankId;
        this.bankName = bankName;
        this.accountTypeId = accountTypeId;
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.routingNumber = routingNumber;
        this.currentBalance = currentBalance;
        this.ledgerBalance = ledgerBalance;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
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

    public String getAccountTypeId() {
        return accountTypeId;
    }

    public void setAccountTypeId(String accountTypeId) {
        this.accountTypeId = accountTypeId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getRoutingNumber() {
        return routingNumber;
    }

    public void setRoutingNumber(String routingNumber) {
        this.routingNumber = routingNumber;
    }

    public String getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(String currentBalance) {
        this.currentBalance = currentBalance;
    }

    public String getLedgerBalance() {
        return ledgerBalance;
    }

    public void setLedgerBalance(String ledgerBalance) {
        this.ledgerBalance = ledgerBalance;
    }
}
