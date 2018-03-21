package com.trial.edupay.Controller;

import android.util.Log;

import com.trial.edupay.Model.Fee;
import com.trial.edupay.Model.Organisation;
import com.trial.edupay.Model.Student;
import com.trial.edupay.Model.Transaction;
import com.trial.edupay.Network.RetroClient;
import com.trial.edupay.Utils.Constants;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mallikapriyakhullar on 25/12/17.
 */

public class StudentApiController {

    public static void getFeeReminders(final Callback<ArrayList<Fee>> cb) {
        RetroClient
                .getUserApi()
                .getFeeReminders(Constants.getBearerKey())
                .enqueue(new Callback<ArrayList<Fee>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Fee>> call, Response<ArrayList<Fee>> response) {
                        Log.d("RESPONSE: REMINDERS", response.isSuccessful() + response.raw().toString());

                        if (response.isSuccessful()) {
                            cb.onResponse(call, response);
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Fee>> call, Throwable t) {
                        Log.d("RESPONSE: REMINDERS", t.getLocalizedMessage());
                        cb.onFailure(call, t);
                    }
                });
    }

    public static void getOrganizations(final Callback<ArrayList<Organisation>> cb) {
        RetroClient
                .getOrganizationApi()
                .getOrganisations()
                .enqueue(new Callback<ArrayList<Organisation>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Organisation>> call, Response<ArrayList<Organisation>> response) {
                        Log.d("RESPONSE: REMINDERS", response.isSuccessful() + response.raw().toString());

                        if (response.isSuccessful()) {
                            cb.onResponse(call, response);
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Organisation>> call, Throwable t) {
                        Log.d("RESPONSE: REMINDERS", t.getLocalizedMessage());
                        cb.onFailure(call, t);
                    }
                });
    }

    public static void getTransactions(final Callback<ArrayList<Transaction>> cb) {
        RetroClient
                .getUserApi()
                .getMyTransactions(Constants.getBearerKey())
                .enqueue(new Callback<ArrayList<Transaction>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Transaction>> call, Response<ArrayList<Transaction>> response) {
                        Log.d("RESPONSE: TRANSACTION", response.isSuccessful() + response.raw().toString());
                        if (response.isSuccessful()) {
                            cb.onResponse(call, response);
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Transaction>> call, Throwable t) {
                        Log.d("RESPONSE: TRANSACTION", t.getLocalizedMessage());
                        cb.onFailure(call, t);
                    }
                });
    }

    public static void getStudentByAdmissionId(String schoolId, String admId, final Callback<Student> cb) {
        RetroClient
                .getStudentApi()
                .getStudentByAdmissionId(Constants.getBearerKey(), schoolId, admId)
                .enqueue(new Callback<Student>() {
                    @Override
                    public void onResponse(Call<Student> call, Response<Student> response) {
                        Log.d("RESPONSE: TRANSACTION", response.isSuccessful() + response.raw().toString());
                        if (response.isSuccessful()) {
                            cb.onResponse(call, response);
                        } else {
                            cb.onFailure(call, null);
                        }
                    }

                    @Override
                    public void onFailure(Call<Student> call, Throwable t) {
                        Log.d("RESPONSE: TRANSACTION", t.getLocalizedMessage());
                        cb.onFailure(call, t);
                    }
                });
    }

    public static void addStudent(String studentId, final Callback cb) {
        RetroClient
                .getStudentApi()
                .addStudent(Constants.getBearerKey(), studentId)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.d("RESPONSE: TRANSACTION", response.isSuccessful() + response.raw().toString());
                        if (response.isSuccessful()) {
                            cb.onResponse(call, response);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d("RESPONSE: TRANSACTION", t.getLocalizedMessage());
                        cb.onFailure(call, t);
                    }
                });

    }
}
