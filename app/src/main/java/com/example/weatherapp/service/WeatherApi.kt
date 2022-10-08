package com.example.weatherapp.service

import com.example.weatherapp.response.WeatherResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {


    @GET("weather.ashx?key=59a44641c38d4ba2819175152220510&format=json&num_of_days=14")
    suspend fun getCityWeatherData(
        @Query("q") cityName: String
    ): Response<WeatherResponse>


}