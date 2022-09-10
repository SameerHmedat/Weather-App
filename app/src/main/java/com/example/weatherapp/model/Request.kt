package com.example.weatherapp.model


import com.google.gson.annotations.SerializedName

data class Request(
    @SerializedName("query")
    val query: String,
    @SerializedName("type")
    val type: String
)