package com.trial.edupay.Modules.Payment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.trial.edupay.Controller.PaymentApiController;
import com.trial.edupay.Model.BaseEntity;
import com.trial.edupay.Model.CartItem;
import com.trial.edupay.Model.ConvenienceFee;
import com.trial.edupay.Model.Fee;
import com.trial.edupay.Model.FeeReminders;
import com.trial.edupay.Modules.Base.BaseActionBarActivity;
import com.trial.edupay.R;
import com.trial.edupay.Utils.ToastHandler;

import java.text.MessageFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mallikapriyakhullar on 10/01/18.
 */

public class FeeDetailActivity extends BaseActionBarActivity {

    @BindView(R.id.recycler) RecyclerView recyclerView;
    @BindView(R.id.subtotalAmount) TextView subtotalAmount;
    @BindView(R.id.convenienceFee) TextView convenienceFeeAmount;
    @BindView(R.id.totalAmount) TextView totalAmount;
    @BindView(R.id.paymentTable) View paymentTable;
    @BindView(R.id.btnNext) Button btnNext;
    
    RadioButton radioNetBanking, radioCreditCard, radioDebitCard, radioWallet, radioUpi;
    TextView netBankingFees, debitCardFees, creditCardFees, walletFees, upiFees;

    private int convenienceFees, subTotalFees, totalFees;
    private String _paymentMode;
    private String studentJson;
    private String organisationId;
    private String feeCycle;

    private ConvenienceFee _convenienceFee;
    private FeeReminders _feeReminders;
    
    private ArrayList<String> _feeIds = new ArrayList<>();

    @Override
    protected boolean setupActionBar() {
        super.setupActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_detail);
        ButterKnife.bind(this);
        setupActionBar();

        radioNetBanking =  paymentTable.findViewById(R.id.radioNetBanking);
        radioCreditCard =  paymentTable.findViewById(R.id.radioCreditCard);
        radioDebitCard =  paymentTable.findViewById(R.id.radioDebitCard);
        radioWallet =  paymentTable.findViewById(R.id.radioWallet);
        radioUpi =  paymentTable.findViewById(R.id.radioUpi);
        netBankingFees =  paymentTable.findViewById(R.id.netBankingFees);
        debitCardFees =  paymentTable.findViewById(R.id.debitCardFees);
        creditCardFees =  paymentTable.findViewById(R.id.creditCardFees);
        walletFees =  paymentTable.findViewById(R.id.walletFees);
        upiFees =  paymentTable.findViewById(R.id.upiFees);

        recyclerView.setNestedScrollingEnabled(false);

        Bundle bundle = getIntent().getExtras();
        
        if (bundle != null) {
            String json = bundle.getString("feeReminders");
            studentJson = bundle.getString("student");
            organisationId = bundle.getString("organisationId");
            if (json != null) {
                _feeReminders = BaseEntity.fromJson(json, FeeReminders.class);
                bindFees();

                for (Fee fee : _feeReminders.fees) {
                    totalFees = totalFees + fee.netPayableAmount;
                    _feeIds.add(fee._id);
                }

                feeCycle = _feeReminders.fees.get(0).organisationId.name;

                setsubTotal(totalFees);
            }
        }

