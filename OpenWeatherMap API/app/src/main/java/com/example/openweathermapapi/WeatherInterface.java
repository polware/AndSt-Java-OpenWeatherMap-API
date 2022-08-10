package com.example.openweathermapapi;

import com.example.openweathermapapi.models.WeatherData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherInterface {

    @GET("weather")
    Call<WeatherData> getWeatherInfo(@Query("q") String city, @Query("appid") String appid);

    @GET("weather")
    Call<WeatherData> getWeatherInfo(@Query("q") String city, @Query("appid") String appid, @Query("units") String value, @Query("lang") String lang);
}
