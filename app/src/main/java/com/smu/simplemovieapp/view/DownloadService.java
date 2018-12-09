package com.smu.simplemovieapp.view;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.smu.simplemovieapp.db.DA_movie_detail;
import com.smu.simplemovieapp.db.DA_movie_header;
import com.smu.simplemovieapp.db.DA_search_history;
import com.smu.simplemovieapp.model.MovieDetail;
import com.smu.simplemovieapp.model.MovieHeader;
import com.smu.simplemovieapp.model.MovieResource;
import com.smu.simplemovieapp.model.SearchHistory;
import com.smu.simplemovieapp.rest.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sapuser on 12/9/2018.
 */

public class DownloadService extends Service {

    private static final String DEBUG_TAG = "DownloadServ";
    private DownloaderTask downloaderTask;
    private DA_search_history da_search_history;
    private DA_movie_header da_movie_header;
    private DA_movie_detail da_movie_detail;
    private List<MovieHeader> movies = new ArrayList<>();
    private SearchHistory searchHistory = new SearchHistory();
    private boolean isRunning = true;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        da_search_history = new DA_search_history(getBaseContext());
        da_movie_header = new DA_movie_header(getBaseContext());
        da_movie_detail = new DA_movie_detail(getBaseContext());

        downloaderTask = new DownloaderTask();
        downloaderTask.execute();
        isRunning = true;
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {

        if (downloaderTask !=null) {
            if(!downloaderTask.isCancelled()) {
                downloaderTask.cancel(true);
            }
        }
        isRunning = false;
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class DownloaderTask extends AsyncTask<String, Void, Boolean> {

        private static final String DEBUG_TAG = "DownloadService";

        @Override
        protected Boolean doInBackground(String... params) {
            initiateDownload();
            return true;
        }

        public void initiateDownload(){
            if (isRunning){
                searchHistory = da_search_history.getValue();
                Log.d(DEBUG_TAG, searchHistory.getQuery() + " ; page : " + searchHistory.getPage() + "; numpage :  " + searchHistory.getNumPage());
                if (searchHistory.getQuery() != null && !searchHistory.getQuery().equals("")) {
                    new ServiceGenerator().getMovie(searchHistory.getQuery(), Integer.parseInt(searchHistory.getPage()), new Callback<MovieResource>() {
                        @Override
                        public void onResponse(Call<MovieResource> call, Response<MovieResource> response) {
                            Log.d(DEBUG_TAG, searchHistory.getPage() + "");
                            movies = response.body().getSearch();
                            da_movie_header.insert_or_replace(movies);
                            getDetailMovie(0);
                        }

                        @Override
                        public void onFailure(Call<MovieResource> call, Throwable t) {

                        }
                    });
                }else {
                    initiateDownload();
                }
            }

        }

        public void getDetailMovie(final int index){
            if (movies != null && movies.get(index) != null){
                new ServiceGenerator().getDetail(movies.get(index).getImdbID(), new Callback<MovieDetail>() {
                    @Override
                    public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {

                        int insertedDetail = da_movie_detail.insert_or_replace(response.body());
                        Log.d(DEBUG_TAG, "detail : " + insertedDetail + " ; title : " + response.body().getTitle());
                        if (index == movies.size()-1){
                            String numPages = searchHistory.getNumPage();
                            String page = searchHistory.getPage();

                            if (page.equals(numPages)){
                                da_search_history.updateFlagDone(searchHistory.getQuery());
                            }else{
                                int pagePlus = Integer.parseInt(searchHistory.getPage()) + 1;
                                searchHistory.setPage(String.valueOf(pagePlus));
                                da_search_history.insert_or_replace(searchHistory);
                            }

                                initiateDownload();
                        }else{
                            int indexPlus = index + 1;
                            getDetailMovie(indexPlus);
                        }

                    }

                    @Override
                    public void onFailure(Call<MovieDetail> call, Throwable t) {

                    }
                });
            }


        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }
    }

}
