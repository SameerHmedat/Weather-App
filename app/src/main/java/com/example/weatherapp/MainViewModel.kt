package com.example.weatherapp

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.manager.Lifecycle
import com.example.weatherapp.model.Data
import com.example.weatherapp.response.WeatherResponse
import com.example.weatherapp.service.WeatherApiService
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Response

class MainViewModel : ViewModel() {

    val dataMutableLiveData = MutableLiveData<Data>()
    val errorMutableLiveData = MutableLiveData<String>()
    val loadingMutableLiveData = MutableLiveData<Boolean>()
    //  var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    init {
        getData("Amman")
    }

    fun getData(cityName: String) {
        loadingMutableLiveData.postValue(true)

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            delay(100)
            val response = WeatherApiService.getInstance().getCityWeatherData(cityName)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    dataMutableLiveData.postValue(response.body()!!.data)
                }
                else{
                    onError("Error : ${response.message()} ")
                }

            }
        }
    }
    private fun onError(message: String) {
        errorMutableLiveData.postValue(message)
    }
}