        radioNetBanking.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    radioCreditCard.setChecked(false);
                    radioDebitCard.setChecked(false);
                    radioWallet.setChecked(false);
                    radioUpi.setChecked(false);
                    setTotal();

                }
            }
        });

        radioCreditCard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    radioNetBanking.setChecked(false);
                    radioDebitCard.setChecked(false);
                    radioWallet.setChecked(false);
                    radioUpi.setChecked(false);
                    setTotal();

                }
            }
        });

        radioDebitCard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    radioNetBanking.setChecked(false);
                    radioCreditCard.setChecked(false);
                    radioWallet.setChecked(false);
                    radioUpi.setChecked(false);
                    setTotal();

                }
            }
        });

        radioWallet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    radioNetBanking.setChecked(false);
                    radioCreditCard.setChecked(false);
                    radioDebitCard.setChecked(false);
                    radioUpi.setChecked(false);
                    setTotal();
                }
            }
        });

        radioUpi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    radioNetBanking.setChecked(false);
                    radioCreditCard.setChecked(false);
                    radioDebitCard.setChecked(false);
                    radioWallet.setChecked(false);
                    setTotal();
                }
            }
        });
    }

    public void bindFees() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        MultiplePaymentAdapter paymentAdapter = new MultiplePaymentAdapter(this, _feeReminders);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(paymentAdapter);
    }

    public void setsubTotal(int total) {
        subTotalFees = total;
        subtotalAmount.setText(MessageFormat.format("₹ {0}", total));
        getConvenienceFee();
    }

    public void setTotal() {
        if (radioNetBanking.isChecked()) {
            convenienceFees = _convenienceFee.netBanking;
            _paymentMode = "netBanking";
        } else if (radioCreditCard.isChecked()) {
            convenienceFees = _convenienceFee.creditCard;
            _paymentMode = "creditCard";
        } else if (radioDebitCard.isChecked()) {
            convenienceFees = _convenienceFee.debitCard;
            _paymentMode = "debitCard";
        } else if (radioWallet.isChecked()) {
            convenienceFees = _convenienceFee.wallet;
            _paymentMode = "wallet";
        } else if (radioUpi.isChecked()) {
            convenienceFees = _convenienceFee.upi;
            _paymentMode = "upi";
        }

        totalFees = subTotalFees + convenienceFees;
        convenienceFeeAmount.setText(MessageFormat.format("₹ {0}", convenienceFees));
        totalAmount.setText(MessageFormat.format("₹ {0}", totalFees));
    }

    public void setFee(String id, Boolean isAdded) {
        if (isAdded)
            _feeIds.add(id);
        else
            _feeIds.remove(id);
    }

    public void getConvenienceFee() {
//        showProgressBar();
        PaymentApiController.getConvenienceFee(subTotalFees, new Callback<ConvenienceFee>() {
            @Override
            public void onResponse(Call<ConvenienceFee> call, Response<ConvenienceFee> response) {
                _convenienceFee = response.body();
                if(_convenienceFee == null) {
                    //todo - error
                    return;
                }
                netBankingFees.setText(MessageFormat.format("₹ {0}", _convenienceFee.netBankingLabel));
                creditCardFees.setText(MessageFormat.format("₹ {0}", _convenienceFee.creditCardLabel));
                debitCardFees.setText(MessageFormat.format("₹ {0}", _convenienceFee.debitCardLabel));
                walletFees.setText(MessageFormat.format("₹ {0}", _convenienceFee.walletLabel));
                upiFees.setText(MessageFormat.format("₹ {0}", _convenienceFee.upiLabel));
                setTotal();   
            }

            @Override
            public void onFailure(Call<ConvenienceFee> call, Throwable t) {
                //todo- error
            }
        });
    }

    @OnClick(R.id.btnNext)
    public void payMultipleFees() {
        if (subTotalFees == 0) {
            ToastHandler.showToast("Please select fees to be paid");
            return;
        }

        if (_feeIds.size() == 1) {
            for (Fee fee : _feeReminders.fees) {
                if (_feeIds.get(0).equals(fee._id)) {
                    feeCycle = fee.feeCycle.name;
                    break;
                }
            }
        } else
            feeCycle = _feeReminders.fees.get(0).organisationId.name;

        CartItem cartItem = new CartItem();
        cartItem.feeIds = _feeIds;
        cartItem.paymentMode = _paymentMode;
        cartItem.amount = totalFees;
        cartItem.organisationId = organisationId;
        cartItem.feeCycle = feeCycle;

        startPaymentProcess();
    }

    public void startPaymentProcess() {

//        if (Utils.getEmail() != null && !Utils.getEmail().isEmpty() && !Utils.getMobile().isEmpty() && Utils.getMobile() != null) {
//            addToCart();
//        }
//        else {
//            Intent intent = new Intent(getApplicationContext(), ContactDetailsActivity.class);
//            startActivity(intent);
//        }
    }

