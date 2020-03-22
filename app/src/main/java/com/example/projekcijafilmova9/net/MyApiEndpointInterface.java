package com.example.projekcijafilmova9.net;


import com.example.projekcijafilmova9.net.model1.Example;
import com.example.projekcijafilmova9.net.model2.Detail;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface MyApiEndpointInterface {

    //http://www.omdbapi.com/?apikey=[yourkey]&s=Batman
    @GET("/")
    Call<Example> getMovieByName(@QueryMap Map<String, String> options);

    //http://www.omdbapi.com/?apikey=[yourkey]&i=imdbid
    @GET("/")
    Call<Detail> getMovieData(@QueryMap Map<String, String> options);

}
