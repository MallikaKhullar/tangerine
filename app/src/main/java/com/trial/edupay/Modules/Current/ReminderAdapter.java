package com.trial.edupay.Modules.Current;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;


import com.trial.edupay.Model.Fee;

import java.util.ArrayList;

/**
 * Created by mallikapriyakhullar on 24/12/17.
 */

public class ReminderAdapter extends RecyclerView.Adapter{

    private ArrayList<Fee> _feeReminders = new ArrayList<>();
    View.OnClickListener itemClickListener;

    public static final int HEADER = 0;
    private static final int ITEM = 1;

    public ReminderAdapter(ArrayList<Fee> feeReminders, View.OnClickListener itemClickListener) {
        _feeReminders = feeReminders;
        this.itemClickListener = itemClickListener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType)
        {
            case HEADER:
                return (ReminderViewBuilder.ReminderHeader) ReminderViewBuilder
                        .getHeader(parent.getContext())
                        .getTag();
            case ITEM:
                return (ReminderViewBuilder.ReminderItem) ReminderViewBuilder
                        .getItem(parent.getContext())
                        .getTag();
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == ITEM && holder instanceof ReminderViewBuilder.ReminderItem) {
            ((ReminderViewBuilder.ReminderItem) holder).inflate(_feeReminders.get(position - 1));
            ((ReminderViewBuilder.ReminderItem) holder).setOnClick(itemClickListener);
        } else {
            ((ReminderViewBuilder.ReminderHeader) holder).inflate(getTotal());
            ((ReminderViewBuilder.ReminderHeader) holder).setOnClick(itemClickListener);

        }
    }

    @Override
    public int getItemCount() {
        return _feeReminders.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0)return HEADER;
        else return ITEM;
    }

    Long getTotal(){
        Long total = 0L;
        for(Fee fee: _feeReminders) total += fee.netPayableAmount;
        return total;
    }
}

