package com.smu.simplemovieapp.rest;



import com.smu.simplemovieapp.model.MovieDetail;
import com.smu.simplemovieapp.model.MovieResource;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by sapuser on 9/17/2017.
 */

public interface MovieClient {
    @GET("http://www.omdbapi.com/")
    Call<MovieResource> getMovie(@Query("apikey") String apikey, @Query("s") String s, @Query("page") int page);

    @GET("http://www.omdbapi.com/")
    Call<MovieDetail> getDetail(@Query("apikey") String apikey, @Query("i") String i);


}
