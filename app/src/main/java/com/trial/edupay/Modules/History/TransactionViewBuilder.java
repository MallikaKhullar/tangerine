package com.trial.edupay.Modules.History;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.trial.edupay.Model.CompactTransactionItem;
import com.trial.edupay.R;
import com.trial.edupay.Utils.Utils;

import java.text.MessageFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mallikapriyakhullar on 25/12/17.
 */

public class TransactionViewBuilder {

    public static View getHeader(Context context){
        LayoutInflater inflater = LayoutInflater.from(context);
        View mainView = inflater.inflate(R.layout.gradient_total_reminder, null);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0,20);
        mainView.setLayoutParams(layoutParams);
        HistoryHeader viewHolder = new HistoryHeader(mainView);
        mainView.setTag(viewHolder);
        return mainView;
    }

    public static View getItem(Context context){
        LayoutInflater inflater = LayoutInflater.from(context);
        View mainView = inflater.inflate(R.layout.item_transaction_history, null);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(20, 5, 20, 25);
        mainView.setLayoutParams(layoutParams);
        HistoryItem viewHolder = new HistoryItem(mainView);
        mainView.setTag(viewHolder);
        return mainView;
    }


    public static class HistoryHeader extends RecyclerView.ViewHolder{
        @BindView(R.id.totalDue) public TextView totalDue;
        @BindView(R.id.headerText) public TextView headerText;

        public HistoryHeader(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void inflate(Long total) {
            headerText.setText("Total fees paid so far");
            totalDue.setText(MessageFormat.format("₹ {0}", total));
        }
    }

    public static class HistoryItem extends RecyclerView.ViewHolder{
        @BindView(R.id.paymentTime) TextView paymentTime;
        @BindView(R.id.feePaidDate) TextView feePaidDate;
        @BindView(R.id.transactionId) TextView transactionText;
        @BindView(R.id.feeAmount) TextView feeAmount;
        @BindView(R.id.schoolLogo) ImageView schoolLogo;
        @BindView(R.id.schoolName) TextView schoolName;
        @BindView(R.id.studentName) TextView studentName;

        View container;

        public HistoryItem(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            container = itemView;
        }

        void inflate(CompactTransactionItem transaction) {

            feeAmount.setText(MessageFormat.format("{0}", transaction.component.amount));
            schoolName.setText(transaction.component.fee.organisationId.name);
            studentName.setText(transaction.component.fee.studentId.name);
            paymentTime.setText(MessageFormat.format("Payment for ₹ {0}", transaction.component.fee.feeCycle.name));
            transactionText.setText(transaction.id);
            feePaidDate.setText(Utils.getDisplayDate((transaction.date)));

            if (transaction.component.fee.organisationId.logo != null) {
//                Glide.with(context)
//                        .load(transaction.fee.organisationId.logo)
//                        .centerCrop()
//                        .fitCenter()
//                        .into(schoolLogo);
            }
        }
    }
}
