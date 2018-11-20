package com.example.nehagupta.openweatherapi.rest;

import com.example.nehagupta.openweatherapi.model.Weather;
import com.example.nehagupta.openweatherapi.model.WeatherContributor;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    //http://api.openweathermap.org/data/2.5/weather?q=London&appid=3475d70695cba4c42abeed83d14d253b
    @GET("weather")
    Call<WeatherContributor> getWeatherDetails(@Query("q") String id, @Query("appid") String apiKey);
}
