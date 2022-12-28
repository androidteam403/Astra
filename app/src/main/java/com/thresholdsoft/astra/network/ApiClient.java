package com.thresholdsoft.astra.network;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor;
import com.thresholdsoft.astra.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    //
//    public static ApiInterface getApiService(String baseUrl) {
//        return getRetrofitInstance(baseUrl).create(ApiInterface.class);
//    }
    private static Retrofit retrofit = null;


    public static Retrofit getRetrofitInstance() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(new OkHttpProfilerInterceptor());
        }
        OkHttpClient client = builder
                .connectTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)//"https://online.apollopharmacy.org/Digital/Apollo/AHL/"
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
    }
}
