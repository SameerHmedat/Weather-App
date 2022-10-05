package com.example.weatherapp

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.manager.Lifecycle
import com.example.weatherapp.model.Data
import com.example.weatherapp.response.WeatherResponse
import com.example.weatherapp.service.WeatherApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class MainViewModel:ViewModel() {

    val dataMutableLiveData=MutableLiveData<Result>()

    init {
        getData("Amman")
    }

     fun getData(cityName: String) {
             dataMutableLiveData.value = Result.Loading
             WeatherApiService.getInstance().getCityWeatherData(cityName).enqueue(
                 object : retrofit2.Callback<WeatherResponse> {

                     override fun onResponse(
                         call: Call<WeatherResponse>,
                         response: Response<WeatherResponse>
                     ) {
                         dataMutableLiveData.value = Result.Success(response.body()!!.data)
                     }

                     override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                         dataMutableLiveData.value = Result.Failure(t)
                         Log.d("Failure Response", t.message.toString())
                     }

                 })

     }

}