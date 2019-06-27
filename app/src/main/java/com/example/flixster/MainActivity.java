package com.example.flixster;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.flixster.models.Config;
import com.example.flixster.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {



    //base url for api
    public final static String API_BASE_URL = "https://api.themoviedb.org/3";
    //param name for the api key
    public final static String API_KEY_PARAM = "api_key";
    //api key

    //tag for logging from activity
    public final static String TAG = "MainActivity";

    //instance fields
    AsyncHttpClient client;

    //base url for loading images
    String imageBaseUrl;
    //poster size
    String posterSize;
    // list of currently playing movies
    ArrayList<Movie> movies;
    //recycler view
    RecyclerView rvMovies;
    //adapter wired to recycler view
    MovieAdapter adapter;
    //image config

    Config config;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //init client
        client = new AsyncHttpClient();
        //inir movie list
        movies = new ArrayList<>();
        //init adapter - movies cannot be changed after this
        adapter = new MovieAdapter(movies);
        //resolve the recycler view and connect layout manager and adapter
        rvMovies = (RecyclerView) findViewById(R.id.rvMovies);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        rvMovies.setAdapter(adapter);



        //get config on app creation
        getConfiguration();
        //get movies


    }


    //get list of currently playing movies from api
    private void getNowPlaying(){
        //create  URL
        String url = API_BASE_URL + "/movie/now_playing";
        //set request params
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key)); //API key required always
        //get request w json object response
        client.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //load the results into movies list
                try {
                    JSONArray results = response.getJSONArray("results");
                    //iterate through results and create movie objects
                    for (int i = 0; i < results.length(); i++){
                        Movie movie = new Movie(results.getJSONObject(i));
                        movies.add(movie);
                        adapter.notifyItemInserted(movies.size()-1);
                    }
                    Log.i(TAG, String.format("Loaded %s movies",results.length()));
                } catch (JSONException e) {
                    logError("Failed to parse now playing movies", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed to get data from now playing endpoint", throwable, true);
            }
        });
    }

    //get config from api
    private void getConfiguration(){
        //create  URL
        String url = API_BASE_URL + "/configuration";
        //set request params
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key)); //API key required always
        //get request w json object response
        client.get(url,params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    config = new Config(response);
                    Log.i(TAG, String.format("Loaded configuration with imagebase url %s and poster size %s", config.getImageBaseUrl(), config.getPosterSize()));
                } catch (JSONException e) {
                    logError("Failed parsing config", e, true);
                }
                //pass config to adapter
                adapter.setConfig(config);
                getNowPlaying();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed getting configuration", throwable, true);
            }
        });
    }

    //handle errors, log and alert the user
    private void logError(String message, Throwable error, boolean alertUser){
        Log.e(TAG, message, error);
        //alert user to avoid silent errors
        if (alertUser){
            //show a toast with error
            Toast.makeText(getApplicationContext(),message, Toast.LENGTH_LONG).show();
        }
    }
}
