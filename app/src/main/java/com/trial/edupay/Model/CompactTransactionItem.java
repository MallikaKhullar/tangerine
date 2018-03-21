package com.trial.edupay.Model;

import org.joda.time.DateTime;

/**
 * Created by mallikapriyakhullar on 25/12/17.
 */


public class CompactTransactionItem {
    public PaymentDetail paymentDetails;
    public String remarks;
    public String mode;
    public int amount;
    public DateTime date;
    public User userId;
    public Boolean deleted;
    public String id;
    public TransactionComponent component;
}
