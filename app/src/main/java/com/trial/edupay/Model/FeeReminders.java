package com.trial.edupay.Model;

import java.util.ArrayList;

/**
 * Created by mallikapriyakhullar on 10/01/18.
 */

public class FeeReminders extends BaseEntity {

    public ArrayList<Fee> fees = new ArrayList<>();

    public FeeReminders(ArrayList<Fee> fees) {
        this.fees = fees;
    }
}