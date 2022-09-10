package com.example.weatherapp.model


import com.google.gson.annotations.SerializedName

data class WeatherIconUrl(
    @SerializedName("value")
    val value: String
)