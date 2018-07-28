package io.github.sher1234.service.api;

import java.util.Map;

import io.github.sher1234.service.model.base.Responded;
import io.github.sher1234.service.model.response.Dashboard;
import io.github.sher1234.service.model.response.Employeez;
import io.github.sher1234.service.model.response.Registrations;
import io.github.sher1234.service.model.response.ServiceCall;
import io.github.sher1234.service.model.response.UserR;
import io.github.sher1234.service.model.response.Users;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

@SuppressWarnings("all")
public interface Api {
    String BASE_URL = "https://sher-sc.000webhostapp.com/v3/";

    @POST("user/login/null")
    @FormUrlEncoded
    Call<UserR> Login(@Field("Email") String email,
                      @Field("Password") String password);

    @POST("user/register/null")
    @FormUrlEncoded
    Call<UserR> Register(@Field("Email") String email,
                         @Field("Password") String password,
                         @Field("EmployeeID") String employee_id);

    @POST("user/reset/request")
    @FormUrlEncoded
    Call<Responded> ResetRequest(@Field("Email") String email,
                                 @Field("EmployeeID") String employee_id);

    @POST("user/reset/password")
    @FormUrlEncoded
    Call<Users> ResetPassword(@Field("Otp") String otp,
                              @Field("Email") String email,
                              @Field("Password") String password);

    @POST("user/email/null")
    @FormUrlEncoded
    Call<Users> GetUserData(@Field("Email") String email);

    @POST("user/update/password")
    @FormUrlEncoded
    Call<UserR> ChangePassword(@Field("Email") String id,
                               @Field("Password") String password,
                               @Field("NewPassword") String newPassword);

    @POST("user/update/account")
    @FormUrlEncoded
    Call<UserR> UpdateAccount(@FieldMap Map<String, String> map);

    @POST("user/update/register")
    @FormUrlEncoded
    Call<Responded> ChangeAccountState(@Field("Email") String email, @Field("Value") String value);

    @POST("user/update/admin")
    @FormUrlEncoded
    Call<Responded> ChangeAccountPrivilege(@Field("Email") String email, @Field("Value") String value);

    @POST("user/delete/null")
    @FormUrlEncoded
    Call<Responded> DeleteAccount(@Field("Email") String email);

    @POST("dashboard/admin/board")
    @FormUrlEncoded
    Call<Dashboard> GetAdminDash(@Field("DateTime") String dateTime);

    @POST("dashboard/user/null")
    @FormUrlEncoded
    Call<Dashboard> GetUserDash(@Field("Email") String email,
                                @Field("DateTime") String dateTime,
                                @Field("EmployeeID") String employeeID);

    @POST("query/get/null")
    @FormUrlEncoded
    Call<Registrations> GetQueryRegs(@FieldMap Map<String, String> map);

    @POST("query/get/null")
    @FormUrlEncoded
    Call<Employeez> GetQueryEmployees(@FieldMap Map<String, String> map);

    @POST("query/get/null")
    @FormUrlEncoded
    Call<Users> GetQueryUsers(@FieldMap Map<String, String> map);

    @POST("dashboard/admin/user")
    @FormUrlEncoded
    Call<Dashboard> GetUserDashAdmin(@Field("Email") String email,
                                     @Field("DateTime") String dateTime,
                                     @Field("EmployeeID") String employeeID);

    @POST("get/call/null")
    @FormUrlEncoded
    Call<ServiceCall> GetCall(@Field("CallNumber") String s);

    @POST("set/register/null")
    @FormUrlEncoded
    Call<Responded> RegisterCall(@FieldMap Map<String, String> map);

    @POST("set/visit/full")
    @FormUrlEncoded
    Call<Responded> RegisterVisit(@FieldMap Map<String, String> map);

    @POST("set/visit/start")
    @FormUrlEncoded
    Call<Responded> StartVisit(@FieldMap Map<String, String> map);

    @POST("set/visit/end")
    @FormUrlEncoded
    Call<Responded> EndVisit(@FieldMap Map<String, String> map);

    @POST("set/allot/null")
    @FormUrlEncoded
    Call<Responded> AllotCall(@Field("CallNumber") String callNumber,
                              @Field("AllottedTo") String allottedTo);

    @POST("edit/visit/null")
    @FormUrlEncoded
    Call<Responded> EditVisit(@FieldMap Map<String, String> map);

    @POST("edit/register/null")
    @FormUrlEncoded
    Call<Responded> EditCall(@FieldMap Map<String, String> map);

    @POST("edit/delete/visit")
    @FormUrlEncoded
    Call<Responded> DeleteVisit(@Field("VisitNumber") String visitNumber);

    @POST("edit/delete/register")
    @FormUrlEncoded
    Call<Responded> DeleteCall(@Field("CallNumber") String callNumber);

    @POST("sign/{number}/null")
    @Multipart
    Call<Responded> SignVisit(@Path(value = "number", encoded = true) String number,
                              @Part MultipartBody.Part file);
}