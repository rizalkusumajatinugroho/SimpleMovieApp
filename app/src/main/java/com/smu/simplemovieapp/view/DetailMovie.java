package com.smu.simplemovieapp.view;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.smu.simplemovieapp.R;
import com.smu.simplemovieapp.adapter.GenreAdapter;
import com.smu.simplemovieapp.adapter.MovieAdapter;
import com.smu.simplemovieapp.model.MovieDetail;
import com.smu.simplemovieapp.presenter.DetailPresenter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailMovie extends AppCompatActivity implements DetailView {
    private Toolbar toolbar;
    private String id = "", title = "", poster = "";
    private ImageView backdrop;
    private TextView tvTitle, tvYear, tvRelease, tvRated, tvDirector, tvWriter, tvActor, tvDesc;
    private DetailPresenter presenter;
    private RecyclerView rvGenre;
    private LinearLayoutManager linearLayoutManager;
    private GenreAdapter genreAdapter;
    private ArrayList<String> genre = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        toolbar = findViewById(R.id.toolbar);
        backdrop = findViewById(R.id.backdrop);
        tvDesc = findViewById(R.id.tvDescription);
        tvTitle = findViewById(R.id.tvTitle);
        tvYear = findViewById(R.id.tvYear);
        tvRelease = findViewById(R.id.tvRelease);
        tvRated = findViewById(R.id.tvRated);
        tvDirector = findViewById(R.id.tvDirector);
        tvWriter = findViewById(R.id.tvWriter);
        tvActor = findViewById(R.id.tvActor);
        rvGenre = findViewById(R.id.rvGenre);

        Intent intent = getIntent();
        id = intent.getStringExtra(getString(R.string.ex_id));
        title = intent.getStringExtra(getString(R.string.ex_title));
        poster = intent.getStringExtra(getString(R.string.ex_poster));


        toolbar.setTitle(title);
        setSupportActionBar(toolbar);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvGenre.setLayoutManager(linearLayoutManager);

        genreAdapter = new GenreAdapter(genre);
        rvGenre.setAdapter(genreAdapter);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null)
        {
            // Display home menu item.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Set collapsing tool bar title.
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(title);

        // Set collapsing tool bar image.
        ImageView collapsingToolbarImageView = (ImageView)findViewById(R.id.backdrop);
        Picasso.get().load(poster).fit().error(R.drawable.ic_broken_image).into(collapsingToolbarImageView);



        presenter = new DetailPresenter(this);

        presenter.getDetail(id);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        // When user click home menu item then quit this activity.
        if(itemId==android.R.id.home)
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void showDetail(MovieDetail movieDetail) {
        tvTitle.setText("Title :" + movieDetail.getTitle());
        tvYear.setText("Year : " + movieDetail.getYear());
        tvRelease.setText("Release : " + movieDetail.getReleased());
        tvRated.setText("Rated : " + movieDetail.getRated());
        tvDirector.setText("Director : " + movieDetail.getDirector());
        tvWriter.setText("Writer : " + movieDetail.getWriter());
        tvDesc.setText(movieDetail.getPlot());

        genre.clear();
        genre.addAll(movieDetail.getGenre());
        genreAdapter.notifyDataSetChanged();
    }

}
