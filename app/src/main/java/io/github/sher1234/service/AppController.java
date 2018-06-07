package io.github.sher1234.service;

import android.app.Application;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.Contract;

import java.util.concurrent.TimeUnit;

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
    public static Retrofit getRetrofit(String url,  long connectTimeOut,
                                       long readTimeOut, long writeTimeOut) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(connectTimeOut, TimeUnit.MINUTES)
                .readTimeout(readTimeOut, TimeUnit.MINUTES)
                .writeTimeout(writeTimeOut, TimeUnit.MINUTES)
                .build();
        return new Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

    }

    @NonNull
    public static Retrofit getRetrofit(String url) {
        return getRetrofit(url, 1, 2, 5);
    }
}
