package io.github.sher1234.service.api;

import java.util.Map;

import io.github.sher1234.service.model.response.Dashboard;
import io.github.sher1234.service.model.response.DashboardWR;
import io.github.sher1234.service.model.response.Registrations;
import io.github.sher1234.service.model.response.Responded;
import io.github.sher1234.service.model.response.ServiceCall;
import io.github.sher1234.service.model.response.Users;
import io.github.sher1234.service.model.response.UsersX;
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

    @POST("dashboard/")
    @FormUrlEncoded
    Call<Dashboard> GetDashboard(@Field("Email") String email,
                                 @Field("DateTime") String dateTime);

    @POST("query/?m=0")
    @FormUrlEncoded
    Call<Registrations> GetRegistrationsUser(@FieldMap Map<String, String> map);

    @POST("set/?m=0")
    @FormUrlEncoded
    Call<Responded> RegisterCall(@FieldMap Map<String, String> map);

    @POST("set/?m=1")
    @FormUrlEncoded
    Call<Responded> RegisterVisit(@FieldMap Map<String, String> map);

    @POST("set/?m=2")
    @FormUrlEncoded
    Call<Responded> RegisterVisitStart(@FieldMap Map<String, String> map);

    @POST("set/?m=3")
    @FormUrlEncoded
    Call<Responded> RegisterVisitEnd(@FieldMap Map<String, String> map);

    @POST("get/call/")
    @FormUrlEncoded
    Call<ServiceCall> GetServiceCall(@Field("CallNumber") String s);

    @POST("admin/?m=0")
    @FormUrlEncoded
    Call<Dashboard> GetDashboard(@Field("DateTime") String dateTime);

    @POST("query/?m=0")
    @FormUrlEncoded
    Call<UsersX> GetUsersAdmin(@FieldMap Map<String, String> map);

    @POST("admin/?m=1")
    @FormUrlEncoded
    Call<DashboardWR> GetDashboardWR(@Field("Email") String email,
                                     @Field("DateTime") String dateTime);
}