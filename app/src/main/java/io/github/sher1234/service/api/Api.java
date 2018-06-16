package io.github.sher1234.service.api;

import java.util.Map;

import io.github.sher1234.service.model.response.Users;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {
    String BASE_URL = "https://sher-sc.000webhostapp.com/v2/";

    @POST("login/?m=0")
    @FormUrlEncoded
    Call<Users> Login(@Field("Email") String email,
                      @Field("Password") String password);

    @POST("login/?m=1")
    @FormUrlEncoded
    Call<Users> Register(@Field("Email") String email,
                         @Field("Password") String password);

    @POST("login/?m=2")
    @FormUrlEncoded
    Call<Users> ResetRequest(@Field("Email") String email);

    @POST("login/?m=3")
    @FormUrlEncoded
    Call<Users> ResetPassword(@Field("Otp") String otp,
                              @Field("Email") String email,
                              @Field("Password") String password);

    @POST("login/?m=4")
    @FormUrlEncoded
    Call<Users> GetUserData(@Field("Email") String email);

    @POST("login/?m=5")
    @FormUrlEncoded
    Call<Users> ChangePassword(@Field("Email") String email,
                               @Field("Password") String password,
                               @Field("NewPassword") String newPassword);

    @POST("login/?m=6")
    @FormUrlEncoded
    Call<Users> UpdateAccount(@FieldMap Map<String, String> map);
}