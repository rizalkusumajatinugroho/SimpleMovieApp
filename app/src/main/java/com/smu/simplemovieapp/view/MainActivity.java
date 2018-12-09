package com.smu.simplemovieapp.view;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.smu.simplemovieapp.R;
import com.smu.simplemovieapp.adapter.MovieAdapter;
import com.smu.simplemovieapp.db.DA_movie_header;
import com.smu.simplemovieapp.db.DA_search_history;
import com.smu.simplemovieapp.model.MovieHeader;
import com.smu.simplemovieapp.model.MovieResource;
import com.smu.simplemovieapp.model.SearchHistory;
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
    private static Bundle state = new Bundle();;
    private Parcelable mListState;
    private final String LIST_STATE_KEY = "list";
    private static final int REQUEST= 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        toolbar = findViewById(R.id.toolbar);
        listMovie = findViewById(R.id.movieList);

        toolbar.setTitle(getString(R.string.movie_list));
        setSupportActionBar(toolbar);

        checkPermissionManifest();

        presenter = new MainPresenter(this);
        linearLayoutManager = new LinearLayoutManager(this);

        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        listMovie.setLayoutManager(linearLayoutManager);

        movieAdapter = new MovieAdapter(movies, listMovie, this);
        listMovie.setAdapter(movieAdapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_menu, menu);

        MenuItem searchView = menu.findItem(R.id.search);
        MenuItem serviceDownload = menu.findItem(R.id.service);
        final Intent intent = new Intent(this, DownloadService.class);

        Switch mySwitch = serviceDownload.getActionView().findViewById(R.id.switchForActionBar);
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something based on isChecked
                if (isChecked){
                    startService(intent);
                }else {
                    stopService(intent);
                }
            }
        });

        SearchView searchViewActionBar = (SearchView) searchView.getActionView();

        searchViewActionBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                presenter.getMovie(query, MainActivity.this);
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mListState = listMovie.getLayoutManager().onSaveInstanceState();
        state.putParcelable(LIST_STATE_KEY, mListState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(state != null)
            mListState = state.getParcelable(LIST_STATE_KEY);
    }

    @Override
    public void showList(List<MovieHeader> movieResource) {
        movieAdapter.setLoaded();
        if (movieResource != null) {
            movies.clear();
            movies.addAll(movieResource);
            movieAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showMoreList(List<MovieHeader> movieResource) {
        movieAdapter.setLoaded();
        if (movieResource != null) {
            movies.remove(null);
            movies.addAll(movieResource);
            movieAdapter.notifyDataSetChanged();
        }
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
        presenter.getMoreMovie(this);
    }


    @Override
    public boolean isNetworkAvailable() {
        ConnectivityManager manager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            // Network is present and connected
            isAvailable = true;
        }
        return isAvailable;
    }



    @Override
    public void onResume(){
        super.onResume();
        if (mListState != null) {
            listMovie.getLayoutManager().onRestoreInstanceState(mListState);
        }
        linearLayoutManager = new LinearLayoutManager(this);

        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        listMovie.setLayoutManager(linearLayoutManager);

        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            listMovie.setLayoutManager(new GridLayoutManager(this, 1));
        }else{
            listMovie.setLayoutManager(new GridLayoutManager(this, 2));
        }

        movieAdapter = new MovieAdapter(movies, listMovie, this);
        listMovie.setAdapter(movieAdapter);


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            listMovie.setLayoutManager(new GridLayoutManager(this, 2));
            movieAdapter = new MovieAdapter(movies, listMovie, this);
            listMovie.setAdapter(movieAdapter);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            listMovie.setLayoutManager(new GridLayoutManager(this, 1));
            movieAdapter = new MovieAdapter(movies, listMovie, this);
            listMovie.setAdapter(movieAdapter);
        }
    }

    public void checkPermissionManifest(){
        if (Build.VERSION.SDK_INT >= 23) {
            Log.d("TAG","@@@ IN IF Build.VERSION.SDK_INT >= 23");
            String[] PERMISSIONS = {
                    android.Manifest.permission.INTERNET,
                    android.Manifest.permission.ACCESS_NETWORK_STATE

            };


            if (!hasPermissions(this, PERMISSIONS)) {
                Log.d("TAG","@@@ IN IF hasPermissions");
                ActivityCompat.requestPermissions((Activity) this, PERMISSIONS, REQUEST );
            } else {
                Log.d("TAG","@@@ IN ELSE hasPermissions");

            }
        } else {
            Log.d("TAG","@@@ IN ELSE  Build.VERSION.SDK_INT >= 23");

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("TAG","@@@ PERMISSIONS grant");

                } else {
                    Log.d("TAG","@@@ PERMISSIONS Denied");

                    Toast.makeText(this, "PERMISSIONS Denied", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
    }

    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}
