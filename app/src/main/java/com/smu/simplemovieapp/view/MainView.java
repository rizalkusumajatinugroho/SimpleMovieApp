package com.smu.simplemovieapp.view;

import com.smu.simplemovieapp.model.MovieHeader;
import com.smu.simplemovieapp.model.MovieResource;

import java.util.List;

/**
 * Created by sapuser on 12/8/2018.
 */

public interface MainView {
    public void showToast(String message);
    public void showList(List<MovieHeader> movieResource);
    public void showMoreList(List<MovieHeader> movieResource);
    public void showLoading();
    public void dismissLoading();
    public void onItemClick(MovieHeader movieHeader);
    public void onLoadMore();
}
