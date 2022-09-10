package com.example.weatherapp.response

import com.example.weatherapp.model.Data
import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("data")
    val data: Data
    )

