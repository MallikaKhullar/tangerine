package com.trial.edupay.Modules.Current;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trial.edupay.Controller.StudentApiController;
import com.trial.edupay.Model.Fee;
import com.trial.edupay.Model.FeeReminders;
import com.trial.edupay.Modules.Payment.FeeDetailActivity;
import com.trial.edupay.R;
import com.trial.edupay.Utils.EmptyState;
import com.trial.edupay.Utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemindersFragment extends Fragment {

    /**
     * VIEWS
     */
    @BindView(R.id.swipeRefresh) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rvAllEntities) RecyclerView rvAllReminders;
    @BindView(R.id.empty_state) View viewEmptyState;

    /**
     * DATA
     */
    ArrayList<Fee> _allFeeEntities = new ArrayList();

    /**
     * CONTROLLERS
     */
    UiController uiController;
    DataController dataController;

    public RemindersFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        void pullData() {
            StudentApiController.getFeeReminders(new Callback<ArrayList<Fee>>() {
                @Override
                public void onResponse(Call<ArrayList<Fee>> call, Response<ArrayList<Fee>> response) {
                    inflateList(response.body()); //pull the server response into local list
                    sortData();
                    uiController.notifyAdapter();
                    uiController.refreshUI(( Utils.dataNotFound(_allFeeEntities)) ? EmptyState.EMPTY : EmptyState.NONE);
                }

                @Override
                public void onFailure(Call<ArrayList<Fee>> call, Throwable t) {
                    _allFeeEntities.clear();
                    uiController.refreshUI(EmptyState.ERROR);
                }
            });
        }

        void inflateList(ArrayList<Fee> allFeeEntities) {
            _allFeeEntities.clear();
            for (Fee fee : allFeeEntities) {
                if (fee.netPayableAmount > 0) {
                    _allFeeEntities.add(fee);
                }
            }
        }

        void sortData() {
            Collections.sort(_allFeeEntities, new Comparator<Fee>() {
                @Override
                public int compare(Fee fee1, Fee fee2) {
                    return fee1.feeCycle.lastDate.isAfter(fee2.feeCycle.lastDate)
                            ? -1 : (fee1.feeCycle.lastDate.isBefore(fee2.feeCycle.lastDate)) ? 1 : 0;
                }
            });
        }
    }

    /**
     * UI Controller handles update of Fragment UI:
     * 1) Refreshing the UI after data fetch
     * 2) Showing or Hiding the empty state
     */
    private class UiController {

        void refreshUI(EmptyState state) {
            //stop the "refreshing loader"
            swipeRefreshLayout.setRefreshing(false);

            //show or hide empty state based on data
            if (state.equals(EmptyState.NONE)) {
                hideEmptyState();
            } else {
                showEmptyState(state);
            }
        }

        void showEmptyState(EmptyState state) {
            viewEmptyState.setVisibility(View.VISIBLE);
            rvAllReminders.setVisibility(View.GONE);
            switch (state) {
                case EMPTY:
                    break;
                case ERROR:
                    break;
            }
        }

        void hideEmptyState() {
            viewEmptyState.setVisibility(View.GONE);
            rvAllReminders.setVisibility(View.VISIBLE);
        }

        void notifyAdapter() {
            ReminderAdapter adapter = (ReminderAdapter) rvAllReminders.getAdapter();
            if (adapter == null) {
                adapter = new ReminderAdapter(_allFeeEntities, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), FeeDetailActivity.class);
                        intent.putExtra("feeReminders", new FeeReminders(_allFeeEntities).toJson());
                        getActivity().startActivity(intent);
                    }
                });
                rvAllReminders.setAdapter(adapter);
            }
            adapter.notifyDataSetChanged();
            rvAllReminders.setLayoutManager(new LinearLayoutManager(getActivity()));
        }

        void setupUI() {
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    dataController.pullData();
                }
            });
        }
    }
}