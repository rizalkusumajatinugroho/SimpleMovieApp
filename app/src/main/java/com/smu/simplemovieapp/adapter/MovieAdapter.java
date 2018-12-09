package com.smu.simplemovieapp.adapter;

import android.media.Image;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smu.simplemovieapp.R;
import com.smu.simplemovieapp.model.MovieHeader;
import com.smu.simplemovieapp.view.MainView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by sapuser on 12/8/2018.
 */

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private List<MovieHeader> listMovie;
    private MainView mainView;
    private final int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private final int VIEW_LOAD = 0;
    private final int VIEW_ITEM = 1;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tvTitle, tvYear, tvType;
        private ImageView ivPoster;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvYear = itemView.findViewById(R.id.tvYear);
            tvType = itemView.findViewById(R.id.tvGenre);
            ivPoster = itemView.findViewById(R.id.ivPoster);
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {

        LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }

    public MovieAdapter(List<MovieHeader> listMovie, RecyclerView rv_movies, final MainView mainView){
        this.listMovie = listMovie;
        this.mainView = mainView;

        if (rv_movies.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) rv_movies.getLayoutManager();
            rv_movies.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager
                            .findLastVisibleItemPosition();


                    if (!mainView.isNetworkAvailable())loading = false;
                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        mainView.onLoadMore();

                        loading = true;
                    }
                }
            });
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list, parent, false);
            return new MyViewHolder(itemView);
        }else{
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_item, parent, false);
            return new LoadingViewHolder(itemView);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_ITEM) {
            final MovieHeader movieHeader = listMovie.get(position);
            final MyViewHolder movieViewHolder = (MyViewHolder) holder;
            movieViewHolder.tvTitle.setText(movieHeader.getTitle());
            movieViewHolder.tvYear.setText(movieHeader.getYear());
            movieViewHolder.tvType.setText(movieHeader.getType());
            Picasso.get().load(movieHeader.getPoster()).fit().error(R.drawable.ic_broken_image).into(movieViewHolder.ivPoster);

            movieViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainView.onItemClick(movieHeader);
                }
            });
        }

    }

    public void setLoaded() {
        loading = false;
    }


    @Override
    public int getItemCount() {
        return listMovie == null ? 0 : listMovie.size();
    }

    @Override
    public int getItemViewType(int position) {
        return listMovie.get(position) == null ? VIEW_LOAD : VIEW_ITEM;
    }
}
