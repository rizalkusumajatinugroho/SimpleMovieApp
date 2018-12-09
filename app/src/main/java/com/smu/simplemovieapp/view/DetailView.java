package com.smu.simplemovieapp.view;

import com.smu.simplemovieapp.model.MovieDetail;

/**
 * Created by sapuser on 12/8/2018.
 */

public interface DetailView {

    public void showDetail(MovieDetail movieDetail);
    public boolean isNetworkAvailable();
}
