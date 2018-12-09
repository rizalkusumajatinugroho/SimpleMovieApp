package com.smu.simplemovieapp.presenter;

import android.content.Context;

import com.smu.simplemovieapp.db.DA_movie_detail;
import com.smu.simplemovieapp.model.MovieDetail;
import com.smu.simplemovieapp.rest.ServiceGenerator;
import com.smu.simplemovieapp.view.DetailView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sapuser on 12/8/2018.
 */

public class DetailPresenter {
    private DetailView detailView;

    public DetailPresenter(DetailView detailView){
        this.detailView = detailView;
    }

    public void getDetail(String id, Context context){
        if (detailView.isNetworkAvailable()){
            new ServiceGenerator().getDetail(id, new Callback<MovieDetail>() {
                @Override
                public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {

                    detailView.showDetail(response.body());
                }

                @Override
                public void onFailure(Call<MovieDetail> call, Throwable t) {

                }
            });
        }else{
            DA_movie_detail da_movie_detail = new DA_movie_detail(context);
            MovieDetail movieDetail = new MovieDetail();
            movieDetail = da_movie_detail.getDetail(id);
            detailView.showDetail(movieDetail);
        }

    }
}
