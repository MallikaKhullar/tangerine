package com.trial.edupay.Model;

import org.joda.time.DateTime;

import java.util.ArrayList;

/**
 * Created by mallikapriyakhullar on 25/12/17.
 */

public class Transaction {
    public String _id;
    public PaymentDetail paymentDetails;
    public String remarks;
    public String mode;
    public int amount;
    public DateTime date;
    public User userId;
    public Boolean deleted;
    public ArrayList<TransactionComponent> components = new ArrayList<>();
}
