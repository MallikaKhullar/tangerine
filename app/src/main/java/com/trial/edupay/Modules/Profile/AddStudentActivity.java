package com.trial.edupay.Modules.Profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.trial.edupay.Controller.DataSourceControl;
import com.trial.edupay.Controller.LoginApiController;
import com.trial.edupay.Controller.StudentApiController;
import com.trial.edupay.Model.Organisation;
import com.trial.edupay.Model.Student;
import com.trial.edupay.Model.User;
import com.trial.edupay.Modules.Base.BaseActionBarActivity;
import com.trial.edupay.Modules.Base.ContainerActivity;
import com.trial.edupay.R;
import com.trial.edupay.Utils.BasicCallback;
import com.trial.edupay.Utils.TextValidator;
import com.trial.edupay.Utils.ToastHandler;
import com.trial.edupay.Utils.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mallikapriyakhullar on 08/01/18.
 */

public class AddStudentActivity extends BaseActionBarActivity {

    @BindView(R.id.progress) View progress;
    @BindView(R.id.studentDivider) View viewStudentDivider;
    @BindView(R.id.schoolName) AutoCompleteTextView actvSchoolName;
    @BindView(R.id.admissionNo) EditText etAdmissionNo;
    @BindView(R.id.searchStudent) TextView tvSearch;
    @BindView(R.id.addStudent) TextView tvSubmit;
    @BindView(R.id.studentName) TextView tvStudentName;

    String searchedSchoolId;
    String searchedAdmId;

    String selectedStudentId;
    String selectedStudentName;

    private ArrayList<Organisation> _organisations = new ArrayList<>();
    private ArrayList<String> organisationNames = new ArrayList<>();

    UiController uiController;
    DataController dataController;

