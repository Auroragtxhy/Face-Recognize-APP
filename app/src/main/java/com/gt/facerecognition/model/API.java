package com.gt.facerecognition.model;

import com.gt.facerecognition.model.domain.FaceRecognition;
import com.gt.facerecognition.model.domain.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface API {

    /* 登录 */
    @POST("login")
    Call<User> loginToAccount(@Query("account") String account, @Query("password") String password);

    /* 获取所有用户的人脸识别信息 */
    @GET("getFaceInfo")
    Call<FaceRecognition> getFaceInfo();

    /* 获取未知用户的信息 */
    @GET("getUnknownInfo")
    Call<FaceRecognition> getUnknownInfo();

    /* 获取用户的人脸识别信息 */
    @GET("getUserInfo")
    Call<FaceRecognition> getUserInfo(@Query("person") String person);

}
