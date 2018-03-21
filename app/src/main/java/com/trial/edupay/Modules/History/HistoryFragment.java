package com.trial.edupay.Modules.History;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trial.edupay.Controller.StudentApiController;
import com.trial.edupay.Model.CompactTransactionItem;
import com.trial.edupay.Model.Transaction;
import com.trial.edupay.Model.TransactionComponent;
import com.trial.edupay.R;
import com.trial.edupay.Utils.EmptyState;
import com.trial.edupay.Utils.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryFragment extends Fragment {

    /** VIEWS */
    @BindView(R.id.swipeRefresh) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rvAllEntities) RecyclerView rvAllTransactions;
    @BindView(R.id.empty_state) View viewEmptyState;

    /** DATA */
    ArrayList<CompactTransactionItem> _allTransactions = new ArrayList();

    /** CONTROLLERS */
    UiController uiController;
    DataController dataController;

    public HistoryFragment() {} // Required empty public constructor


    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_with_rv, null);
        ButterKnife.bind(this, rootView);
        uiController = new UiController();
        dataController = new DataController();
        uiController.setupUI();
        dataController.pullData();
        return rootView;
    }


    /**
     * The data controller class handles all of the data needs for this fragment:
     * 1) Pulling from the server
     * 2) Sorting
     * 3) Inflating member variables for the data
     */
    private class DataController {
        void pullData(){
            StudentApiController.getTransactions(new Callback<ArrayList<Transaction>>() {
                @Override
                public void onResponse(Call<ArrayList<Transaction>> call, Response<ArrayList<Transaction>> response) {
                    inflateList(response.body()); //pull the server response into local list
                    uiController.notifyAdapter();
                    uiController.refreshUI(Utils.dataNotFound(_allTransactions) ? EmptyState.EMPTY : EmptyState.NONE);
                }

                @Override
                public void onFailure(Call<ArrayList<Transaction>> call, Throwable t) {
                    _allTransactions.clear();
                    uiController.refreshUI(EmptyState.ERROR); //update the UI with server results
                }
            });
        }


        CompactTransactionItem buildTransaction(Transaction transaction, TransactionComponent component) {
            CompactTransactionItem compactTransactionItem = new CompactTransactionItem();
            compactTransactionItem.date = transaction.date;
            compactTransactionItem.deleted = transaction.deleted;
            compactTransactionItem.mode = transaction.mode;
            compactTransactionItem.paymentDetails = transaction.paymentDetails;
            compactTransactionItem.remarks = transaction.remarks;
            compactTransactionItem.userId = transaction.userId;
            compactTransactionItem.component = component;
            compactTransactionItem.id = transaction._id;
            return compactTransactionItem;
        }

        void inflateList(ArrayList<Transaction> allFeeEntities){
            _allTransactions.clear();
            for (Transaction transaction : allFeeEntities) {
                for (TransactionComponent component : transaction.components) {
                    _allTransactions.add(buildTransaction(transaction, component));
                }
            }
        }
    }

    private class UiController {

        void refreshUI(EmptyState state) {
            //stop the "refreshing loader"
            swipeRefreshLayout.setRefreshing(false);

            //show or hide empty state based on data
            if (state.equals(EmptyState.NONE)) hideEmptyState();
            else showEmptyState(state);
        }

        void showEmptyState(EmptyState state) {
            viewEmptyState.setVisibility(View.VISIBLE);
            rvAllTransactions.setVisibility(View.GONE);
            switch(state){
                case EMPTY: break;
                case ERROR: break;
            }
        }

        void hideEmptyState(){
            viewEmptyState.setVisibility(View.GONE);
            rvAllTransactions.setVisibility(View.VISIBLE);
        }

        void notifyAdapter() {
            TransactionAdapter adapter = (TransactionAdapter) rvAllTransactions.getAdapter();
            if(adapter == null) {
                adapter = new TransactionAdapter(_allTransactions);
                rvAllTransactions.setAdapter(adapter);
            }
            adapter.notifyDataSetChanged();
            rvAllTransactions.setLayoutManager(new LinearLayoutManager(getActivity()));
        }

        void setupUI(){
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    dataController.pullData();
                }
            });
        }
    }
}