//    public void showDialogForFeeBreakdown(Fee fee) {
//        int total = 0;
//        FeeComponent feeComponent;
//        ArrayList<FeeComponent> feeComponents = fee.feeComponents;
//
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        final View dialogView = View.inflate(this, R.layout.dialog_fee_breakdown, null);
//
//        builder.setTitle("Fee breakdown")
//                .setView(dialogView)
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        final AlertDialog dialog = builder.create();
//
//        TableLayout table = (TableLayout) dialogView.findViewById(R.id.tableLayout);
//        assert table != null;
//        table.setStretchAllColumns(true);
//        table.bringToFront();
//
//        for (int i = 0; i < feeComponents.size(); i++) {
//            if (feeComponents.get(i).value > 0) {
//                TableRow tr = new TableRow(this);
//
//                TextView feeComponentName = new TextView(this);
//                TextView feeValue = new TextView(this);
//
//                feeComponentName.setPadding(15, 15, 15, 15);
//                DisplayMetrics metrics = getResources().getDisplayMetrics();
//                int width = metrics.widthPixels / 4;
//                feeComponentName.setMaxWidth(width);
//
//                feeValue.setPadding(15, 15, 15, 15);
//                feeValue.setGravity(Gravity.END);
//
//                feeComponent = feeComponents.get(i);
//                total = total + feeComponent.value;
//
//                feeComponentName.setText(feeComponent.schoolFeeComponent.name);
//                feeValue.setText(MessageFormat.format("₹ {0}", feeComponent.value));
//
//                feeComponentName.setAllCaps(false);
//                feeComponentName.setTextColor(getResources().getColor(R.color.theme_text_secondary));
//                feeValue.setTextColor(getResources().getColor(R.color.theme_text_secondary));
//                feeComponentName.setTypeface(Typeface.DEFAULT);
//                feeValue.setTypeface(Typeface.DEFAULT);
//
//                tr.addView(feeComponentName);
//                tr.addView(feeValue);
//                table.addView(tr);
//
//                View view = new View(this);
//                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));
//                view.setBackgroundColor(getResources().getColor(R.color.lightGrey));
//                table.addView(view);
//            }
//        }
//
//        if (fee.useLateFee) {
//            if (fee.lateFee > 0) {
//                TableRow tr = new TableRow(this);
//
//                TextView feeComponentName = new TextView(this);
//                TextView feeValue = new TextView(this);
//
//                feeComponentName.setPadding(15, 15, 15, 15);
//                DisplayMetrics metrics = getResources().getDisplayMetrics();
//                int width = metrics.widthPixels / 4;
//                feeComponentName.setMaxWidth(width);
//
//                feeValue.setPadding(15, 15, 15, 15);
//                feeValue.setGravity(Gravity.END);
//
//                feeComponentName.setText("Late Fees");
//                feeValue.setText(MessageFormat.format("+(₹ {0})", fee.lateFee));
//                total = total + fee.lateFee;
//
//                feeComponentName.setAllCaps(false);
//                feeComponentName.setTextColor(getResources().getColor(R.color.theme_text_secondary));
//                feeValue.setTextColor(getResources().getColor(R.color.theme_text_secondary));
//                feeComponentName.setTypeface(Typeface.DEFAULT);
//                feeValue.setTypeface(Typeface.DEFAULT);
//
//                tr.addView(feeComponentName);
//                tr.addView(feeValue);
//                table.addView(tr);
//
//                View view = new View(this);
//                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));
//                view.setBackgroundColor(getResources().getColor(R.color.lightGrey));
//                table.addView(view);
//            }
//        }
//
//        if (fee.status.equals("partial")) {
//            if ((fee.totalAmount - fee.netPayableAmount) > 0) {
//                TableRow tr = new TableRow(this);
//
//                TextView feeComponentName = new TextView(this);
//                TextView feeValue = new TextView(this);
//
//                feeComponentName.setPadding(15, 15, 15, 15);
//                DisplayMetrics metrics = getResources().getDisplayMetrics();
//                int width = metrics.widthPixels / 4;
//                feeComponentName.setMaxWidth(width);
//
//                feeValue.setPadding(15, 15, 15, 15);
//                feeValue.setGravity(Gravity.END);
//
//                feeComponentName.setText("Already paid (Partial)");
//                feeValue.setText(MessageFormat.format("-(₹ {0})", fee.totalAmount - fee.netPayableAmount));
//                total = total - (fee.totalAmount - fee.netPayableAmount);
//
//                feeComponentName.setAllCaps(false);
//                feeComponentName.setTextColor(getResources().getColor(R.color.theme_text_secondary));
//                feeValue.setTextColor(getResources().getColor(R.color.theme_text_secondary));
//                feeComponentName.setTypeface(Typeface.DEFAULT);
//                feeValue.setTypeface(Typeface.DEFAULT);
//
//                tr.addView(feeComponentName);
//                tr.addView(feeValue);
//                table.addView(tr);
//
//                View view = new View(this);
//                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));
//                view.setBackgroundColor(getResources().getColor(R.color.lightGrey));
//                table.addView(view);
//            }
//        }
//
//        TableRow tr = new TableRow(this);
//
//        TextView feeComponentName = new TextView(this);
//        TextView feeValue = new TextView(this);
//
//        feeComponentName.setPadding(15, 15, 15, 15);
//        DisplayMetrics metrics = getResources().getDisplayMetrics();
//        int width = metrics.widthPixels / 4;
//        feeComponentName.setMaxWidth(width);
//
//        feeValue.setPadding(15, 15, 15, 15);
//        feeValue.setGravity(Gravity.END);
//
//        feeComponentName.setText("Total");
//        feeValue.setText(MessageFormat.format("₹ {0}", total));
//
//        feeComponentName.setAllCaps(true);
//        feeComponentName.setTextColor(getResources().getColor(R.color.theme_text_primary));
//        feeValue.setTextColor(getResources().getColor(R.color.theme_text_primary));
//        feeComponentName.setTypeface(Typeface.DEFAULT_BOLD);
//        feeValue.setTypeface(Typeface.DEFAULT_BOLD);
//
//        tr.addView(feeComponentName);
//        tr.addView(feeValue);
//        table.addView(tr);
//
//        dialog.show();
//    }

}
