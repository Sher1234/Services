package io.github.sher1234.service;

import android.app.Application;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import io.github.sher1234.service.model.base.User;
import io.github.sher1234.service.util.Strings;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppController extends Application {

    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    @Contract(pure = true)
    public static synchronized AppController getInstance() {
        return mInstance;
    }

    @NonNull
    public static Retrofit getRetrofit(@NotNull String url) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout((long) 1, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .writeTimeout((long) 5, TimeUnit.MINUTES)
                .build();
        return new Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

    }

    public static void setUserInPrefs(@NotNull User user) {
        SharedPreferences preferences = getInstance().
                getSharedPreferences(Strings.UserPreferences, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(Strings.Status, true);
        editor.putString(Strings.Name, user.Name);
        editor.putString(Strings.Email, user.Email);
        editor.putString(Strings.Phone, user.Phone);
        editor.putString(Strings.Password, user.Password);
        editor.putBoolean(Strings.IsAdmin, user.isAdmin());
        editor.putBoolean(Strings.Exists, user.isExists());
        editor.putString(Strings.EmployeeID, user.EmployeeID);
        editor.putBoolean(Strings.IsRegistered, user.isRegistered());
        editor.apply();
    }

    public static User getUserFromPrefs() {
        SharedPreferences preferences = getInstance().
                getSharedPreferences(Strings.UserPreferences, MODE_PRIVATE);
        User user = new User();
        user.Name = preferences.getString(Strings.Name, null);
        user.Email = preferences.getString(Strings.Email, null);
        user.Phone = preferences.getString(Strings.Phone, null);
        user.setAdmin(preferences.getBoolean(Strings.IsAdmin, false));
        user.Password = preferences.getString(Strings.Password, null);
        user.EmployeeID = preferences.getString(Strings.EmployeeID, "");
        user.setRegistered(preferences.getBoolean(Strings.IsRegistered, false));
        return user;
    }

    public static void removeUserFromPrefs() {
        SharedPreferences preferences = getInstance().
                getSharedPreferences(Strings.UserPreferences, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(Strings.Name);
        editor.remove(Strings.Email);
        editor.remove(Strings.Phone);
        editor.remove(Strings.IsAdmin);
        editor.remove(Strings.Password);
        editor.remove(Strings.EmployeeID);
        editor.remove(Strings.IsRegistered);
        editor.apply();
    }
}
