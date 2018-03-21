package com.trial.edupay.Modules.Payment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.trial.edupay.Model.Fee;
import com.trial.edupay.Model.FeeReminders;
import com.trial.edupay.R;
import com.trial.edupay.Utils.Utils;

import java.text.MessageFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mallikapriyakhullar on 10/01/18.
 */

public class MultiplePaymentAdapter extends RecyclerView.Adapter<MultiplePaymentAdapter.ViewHolder> {

    private ArrayList<Fee> _fees = new ArrayList<>();

    private int total;
    private Context context;
    private FeeDetailActivity _activity;

    public MultiplePaymentAdapter(FeeDetailActivity activity, FeeReminders feeReminders) {
        _fees = feeReminders.fees;
        _activity = activity;

        for (Fee fee : _fees)
            total = total + fee.netPayableAmount;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.checkBox) CheckBox checkBox;
        @BindView(R.id.studentName) TextView studentName;
        @BindView(R.id.schoolName) TextView schoolName;
        @BindView(R.id.feeBody) TextView feeBody;
        @BindView(R.id.feeAmount) TextView feeAmount;
        @BindView(R.id.overDue) TextView overDue;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public MultiplePaymentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_payment, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MultiplePaymentAdapter.ViewHolder holder, final int position) {

        final Fee fee = _fees.get(position);

        holder.studentName.setText(fee.studentId.name);
        holder.schoolName.setText(fee.organisationId.name);
        holder.feeBody.setText(MessageFormat.format("{0} fees due by {1}", fee.feeCycle.name, Utils.getDisplayDate(Utils.convertToCurrentTimeZone(fee.feeCycle.lastDate))));
        holder.feeAmount.setText(MessageFormat.format("₹ {0}", fee.netPayableAmount));

        if (fee.useLateFee && fee.feeCycle.lastDate.isBeforeNow())
            holder.overDue.setVisibility(View.VISIBLE);
        else
            holder.overDue.setVisibility(View.GONE);

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    total = total + fee.netPayableAmount;

                else
                    total = total - fee.netPayableAmount;

                _activity.setsubTotal(total);
                _activity.setFee(fee._id, b);
            }
        });

//        holder.txtBreakDown.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                _activity.showDialogForFeeBreakdown(fee);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return _fees.size();
    }

}

