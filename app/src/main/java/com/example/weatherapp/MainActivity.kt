package com.example.weatherapp

import android.content.Context
import android.icu.util.Calendar
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.weatherapp.adapters.CardAdapter
import com.example.weatherapp.adapters.ElementAdapter
import com.example.weatherapp.dataClass.Card
import com.example.weatherapp.dataClass.Element
import com.example.weatherapp.response.WeatherResponse
import com.example.weatherapp.service.WeatherApiService
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private val infoList: ArrayList<Element> = arrayListOf()
    private var myAdapter = ElementAdapter(infoList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        isConnectedInternet()
        setupInfoUIList()
        setInfoList()
        changeText()
        initialCity()



        val df: DateFormat = SimpleDateFormat("h:mm a")
        val date: String = df.format(Calendar.getInstance().time)
        txt_time.text = "Time Now : $date"


    }


    private fun setupInfoUIList() {
        rvElement.layoutManager = LinearLayoutManager(this)
        rvElement.setHasFixedSize(true)

        rvCard.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvCard.setHasFixedSize(true)

        myAdapter.setOnItemClickListener(object : ElementAdapter.OnItemClickedListener {
            override fun onItemClick(position: Int) {
                edt_city_name.setText(myAdapter.newList[position].city_name)
                val cityName = edt_city_name.text.toString()
                rvElement.visibility = View.INVISIBLE
                WeatherApiService.getInstance().getCityWeatherData(cityName)
                    .enqueue(object : Callback<WeatherResponse> {
                        override fun onResponse(
                            call: Call<WeatherResponse>,
                            response: Response<WeatherResponse>
                        ) {
                            setDataOnViews(response.body()!!)
                        }

                        override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                            cons1.visibility = View.INVISIBLE
                            linearDateTimeCity.visibility = View.INVISIBLE
                            linearHumdityUpdateDesc.visibility = View.INVISIBLE
                            rvCard.visibility = View.INVISIBLE
                            tv_degree.visibility = View.INVISIBLE
                            progressError.visibility = View.VISIBLE  // Disconnected

                        }
                    }
                    )


            }
        }
        )
    }


    private fun setDataOnViews(body: WeatherResponse) {
        val cardList: ArrayList<Card> = arrayListOf()
        val cardAdapter = CardAdapter(cardList)

        for (i in 1 until 9) {
            val date = "${body.data.weather[i].date}"
            val degree = "${body.data.weather[i].maxtempC}°C"
            val sunRise = "SunR: ${body.data.weather[i].astronomy[0].sunrise}"
            cardList.add(Card(date, degree, sunRise))
        }
        rvCard.adapter = cardAdapter

        tv_degree.text = "${body.data.currentCondition[0].tempC}°C"
        tv_city_name.text = "City : ${body.data.request[0].query}"
        tv_humidity.text = "${body.data.currentCondition[0].humidity}%"
        tv_last_update.text = body.data.currentCondition[0].observationTime
        tv_weather_desc.text = body.data.currentCondition[0].weatherDesc[0].value
        tv_visibility.text = body.data.currentCondition[0].visibility
        txt_date.text = "Date : ${body.data.weather[0].date}"
        // Log.d("MainActivity", body.data.currentCondition[0].weatherIconUrl[0].value)

        //here because the api return http so not safe so don't work ,fixed it by adding 's' secure
        var url = body.data.currentCondition[0].weatherIconUrl[0].value
        if (url[4] != 's') {
            url = url.substring(0, 4) + "s" + url.substring(4)
        }
        Glide.with(this).load(url).into(img_weather_pictures)

    }

    private fun setInfoList() {
        val json: String =
            this.assets.open("country.json").bufferedReader().use { it.readText() }
        val jsoArr = JSONArray(json)
        for (i in 0 until jsoArr.length()) {
            val jsonObj = jsoArr.getJSONObject(i)
            infoList += Element(city_name = jsonObj.getString("city"))
            rvElement.adapter = myAdapter
        }

    }

    private fun changeText() {
        edt_city_name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                rvElement.visibility = View.VISIBLE
                filter(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                rvElement.visibility = View.VISIBLE
                filter(s.toString())
            }

            private fun filter(text: String) {
                val filterList: ArrayList<Element> = arrayListOf()
                for (item in infoList) {
                    if (item.city_name.lowercase(Locale.getDefault())
                            .contains(text.lowercase(Locale.getDefault()))
                    ) {
                        filterList.add(item)
                    }
                }
                myAdapter.filterList(filterList)
            }
        })
    }


    private fun initialCity() {
        WeatherApiService.getInstance().getCityWeatherData("Amman")
            .enqueue(object : Callback<WeatherResponse> {
                override fun onResponse(
                    call: Call<WeatherResponse>,
                    response: Response<WeatherResponse>
                ) {
                    setDataOnViews(response.body()!!)
                }

                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {}
            })
    }

    private fun isOnline(): Boolean {
        var connected = false
        try {
            val connectivityManager =
                applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            connected = networkInfo != null && networkInfo.isAvailable && networkInfo.isConnected
            return connected
        } catch (e: Exception) {
            Log.e("failed Internet", e.message.toString())
        }
        return connected
    }


    private fun isConnectedInternet() {
        if (isOnline()) {
            //Connected
            txtError.visibility = View.GONE
            progressError.visibility = View.GONE

        } else {
            // Disconnected
            cons1.visibility = View.INVISIBLE
            linearDateTimeCity.visibility = View.INVISIBLE
            linearHumdityUpdateDesc.visibility = View.INVISIBLE
            rvCard.visibility = View.INVISIBLE
            progressError.visibility = View.VISIBLE
            txtError.visibility = View.VISIBLE
        }
    }
}
