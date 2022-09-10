package com.example.weatherapp.service

import com.example.weatherapp.response.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

//
    @GET("weather.ashx?key=53c58cb2f50241e5841160922220609&format=json&num_of_days=9")
    fun getCityWeatherData(
        @Query("q") cityName: String
    ): Call<WeatherResponse>


//    @GET("weather.ashx?key=53c58cb2f50241e5841160922220609&q=amman&format=json&num_of_days=9")
//    fun getAmmanWeatherData(): Call<WeatherResponse>
//
//
//    @GET("weather.ashx?key=53c58cb2f50241e5841160922220609&q=irbid&format=json&num_of_days=9")
//    fun getIrbidWeatherData(): Call<WeatherResponse>
//
//
//    @GET("weather.ashx?key=53c58cb2f50241e5841160922220609&q=aqaba&format=json&num_of_days=9")
//    fun getAqabaWeatherData(): Call<WeatherResponse>


}