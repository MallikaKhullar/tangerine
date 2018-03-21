package com.trial.edupay.Modules.History;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.trial.edupay.Model.CompactTransactionItem;

import java.util.ArrayList;

/**
 * Created by mallikapriyakhullar on 25/12/17.
 */

public class TransactionAdapter extends RecyclerView.Adapter{

    private ArrayList<CompactTransactionItem> _transactions = new ArrayList<>();

    public static final int HEADER = 0;
    private static final int ITEM = 1;

    public TransactionAdapter(ArrayList<CompactTransactionItem> transactions) {
        _transactions = transactions;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case HEADER:
                return (RecyclerView.ViewHolder) TransactionViewBuilder
                        .getHeader(parent.getContext())
                        .getTag();
            case ITEM:
                return (RecyclerView.ViewHolder) TransactionViewBuilder
                        .getItem(parent.getContext())
                        .getTag();
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == ITEM && holder instanceof TransactionViewBuilder.HistoryItem) {
            ((TransactionViewBuilder.HistoryItem) holder).inflate(_transactions.get(position - 1));
        } else {
            ((TransactionViewBuilder.HistoryHeader) holder).inflate(getTotal());
        }
    }


    Long getTotal(){
        Long total = 0L;
        for(CompactTransactionItem transaction: _transactions) total += transaction.component.amount;
        return total;
    }

    @Override
    public int getItemCount() {
        return _transactions.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0)return HEADER;
        else return ITEM;
    }
}


