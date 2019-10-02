package com.ulibre.omdbclient.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.ulibre.omdbclient.Model.Movie;
import com.ulibre.omdbclient.MovieActivity;
import com.ulibre.omdbclient.R;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {


    private List<Movie> movies;
    private LayoutInflater mInflater;
    private Context context;

    public MovieAdapter(Context context, List<Movie> movies){
        this.context = context;
        this.movies = movies;
        this.mInflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.movie_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Movie movie = movies.get(position);

        holder.mTitle.setText(movie.getTitle());
        holder.mYear.setText(movie.getYear());

        Picasso.get()
                .load(movie.getPoster())
                .error(R.drawable.ic_movie_black_24dp)
                .into(holder.mPoster);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MovieActivity.class);
                intent.putExtra("id", movie.getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        ImageView mPoster;
        TextView mTitle;
        TextView mYear;

        ViewHolder(View itemView) {
            super(itemView);

            mPoster = itemView.findViewById(R.id.moviePoster);
            mTitle = itemView.findViewById(R.id.movieTitle);
            mYear = itemView.findViewById(R.id.movieYear);

            itemView.setOnClickListener(this);

        }

        public void setItem(Movie item) {

        }


        @Override
        public void onClick(View v) {

        }
    }

}
