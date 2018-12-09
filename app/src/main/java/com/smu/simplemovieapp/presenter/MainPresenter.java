package com.smu.simplemovieapp.presenter;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.smu.simplemovieapp.db.DA_movie_header;
import com.smu.simplemovieapp.db.DA_search_history;
import com.smu.simplemovieapp.model.MovieHeader;
import com.smu.simplemovieapp.model.MovieResource;
import com.smu.simplemovieapp.model.SearchHistory;
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

    public void insertHistory(String query, int page, int numPage, Context context) {
        DA_search_history da_search_history = new DA_search_history(context);

        SearchHistory check = da_search_history.getValueById(query);
        if (check == null || check.getFlagDone() == null ||check.getFlagDone().equals("")) {
            SearchHistory searchHistory = new SearchHistory();
            searchHistory.setQuery(query);
            searchHistory.setPage(String.valueOf(page));
            searchHistory.setNumPage(String.valueOf(numPage));
            searchHistory.setFlagDone("");
            int inserted = da_search_history.insert_or_replace(searchHistory);
            Log.d("debug_rizal", "inserted : " + inserted + " ;query : " + query);
        }
    }

    public MovieResource getMovieOffline(String query, int page, Context context){
        MovieResource rtn = new MovieResource();
        int dataLimit = page;
        String offset = "1";
        if (dataLimit > 1){
            offset = ((dataLimit - 1) * 10) + "";
        }
        DA_movie_header da_movie_header = new DA_movie_header(context);

        rtn.setSearch(da_movie_header.getListSearch(query, offset));
        rtn.setTotalResults(da_movie_header.countListSearch(query));
        return rtn;
    }

    public void getMovie(final String search, final Context context){
        if (mainView.isNetworkAvailable()){
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
                                insertHistory(search, page, numPages, context);
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
        }else{
            if (search.length() > 2) {
                initPage(search);
                MovieResource movieResource = new MovieResource();
                movieResource = getMovieOffline(search, page, context);
                List<MovieHeader> movies = movieResource.getSearch();
                if (movies != null && movies.size() > 0){
                    setPagination(movieResource);
                    insertHistory(search, page, numPages, context);
                    page++;
                    if (page < numPages){
                        movies.add(null);
                    }
                    mainView.showList(movies);
                }

            }
        }
    }

    public void getMoreMovie(Context context){
        if (page < numPages){
            if (mainView.isNetworkAvailable()){
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
            }else{
                MovieResource movieResource = new MovieResource();
                movieResource = getMovieOffline(search, page, context);
                List<MovieHeader> movies = movieResource.getSearch();
                Log.d("debug_rizal", "load more 10 : " + movies.size() + "; numpage : " + numPages + "; page : " + page);
                if (movies != null && movies.size() > 0){
                    page++;
                    if (page < numPages){
                        movies.add(null);
                    }
                    mainView.showMoreList(movies);
                }

            }

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
