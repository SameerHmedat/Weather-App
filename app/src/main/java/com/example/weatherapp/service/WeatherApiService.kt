package com.example.weatherapp.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherApiService {

    private const val BASE_URL = "https://api.worldweatheronline.com/premium/v1/"
    private var weatherApi: WeatherApi? = null

    fun getInstance(): WeatherApi {
        if (weatherApi == null) {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            weatherApi=retrofit.create(WeatherApi::class.java)
        }

        return weatherApi!!
    }
}