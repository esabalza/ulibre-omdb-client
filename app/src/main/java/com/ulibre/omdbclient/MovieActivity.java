package com.ulibre.omdbclient;

import android.os.Bundle;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;
import com.ulibre.omdbclient.Adapter.MovieAdapter;
import com.ulibre.omdbclient.Model.Movie;
import com.ulibre.omdbclient.Service.OMDBService;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MovieActivity extends AppCompatActivity {


    private OMDBService mOMDBService;
    private TextView mMovieTitle;
    private TextView mMovieYear;
    private TextView mMoviePlot;
    private ImageView mMoviePoster;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setup();
        load();
    }

    /**
     * Setup
     */
    private void setup() {
        this.mOMDBService = new OMDBService(this, "8be0d4b1");

        this.mMoviePlot = (TextView)findViewById(R.id.moviePlot);
        this.mMoviePoster = (ImageView) findViewById(R.id.moviePoster);
        this.mMovieYear = (TextView)findViewById(R.id.movieYear);
        this.mMovieTitle = (TextView)findViewById(R.id.movieTitle);

    }


    public void load(){
        final String movieId = getIntent().getStringExtra("id");

        this.mOMDBService.findById(movieId, new OMDBService.OMDBSingleMovieResponse() {
            @Override
            public void onResponse(Movie movie) {

                mMoviePlot.setText(movie.getPlot());

                Picasso.get()
                        .load(movie.getPoster())
                        .placeholder(R.drawable.ic_movie_black_24dp)
                        .error(R.drawable.ic_movie_black_24dp)
                        .into(mMoviePoster);

                mMovieYear.setText(movie.getYear());
                mMovieTitle.setText(movie.getTitle());


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
    }



}
