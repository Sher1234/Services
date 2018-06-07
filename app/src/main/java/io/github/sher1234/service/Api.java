package io.github.sher1234.service;

import io.github.sher1234.service.model.response.Users;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {

    //String BASE_URL = "http://sher-sc.000webhostapp.com/api-v2/";

    String BASE_URL = "http://sher-sc.000webhostapp.com/test/";

    @POST("api-login-func.php?m=u-e")
    @FormUrlEncoded
    Call<Users> CheckUserEmail(@Field("Email") String email);


    @POST("api-login-func.php?m=u-l")
    @FormUrlEncoded
    Call<Users> LoginUser(@Field("Email") String email,
                          @Field("Password") String password);

    @POST("api-login-func.php?m=u-rp")
    @FormUrlEncoded
    Call<Users> ResetPassword(@Field("Email") String email,
                              @Field("Password") String password);

    @POST("api-login-func.php?m=u-rp-r")
    @FormUrlEncoded
    Call<Users> ResetPasswordRequest(@Field("Email") String email);
}