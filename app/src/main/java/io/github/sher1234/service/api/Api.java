package io.github.sher1234.service.api;

import java.util.Map;

import io.github.sher1234.service.model.response.AccountJson;
import io.github.sher1234.service.model.response.Calls;
import io.github.sher1234.service.model.response.Dashboard;
import io.github.sher1234.service.model.response.Employees;
import io.github.sher1234.service.model.response.Responded;
import io.github.sher1234.service.model.response.Services;
import io.github.sher1234.service.model.response.Users;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

@SuppressWarnings("all")
public interface Api {
    String BASE_URL = "https://sher-sc.000webhostapp.com/v4/";

    @GET("excel/{start}/{end}")
    Call<ResponseBody> onDownloadFile(@Path(value = "start", encoded = true) String start,
                                      @Path(value = "end", encoded = true) String end);

    @POST("user/login/null")
    @FormUrlEncoded
    Call<AccountJson> onLoginUser(@Field("Email") String email,
                                  @Field("Password") String password);

    @POST("user/register/null")
    @FormUrlEncoded
    Call<AccountJson> onRegisterUser(@FieldMap Map<String, String> map);

    @POST("user/delete/null")
    @FormUrlEncoded
    Call<Responded> onDeleteUser(@Field("UserID") String id,
                                 @Field("Email") String email);

    @POST("user/update/password")
    @FormUrlEncoded
    Call<AccountJson> onChangePassword(@FieldMap Map<String, String> map);

    @POST("user/update/detail")
    @FormUrlEncoded
    Call<AccountJson> onUpdateUser(@FieldMap Map<String, String> map);

    @POST("user/update/reg")
    @FormUrlEncoded
    Call<Responded> setUserRegisteration(@Field("UserID") String id,
                                         @Field("Value") String value);

    @POST("user/update/admin")
    @FormUrlEncoded
    Call<Responded> setUserPrevilige(@Field("UserID") String id,
                                     @Field("Value") String value);

    @POST("dashboard/admin/board")
    @FormUrlEncoded
    Call<Dashboard> getAdminBoard(@Field("Date") String date);

    @POST("dashboard/user/null")
    @FormUrlEncoded
    Call<Dashboard> getUserBoard(@Field("ID") String userId, @Field("Date") String date);

    @POST("dashboard/admin/user")
    @FormUrlEncoded
    Call<Dashboard> getUserAdmin(@Field("ID") String userId, @Field("Date") String date);

    @POST("query/get/null")
    @FormUrlEncoded
    Call<Calls> getQueryCalls(@FieldMap Map<String, String> map);

    @POST("query/get/null")
    @FormUrlEncoded
    Call<Employees> getQueryEmployees(@FieldMap Map<String, String> map);

    @POST("query/get/null")
    @FormUrlEncoded
    Call<Users> getQueryUsers(@FieldMap Map<String, String> map);

    @POST("call/get/null")
    @FormUrlEncoded
    Call<Services> getFullCall(@Field("CallID") String s);

    @POST("call/add/null")
    @FormUrlEncoded
    Call<Responded> onAddCall(@FieldMap Map<String, String> map);

    @POST("visit/add/full")
    @FormUrlEncoded
    Call<Responded> onAddVisit(@FieldMap Map<String, String> map);

    @POST("visit/add/start")
    @FormUrlEncoded
    Call<Responded> onStartVisit(@FieldMap Map<String, String> map);

    @POST("visit/add/end")
    @FormUrlEncoded
    Call<Responded> onEndVisit(@FieldMap Map<String, String> map);

    @POST("call/allot/null")
    @FormUrlEncoded
    Call<Responded> onAllotCall(@Field("CallID") String callID, @Field("ID2") String id2);

    @POST("visit/edit/null")
    @FormUrlEncoded
    Call<Responded> onEditVisit(@FieldMap Map<String, String> map);

    @POST("call/edit/null")
    @FormUrlEncoded
    Call<Responded> onEditCall(@FieldMap Map<String, String> map);

    @POST("visit/delete/null")
    @FormUrlEncoded
    Call<Responded> onDeleteVisit(@Field("VisitID") String id);

    @POST("call/delete/null")
    @FormUrlEncoded
    Call<Responded> onDeleteCall(@Field("CallID") String id);

    @POST("sign/visit/{number}")
    @Multipart
    Call<Responded> onSignVisit(@Path(value = "number", encoded = true) String number,
                                @Part MultipartBody.Part file);
}