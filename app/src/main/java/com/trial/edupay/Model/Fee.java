package com.trial.edupay.Model;

import org.joda.time.DateTime;

import java.util.ArrayList;

/**
 * Created by mallikapriyakhullar on 24/12/17.
 */

public class Fee {
    public String _id;
    public String feeCycleId;
    public Student studentId = new Student();
    public String status;
    public Organisation organisationId = new Organisation();
    public Boolean deleted;
    public int pendingAmount;
    public int totalAmount;
    public int netPayableAmount;
    public transient DateTime approvalDate;
    public int lateFee;
    public Boolean useLateFee;
    public FeeCycle feeCycle = new FeeCycle();
    public transient DateTime lastDate;
    public ArrayList<FeeComponent> feeComponents = new ArrayList<>();
}
