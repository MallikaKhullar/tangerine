package com.trial.edupay.Network;

import com.trial.edupay.Model.Organisation;
import com.trial.edupay.Model.Student;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

/**
 * Created by mallikapriyakhullar on 23/12/17.
 */

public interface OrganizationApi {

    @GET("/api/v1/organisations/{schoolId}/students/ByAdmissionId/{admissionId}")
    void getStudentByAdmissionId(
            @Header("Authorization") String authorization,
            @Path("schoolId") String schoolId,
            @Path("admissionId") String admissionId,
            Callback<Student> cb
    );

    @GET("/api/v1/organisations")
    Call<ArrayList<Organisation>> getOrganisations();


    @GET("/api/v1/organisations/{schoolId}/students/ByAdmissionId/{admissionId}/pendingFees")
    void getFeeDetails(
            @Path("schoolId") String schoolId,
            @Path("admissionId") String admissionId
//            ,Callback<ArrayList<Fee>> cb
    );
}