package com.trial.edupay.Network;

import com.trial.edupay.Model.Student;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by mallikapriyakhullar on 23/12/17.
 */

public interface StudentApi {

    @GET ("/api/v1/students")
    void getFeeDetailsFromLink (
            @Query ("shortCode") String shortCode,
            @Query ("studentId") String studentId
//            ,Callback<ArrayList<Fee>> cb
    );

    @FormUrlEncoded
    @POST("/api/v1/users/me/students")
    Call<ResponseBody> addStudent(
            @Header("Authorization") String authorization,
            @Field("studentId") String studentId
    );


    @GET("/api/v1/organisations/{schoolId}/students/ByAdmissionId/{admissionId}")
    Call<Student> getStudentByAdmissionId(
            @Header("Authorization") String authorization,
            @Path("schoolId") String schoolId,
            @Path("admissionId") String admissionId
    );

//    @FormUrlEncoded
//    @POST("/api/v1/users/me/students/remove")
//    void deleteChild(
//            @Header("Authorization") String authorization,
//            @Field("studentId") String studentId,
//            Callback<Response> cb
//    );
}
