package com.smu.simplemovieapp.rest;


import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.smu.simplemovieapp.model.MovieDetail;
import com.smu.simplemovieapp.model.MovieResource;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sapuser on 9/17/2017.
 */

public class ServiceGenerator {
    private final String API_BASE_URL = "http://www.omdbapi.com/";
    private final String API_KEY = "d5f7aabb";
    private MovieClient serviceClass;
    private String username=null;
    private String password=null;


    private static OkHttpClient.Builder httpClient = null;
    static Gson gson = new GsonBuilder()
            .setLenient()
            .create();


    public ServiceGenerator(){

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create(gson));



        if(httpClient == null){
            httpClient = new OkHttpClient.Builder();

            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    Request.Builder requestBuilder = original.newBuilder()
                            //.header("Authorization", basic)
                            .header("Accept", "application/json")
                            .method(original.method(), original.body());

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
        }
       // if (username != null && password != null) {
//            String credentials = username + ":" + password;
//            final String basic =
//                    "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

       // }

        OkHttpClient client = httpClient.build();

        Retrofit retrofit = builder.client(client).build();
        this.serviceClass=retrofit.create(MovieClient.class);
    }

    public void getMovie(String search, int page, Callback<MovieResource> callback){
        Call<MovieResource> call=serviceClass.getMovie(API_KEY, search, page);
        call.enqueue(callback);
    }

    public void getDetail(String id, Callback<MovieDetail> callback){
        Call<MovieDetail> call=serviceClass.getDetail(API_KEY, id);
        call.enqueue(callback);
    }


}
