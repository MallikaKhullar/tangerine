package com.trial.edupay.Modules.Notices;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.trial.edupay.Model.Message;
import com.trial.edupay.Utils.BasicStringCallback;

import java.util.ArrayList;

/**
 * Created by mallikapriyakhullar on 11/01/18.
 */

public class NotificationsAdapter extends RecyclerView.Adapter{

    private ArrayList<Message> _messages = new ArrayList<>();

    public static final int HEADER = 0;
    private static final int ITEM = 1;

    BasicStringCallback callback;

    public NotificationsAdapter(ArrayList<Message> messages, BasicStringCallback callback) {
        _messages = messages;
        this.callback = callback;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case HEADER:
                return (RecyclerView.ViewHolder) NotificationViewBuilder
                        .getHeader(parent.getContext())
                        .getTag();
            case ITEM:
                return (RecyclerView.ViewHolder) NotificationViewBuilder
                        .getItem(parent.getContext())
                        .getTag();
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == ITEM && holder instanceof NotificationViewBuilder.NotificationItem) {
            ((NotificationViewBuilder.NotificationItem) holder).setListener(callback);
            ((NotificationViewBuilder.NotificationItem) holder).inflate(_messages.get(position - 1));
        } else {
            ((NotificationViewBuilder.NotificationHeader) holder).inflate(getTotal());
        }
    }


    @Override
    public int getItemCount() {
        return _messages.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0)return HEADER;
        else return ITEM;
    }

    int getTotal() {
        int count = 0;
        for(Message msg: _messages){
            if(!msg.isRead) count ++;
        }
        return count;
    }
}



