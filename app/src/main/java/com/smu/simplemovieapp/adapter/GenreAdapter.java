package com.smu.simplemovieapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smu.simplemovieapp.R;

import java.util.ArrayList;

/**
 * Created by sapuser on 12/8/2018.
 */

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.MyViewHolder> {
    private ArrayList<String> listGenre;
    public GenreAdapter(ArrayList<String> listGenre){
        this.listGenre = listGenre;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.genre_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String genre = listGenre.get(position);
        holder.tvGenre.setText(genre);
    }

    @Override
    public int getItemCount() {
        return listGenre.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tvGenre;


        public MyViewHolder(View itemView) {
            super(itemView);

            tvGenre = itemView.findViewById(R.id.tvGenre);

        }
    }
}
