package com.example.weatherapp

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.manager.Lifecycle
import com.example.weatherapp.Result
import com.example.weatherapp.model.Data
import com.example.weatherapp.response.WeatherResponse
import com.example.weatherapp.service.WeatherApiService
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Response

class MainViewModel : ViewModel() {

    val resultMutableLiveData=MutableLiveData<com.example.weatherapp.Result>()
//    val dataMutableLiveData = MutableLiveData<Data>()
//    val errorMutableLiveData = MutableLiveData<String>()
//    val loadingMutableLiveData = MutableLiveData<Boolean>()
      var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    init {
        getData("Amman")
    }

    @SuppressLint("SuspiciousIndentation")
    fun getData(cityName: String) {

       job=viewModelScope.launch(Dispatchers.IO+exceptionHandler){
            val response = WeatherApiService.getInstance().getCityWeatherData(cityName)
            withContext(Dispatchers.Main) {
                resultMutableLiveData.postValue(Result.Loading)
                if (response.isSuccessful) {
                    resultMutableLiveData.postValue(com.example.weatherapp.Result.Success(response.body()!!.data))
                    Log.d("Success","Success")
                }
                else{
                    Log.d("MainThread","MainThread"+response.message().toString())
                  //  resultMutableLiveData.postValue(com.example.weatherapp.Result.Failure(Throwable(response.message()) ))
                 //   onError("Error : ${response.message()} ")
                }

            }
        }
    }
    private fun onError(message: String) {
        resultMutableLiveData.postValue(com.example.weatherapp.Result.Failure(Throwable(message)))
         }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }


}
