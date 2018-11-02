package io.github.sher1234.service;

import android.app.Application;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import io.github.sher1234.service.functions.TaskUser;
import io.github.sher1234.service.model.base.User;
import io.github.sher1234.service.util.Strings;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppController extends Application {

    private static AppController instance;

    @Contract(pure = true)
    public static synchronized AppController getInstance() {
        return instance;
    }

    private static User getUserFromPrefs() {
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

    private static void removeUserFromPrefs() {
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

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        if (getUserFromPrefs() != null && getUserFromPrefs().isExists())
            new TaskUser().onMigrateUser(getUserFromPrefs());
        removeUserFromPrefs();
    }

    @NonNull
    public Retrofit getRetrofitRequest(@NotNull String url) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout((long) 90, TimeUnit.SECONDS)
                .writeTimeout((long) 210, TimeUnit.SECONDS)
                .connectTimeout((long) 50, TimeUnit.SECONDS)
                .build();
        return new Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}
