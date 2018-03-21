package com.trial.edupay.Model;

import com.trial.edupay.Utils.Utils;

import java.util.ArrayList;

/**
 * Created by mallikapriyakhullar on 10/01/18.
 */
public class CartItem extends BaseEntity {

    public String id;
    public ArrayList<String> feeIds = new ArrayList<>();
    public String paymentMode;
    public String mobile = Utils.getMobile();
    public String email = Utils.getEmail();
    public String transactionMode = "app";
    public int amount;
    public String organisationId;
    public String feeCycle;
}

