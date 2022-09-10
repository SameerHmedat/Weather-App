package com.example.weatherapp.model


import com.google.gson.annotations.SerializedName

data class Month(
    @SerializedName("absMaxTemp")
    val absMaxTemp: String,
    @SerializedName("absMaxTemp_F")
    val absMaxTempF: String,
    @SerializedName("avgDailyRainfall")
    val avgDailyRainfall: String,
    @SerializedName("avgMinTemp")
    val avgMinTemp: String,
    @SerializedName("avgMinTemp_F")
    val avgMinTempF: String,
    @SerializedName("index")
    val index: String,
    @SerializedName("name")
    val name: String
)