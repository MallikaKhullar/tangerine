package com.trial.edupay.Model;

import org.joda.time.DateTime;

import java.util.ArrayList;

/**
 * Created by mallikapriyakhullar on 25/12/17.
 */

public class PaymentDetail {

    public String mobile;
    public String email;
    public int amount;
    public String date = DateTime.now().toString();
    public String transactionReference;
    public ArrayList<FeeDetail> feeIdsAndAmounts = new ArrayList<>();
}
