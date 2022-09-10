package com.example.weatherapp.model


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("ClimateAverages")
    val climateAverages: List<ClimateAverage>,
    @SerializedName("current_condition")
    val currentCondition: List<CurrentCondition>,
    @SerializedName("request")
    val request: List<Request>,
    @SerializedName("weather")
    val weather: List<Weather>
)