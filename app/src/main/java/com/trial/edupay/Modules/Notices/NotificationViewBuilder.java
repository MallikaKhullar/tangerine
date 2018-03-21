package com.trial.edupay.Modules.Notices;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.trial.edupay.MainApplication;
import com.trial.edupay.Model.Message;
import com.trial.edupay.R;
import com.trial.edupay.Utils.BasicStringCallback;
import com.trial.edupay.Utils.Utils;

import org.joda.time.DateTime;
import org.ocpsoft.prettytime.PrettyTime;

import java.text.MessageFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mallikapriyakhullar on 11/01/18.
 */

public class NotificationViewBuilder {

    public static View getHeader(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View mainView = inflater.inflate(R.layout.gradient_total_reminder, null);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, 0);
        mainView.setLayoutParams(layoutParams);
        NotificationHeader viewHolder = new NotificationHeader(mainView);
        mainView.setTag(viewHolder);
        return mainView;
    }

    public static View getItem(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View mainView = inflater.inflate(R.layout.item_notification, null);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, 180);
        mainView.setLayoutParams(layoutParams);
        NotificationItem viewHolder = new NotificationItem(mainView);
        mainView.setTag(viewHolder);
        return mainView;
    }





    public static class NotificationHeader extends RecyclerView.ViewHolder {
        @BindView(R.id.totalDue) public TextView totalDue;
        @BindView(R.id.headerText) public TextView headerText;

        public NotificationHeader(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void inflate(int total) {
            headerText.setText("Total unread notifications");
            totalDue.setText(MessageFormat.format("{0}", total > 0 ? total : '-'));
        }
    }

        public static class NotificationItem extends RecyclerView.ViewHolder {
            @BindView(R.id.schoolLogo) ImageView schoolLogo;
            @BindView(R.id.schoolName) TextView schoolName;
            @BindView(R.id.messageBody) TextView messageBody;
            @BindView(R.id.msgCategory) TextView msgCategory;
            @BindView(R.id.days) TextView days;

            View container;

            BasicStringCallback callback;

            public NotificationItem(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                container = itemView;
            }

            void setListener(final BasicStringCallback callback) {
                this.callback = callback;
            }

            void inflate(final Message message) {
                container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {callback.onSuccess(message.toJson());
                    }
                });

                if(!message.isRead) container.setBackgroundColor(ContextCompat.getColor(MainApplication.getAppContext(),R.color.lightestblue));
                else container.setBackgroundColor(ContextCompat.getColor(MainApplication.getAppContext(),R.color.white));


                PrettyTime p = new PrettyTime(DateTime.now().toLocalDateTime().toDate());
                days.setText(p.format((message.createdAt).toLocalDateTime().toDate()));

                if(message.organisationId.logo != null) {
//                    Glide.with(context)
//                            .load(message.organisationId.logo)
//                            .centerCrop()
//                            .fitCenter()
//                            .into(holder.schoolLogo);
                }

               schoolName.setText(message.organisationId.name);
               messageBody.setText(Utils.convertHtmlToString(message.content));
               //TODO - implement after category is implemented
//                msgCategory.setText(message.category);
            }
        }
}