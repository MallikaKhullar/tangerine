package com.trial.edupay.Modules.Current;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.trial.edupay.Model.Fee;
import com.trial.edupay.R;
import com.trial.edupay.Utils.Utils;

import java.text.MessageFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mallikapriyakhullar on 25/12/17.
 */

public class ReminderViewBuilder {

    public static View getHeader(Context context){
        LayoutInflater inflater = LayoutInflater.from(context);
        View mainView = inflater.inflate(R.layout.gradient_total_reminder, null);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0,20);
        mainView.setLayoutParams(layoutParams);
        ReminderHeader viewHolder = new ReminderHeader(mainView);
        mainView.setTag(viewHolder);
        return mainView;
    }

    public static View getItem(Context context){
        LayoutInflater inflater = LayoutInflater.from(context);
        View mainView = inflater.inflate(R.layout.item_fee_reminder, null);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(20, 5, 20,5);
        mainView.setLayoutParams(layoutParams);
        ReminderItem viewHolder = new ReminderItem(mainView);
        mainView.setTag(viewHolder);
        return mainView;
    }


    public static class ReminderHeader extends RecyclerView.ViewHolder{
        @BindView(R.id.totalDue) public TextView totalDue;
        public View container;

        public ReminderHeader(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            container = itemView;
        }

        void setOnClick(View.OnClickListener listener) {
            container.setOnClickListener(listener);
        }

        void inflate(Long total) {
            totalDue.setText(MessageFormat.format("₹ {0}", total));
        }
    }

    public static class ReminderItem extends RecyclerView.ViewHolder{

        @BindView(R.id.layout) public View layout;
        @BindView(R.id.feeSubject) public TextView feeSubject;
        @BindView(R.id.feeBody) public TextView feeBody;
        @BindView(R.id.feeCollege) public TextView feeCollege;
        @BindView(R.id.dueDate) public TextView dueDate;
        @BindView(R.id.feeAmount) public TextView feeAmount;
        @BindView(R.id.overDue) public View overDue;

        View container;

        public ReminderItem(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            container = itemView;
        }

        void setOnClick(View.OnClickListener listener) {
            layout.setOnClickListener(listener);
        }

        void inflate(Fee fee) {

            feeSubject.setText(fee.studentId.name );
            feeCollege.setText("\u2022   " + fee.organisationId.name);
            feeBody.setText(fee.feeCycle.name);
            dueDate.setText("Due by " + Utils.getDisplayDate(fee.feeCycle.lastDate));
            feeAmount.setText(MessageFormat.format("₹ {0}", fee.netPayableAmount));

            if(fee.feeCycle.lastDate.isBeforeNow())
                overDue.setVisibility(View.VISIBLE);
            else
                overDue.setVisibility(View.GONE);
        }
    }
}
