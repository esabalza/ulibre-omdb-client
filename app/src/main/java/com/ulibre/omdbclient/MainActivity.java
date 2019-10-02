package com.ulibre.omdbclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ulibre.omdbclient.Adapter.MovieAdapter;
import com.ulibre.omdbclient.Model.Movie;
import com.ulibre.omdbclient.Service.OMDBService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private OMDBService mOMDBService;
    private EditText mSearchField;
    private Handler mSearchDelay;
    public RecyclerView mMovieRecyclerView;
    public MovieAdapter mMovieAdapter;
    public List<Movie> mMovieList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setup();
        this.load();
        this.listen();
    }

    private void listen() {

        mSearchField.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(mSearchDelay != null){
                    mSearchDelay.removeCallbacksAndMessages(null);
                }

                mSearchDelay = new Handler();
                mSearchDelay.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        search(mSearchField.getText().toString());
                    }
                }, 500);


                Log.d("Key", keyCode + "");
                if(keyCode == 66){
                    hideKeyboard(MainActivity.this);
                }

                return false;
            }
        });
    }


    /**
     *
     * @param query
     */
    private void search(String query) {
        this.mOMDBService.searchByTitle(query, new OMDBService.OMDBMovieListResponse() {
            @Override
            public void onResponse(ArrayList<Movie> newMovies) {

                for(Movie movie : newMovies){
                    Log.d("Movie", movie.getTitle());
                }

                mMovieList.clear();
                mMovieList.addAll(newMovies);
                mMovieAdapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
    }

    /**
     * Setup API Wrapper
     */
    private void setup() {
        this.mOMDBService = new OMDBService(this, "8be0d4b1");
        mSearchField = (EditText) this.findViewById(R.id.search);
        mMovieRecyclerView = (RecyclerView) this.findViewById(R.id.movieList);
        mMovieList = new ArrayList<>();
        mMovieAdapter = new MovieAdapter(this, mMovieList);

        mMovieRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mMovieRecyclerView.setAdapter(mMovieAdapter);
    }

    public void load(){
        search("Galaxy");
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
