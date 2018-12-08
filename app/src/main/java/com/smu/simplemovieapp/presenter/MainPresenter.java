package com.smu.simplemovieapp.presenter;


import android.widget.Toast;

import com.smu.simplemovieapp.model.MovieHeader;
import com.smu.simplemovieapp.model.MovieResource;
import com.smu.simplemovieapp.rest.ServiceGenerator;
import com.smu.simplemovieapp.view.MainView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sapuser on 12/8/2018.
 */

public class MainPresenter  {
    private MainView mainView;
    private int page = 1;
    private int numPages = 2;
    private String search = "";

    public MainPresenter(MainView mainView){
        this.mainView = mainView;
    }

    public void getMovie(String search){
        if (search.length() > 2){
            initPage(search);
            mainView.showLoading();
            new ServiceGenerator().getMovie(search, page, new Callback<MovieResource>() {
                @Override
                public void onResponse(Call<MovieResource> call, Response<MovieResource> response) {
                    mainView.dismissLoading();
                    if (response.code() == 200){
                        if (response.body().getResponse().equalsIgnoreCase("true")){
                            List<MovieHeader> movies = response.body().getSearch();
                            setPagination(response.body());
                            page++;
                            if (page < numPages){
                                movies.add(null);
                            }
                            mainView.showList(movies);
                        }

                    }
                }

                @Override
                public void onFailure(Call<MovieResource> call, Throwable t) {
                    mainView.dismissLoading();
                }
            });
        }



    }

    public void getMoreMovie(){
        if (page < numPages){

            new ServiceGenerator().getMovie(search, page, new Callback<MovieResource>() {
                @Override
                public void onResponse(Call<MovieResource> call, Response<MovieResource> response) {

                    if (response.code() == 200){
                        if (response.body().getResponse().equalsIgnoreCase("true")){
                            List<MovieHeader> movies = response.body().getSearch();
                            page++;
                            if (page < numPages){
                                movies.add(null);
                            }
                            mainView.showMoreList(movies);
                        }

                    }
                }

                @Override
                public void onFailure(Call<MovieResource> call, Throwable t) {

                }
            });
        }



    }

    private void initPage(String search) {
        page = 1;
        numPages = 2;
        this.search = search;
    }

    private void setPagination(MovieResource movieList) {
        numPages = movieList.getTotalResults() / 10;
        if (movieList.getTotalResults() % 10 != 0) {
            numPages++;
        }
    }


}
