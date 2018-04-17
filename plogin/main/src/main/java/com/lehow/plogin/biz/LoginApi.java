package com.lehow.plogin.biz;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LoginApi {

  @FormUrlEncoded @POST("lawyer/login") Flowable<UserEntity> login(
      @Field("user") String phoneNum, @Field("pwd") String password);
}
