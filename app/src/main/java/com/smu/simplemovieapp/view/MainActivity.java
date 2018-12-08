package com.smu.simplemovieapp.view;

import android.app.Dialog;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.smu.simplemovieapp.R;
import com.smu.simplemovieapp.adapter.MovieAdapter;
import com.smu.simplemovieapp.db.DA_movie_header;
import com.smu.simplemovieapp.model.MovieHeader;
import com.smu.simplemovieapp.model.MovieResource;
import com.smu.simplemovieapp.presenter.MainPresenter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainView {
    private Toolbar toolbar;
    private MainPresenter presenter;
    private RecyclerView listMovie;
    private LinearLayoutManager linearLayoutManager;
    private MovieAdapter movieAdapter;
    private List<MovieHeader> movies = new ArrayList<>();
    private Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        listMovie = findViewById(R.id.movieList);

        toolbar.setTitle(getString(R.string.movie_list));
        setSupportActionBar(toolbar);

        presenter = new MainPresenter(this);



        linearLayoutManager = new LinearLayoutManager(this);
        listMovie.setLayoutManager(linearLayoutManager);

        movieAdapter = new MovieAdapter(movies, listMovie, this);
        listMovie.setAdapter(movieAdapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_menu, menu);

        MenuItem searchView = menu.findItem(R.id.search);
        SearchView searchViewActionBar = (SearchView) searchView.getActionView();

        searchViewActionBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                presenter.getMovie(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showList(List<MovieHeader> movieResource) {
        movieAdapter.setLoaded();
        if (movieResource != null) {
            insertDataHeader(movieResource);
            movies.clear();
            movies.addAll(movieResource);
            movieAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showMoreList(List<MovieHeader> movieResource) {
        movieAdapter.setLoaded();
        if (movieResource != null) {
            insertDataHeader(movieResource);
            movies.remove(null);
            movies.addAll(movieResource);
            movieAdapter.notifyDataSetChanged();
        }
    }

    public void insertDataHeader(List<MovieHeader> movieResource){
        DA_movie_header da_movie_header = new DA_movie_header(this);
        da_movie_header.insert_or_replace(movieResource);
    }

    @Override
    public void showLoading() {
        dialog  = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_loading);
        ImageView gifImageView = dialog.findViewById(R.id.custom_loading_imageView);
        Picasso.get().load(R.drawable.loading).fit().error(R.drawable.ic_broken_image).into(gifImageView);
        dialog.show();

    }

    @Override
    public void dismissLoading() {
        dialog.dismiss();
    }

    @Override
    public void onItemClick(MovieHeader movieHeader) {
        Intent intent = new Intent(MainActivity.this, DetailMovie.class);
        intent.putExtra(getString(R.string.ex_id), movieHeader.getImdbID());
        intent.putExtra(getString(R.string.ex_title), movieHeader.getTitle());
        intent.putExtra(getString(R.string.ex_poster), movieHeader.getPoster());
        startActivity(intent);
    }

    @Override
    public void onLoadMore() {
        presenter.getMoreMovie();
    }


}