    Boolean isLoggedIn;

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
        setContentView(R.layout.activity_add_student);
        isLoggedIn = getIntent().getBooleanExtra("isLoggedIn", false);
        uiController = new UiController();
        dataController = new DataController();
        uiController.setupUI();
        setupActionBar();
        dataController.fetchOrganizationsList(new BasicCallback() {
            @Override
            public void onSuccess() {
                uiController.refreshList();
            }

            @Override
            public void onError() {
                //handle failure of loading list of colleges
                ToastHandler.showToast("Could not load universities");
                if(AddStudentActivity.this != null) AddStudentActivity.this.finish();
            }
        });
    }


    /**
     * The Data Controller handles fetching and submitting to & fro the network
     * Directly accesses the network controllers
     * Can't touch the UI
     */
    private class DataController {

        void triggerDBAdd() {
            StudentApiController.addStudent(selectedStudentId, new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    LoginApiController.getMe(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            User user = response.body();
                            Utils.saveUser(user);
                            DataSourceControl.setUserState(DataSourceControl.UserState.LOGGED_IN);
                            uiController.childAdded();
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            ToastHandler.showToast("Couldn't add student, please try again");
                        }
                    });
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    ToastHandler.showToast("Couldn't add student, please try again");
                }
            });
        }

        void triggerDBSearch(final BasicCallback callback) {

            StudentApiController.getStudentByAdmissionId(searchedSchoolId, searchedAdmId, new Callback<Student>() {
                @Override public void onResponse(Call<Student> call, Response<Student> response) {

                    if (response.body() == null) {
                        callback.onError(); return;
                    }

                    selectedStudentId = response.body().admissionId;
                    selectedStudentName = response.body().name;
                    callback.onSuccess();
                }

                @Override public void onFailure(Call<Student> call, Throwable t) {
                    callback.onError();
                }
            });
        }

        void fetchOrganizationsList(final BasicCallback callback) {
            organisationNames.clear();
            StudentApiController.getOrganizations(new Callback<ArrayList<Organisation>>() {
                @Override
                public void onResponse(Call<ArrayList<Organisation>> call, Response<ArrayList<Organisation>> response) {
                    _organisations.addAll(response.body());

                    for (Organisation organisation : _organisations)
                        organisationNames.add(organisation.name);

                    callback.onSuccess();
                }

                @Override
                public void onFailure(Call<ArrayList<Organisation>> call, Throwable t) {
                    callback.onError();
                }
            });
        }
    }

    /**
     * The UI Controller handles updating of the UI
     * Directly accesses the UI
     * Can't access the network controllers
     */
    private class UiController {

        View.OnClickListener searchListener = new View.OnClickListener() {
            @Override public void onClick(View v) {initiateSearch();}
        };

        View.OnClickListener addStudentListener = new View.OnClickListener() {
            @Override public void onClick(View v) {initiateAdd();}
        };

        void childAdded() {
            ToastHandler.showToast("Student added");
            if(isLoggedIn){
                if(AddStudentActivity.this != null) AddStudentActivity.this.finish();
            } else {
                startActivity(new Intent(AddStudentActivity.this,ContainerActivity.class).putExtra("isLoggedIn", true));
                if(AddStudentActivity.this != null) AddStudentActivity.this.finish();
            }
        }

        void refreshList() {
            //update the UI with the list of schools
            ArrayAdapter<String> adapter = new ArrayAdapter<>(AddStudentActivity.this, android.R.layout.simple_list_item_1, organisationNames);
            actvSchoolName.setAdapter(adapter);
        }

        void refreshSubmitUI() {
            //Step 1: disable the loader
            progress.setVisibility(View.GONE);

            //Step 2: show searched student
            tvStudentName.setVisibility(View.VISIBLE);
            tvStudentName.setText("Student: " + selectedStudentName);
            viewStudentDivider.setVisibility(View.VISIBLE);

            //Step 3: allow him to submit this student to DB under his user
            tvSubmit.setVisibility(View.VISIBLE);
        }

        void setupUI(){
            //bind the view using butterknife
            ButterKnife.bind(AddStudentActivity.this);

            //setup listeners
            tvSubmit.setOnClickListener(addStudentListener);
            tvSearch.setOnClickListener(searchListener);

            //clear all errors if he starts typing again
            etAdmissionNo.addTextChangedListener(new TextValidator(etAdmissionNo) {
                @Override public void validate(TextView textView, String text) {clearErrors();}
            });
            actvSchoolName.addTextChangedListener(new TextValidator(actvSchoolName) {
                @Override public void validate(TextView textView, String text) {clearErrors();}
            });

            //register listeners for the 'done' action button on the keyboard
            etAdmissionNo.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        initiateSearch();
                        return true;
                    }
                    return false;
                }
            });
            actvSchoolName.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        initiateSearch();
                        return true;
                    }
                    return false;
                }
            });
        }

        void clearErrors(){
            etAdmissionNo.setError(null);
            actvSchoolName.setError(null);
        }

        void initiateSearch() {
            hideKeyboard(AddStudentActivity.this, etAdmissionNo);

            //Step 1: check validity of admission number
            if (etAdmissionNo.getText() == null || etAdmissionNo.getText().toString().isEmpty()) {
                //invalid admNo
                etAdmissionNo.setError("Please enter a valid admission number");
                return;
            } else {
                //valid admNo
                searchedAdmId = etAdmissionNo.getText().toString();
            }

            //Step 2: check validity of organization name
            for (Organisation organisation : _organisations) {
                if (organisation.name.equals(actvSchoolName.getText().toString())) {
                    //valid school
                    searchedSchoolId = organisation._id;
                    break;
                }
            }

            if(searchedSchoolId == null) {
                //invalid school
                actvSchoolName.setError("Please select a valid school");
                return;
            }

            //Step 3: since all are valid, show loading progress
            progress.setVisibility(View.VISIBLE);

            //Step 4: since all are valid, trigger the adding of the student to the DB
            dataController.triggerDBSearch(new BasicCallback() {
                @Override
                public void onSuccess() {
                    refreshSubmitUI();
                }

                @Override
                public void onError() {
                    progress.setVisibility(View.GONE);
                    ToastHandler.showToast("No student with these credentials, please try again");
                }
            });
        }

        void initiateAdd(){
            //Step 1: Check if student has been searched
            if(selectedStudentId == null || selectedStudentName == null) {
                ToastHandler.showToast("Please select a student first");
                return;
            }

            //Step 2: Trigger add to DB
            dataController.triggerDBAdd();
        }

        public void hideKeyboard(Context context, View view) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
