package com.trial.edupay.Modules.Notices;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trial.edupay.Controller.NotificationApiController;
import com.trial.edupay.MainApplication;
import com.trial.edupay.Model.Message;
import com.trial.edupay.R;
import com.trial.edupay.Utils.BasicStringCallback;
import com.trial.edupay.Utils.EmptyState;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NoticesFragment extends Fragment {

    /** VIEWS */
    @BindView(R.id.swipeRefresh) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rvAllEntities) RecyclerView rvAllNotifications;
    @BindView(R.id.empty_state) View viewEmptyState;

    /** DATA */
    ArrayList<Message> _allMessages = new ArrayList();

    /** CONTROLLERS */
    UiController uiController;
    DataController dataController;

    public NoticesFragment() {} // Required empty public constructor

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
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
            NotificationApiController.getNotifications(new Callback<ArrayList<Message>>() {
                @Override
                public void onResponse(Call<ArrayList<Message>> call, Response<ArrayList<Message>> response) {
                    ArrayList<Message> messages = response.body();
                    if( messages == null || messages.isEmpty()) {
                        uiController.refreshUI(EmptyState.EMPTY);
                    }
                    _allMessages.clear();
                    _allMessages.addAll(messages);
                    uiController.refreshUI(EmptyState.NONE);
                    uiController.notifyAdapter();
                }

                @Override
                public void onFailure(Call<ArrayList<Message>> call, Throwable t) {
                    uiController.refreshUI(EmptyState.ERROR);
                }
            });
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
            rvAllNotifications.setVisibility(View.GONE);
            switch(state){
                case EMPTY: break;
                case ERROR: break;
            }
        }

        void hideEmptyState(){
            viewEmptyState.setVisibility(View.GONE);
            rvAllNotifications.setVisibility(View.VISIBLE);
        }

        void notifyAdapter() {
            NotificationsAdapter adapter = (NotificationsAdapter) rvAllNotifications.getAdapter();
            if(adapter == null) {
                adapter = new NotificationsAdapter(_allMessages, new BasicStringCallback() {
                    @Override
                    public void onSuccess(String response) {
                        Intent intent = new Intent(getActivity(), MessageDisplayActivity.class);
                        intent.putExtra("message", response);
                        getActivity().startActivity(intent);
                    }

                    @Override
                    public void onError() {

                    }
                });


                rvAllNotifications.setAdapter(adapter);
            }
            adapter.notifyDataSetChanged();
            rvAllNotifications.setLayoutManager(new LinearLayoutManager(getActivity()));
        }

        void setupUI(){

            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    dataController.pullData();
                }
            });
            swipeRefreshLayout.setBackgroundColor(ContextCompat.getColor(MainApplication.getAppContext(),R.color.white));
            swipeRefreshLayout.setRefreshing(true);
        }
    }
}

