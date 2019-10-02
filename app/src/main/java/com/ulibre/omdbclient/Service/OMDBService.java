package com.ulibre.omdbclient.Service;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ulibre.omdbclient.Model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OMDBService {

    private Context context;
    private String key;
    private RequestQueue requestQueue;


    /**
     * Consume el OMDB WebService
     * @param context
     */
    public OMDBService(Context context, String key){
        this.context = context;
        this.key = key;
        this.setup();
    }

    private void setup() {

        // Instantiate the cache
        Cache cache = new DiskBasedCache(this.context.getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        requestQueue = new RequestQueue(cache, network);

        // Start the queue
        requestQueue.start();
    }

    /**
     * GET http://www.omdbapi.com/?i=
     * @param id
     * @param singleMovieResponse
     * @param errorListener
     */
    public void findById(String id, final OMDBSingleMovieResponse singleMovieResponse, Response.ErrorListener errorListener){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://www.omdbapi.com/?i="+id+"&apikey=" + this.key,  null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject movieJsonObject) {
                Movie movie = new Movie();

                try {
                    movie.setId(movieJsonObject.getString("imdbID"));
                    movie.setTitle(movieJsonObject.getString("Title"));
                    movie.setYear(movieJsonObject.getString("Year"));
                    movie.setPoster(movieJsonObject.getString("Poster"));

                    movie.setGenre(movieJsonObject.getString("Genre"));
                    movie.setRated(movieJsonObject.getString("Rated"));
                    movie.setRuntime(movieJsonObject.getString("Runtime"));
                    movie.setPlot(movieJsonObject.getString("Plot"));


                    singleMovieResponse.onResponse(movie);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, errorListener);
        this.requestQueue.add(jsonObjectRequest);
    }

    /**
     * GET http://www.omdbapi.com/?s=
     * @param query
     * @param errorListener
     */
    public void searchByTitle(String query, final OMDBMovieListResponse responseListener, Response.ErrorListener errorListener){

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://www.omdbapi.com/?s=" + query + "&apikey=" + this.key, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray movies = response.getJSONArray("Search");
                    ArrayList<Movie> movieArrayList = new ArrayList<>();

                    for(int i = 0; i < movies.length(); i++){

                        // Build object list
                        JSONObject movieJsonObject = movies.getJSONObject(i);
                        Movie movie = new Movie();

                        movie.setId(movieJsonObject.getString("imdbID"));
                        movie.setTitle(movieJsonObject.getString("Title"));
                        movie.setYear(movieJsonObject.getString("Year"));
                        movie.setPoster(movieJsonObject.getString("Poster"));
                        movieArrayList.add(movie);
                    }

                    responseListener.onResponse(movieArrayList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, errorListener);

        this.requestQueue.add(jsonObjectRequest);
    }

    public interface OMDBMovieListResponse{
        void onResponse(ArrayList<Movie> movies);
    }

    public interface OMDBSingleMovieResponse{
        void onResponse(Movie movie);
    }

}
