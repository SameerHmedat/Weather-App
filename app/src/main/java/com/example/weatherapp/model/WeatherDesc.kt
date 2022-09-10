package com.example.weatherapp.model


import com.google.gson.annotations.SerializedName

data class WeatherDesc(
    @SerializedName("value")
    val value: String
)