package com.example.weatherapp

import android.icu.util.Calendar
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.weatherapp.adapters.CardDayAdapter
import com.example.weatherapp.adapters.CardHourAdapter
import com.example.weatherapp.adapters.ElementAdapter
import com.example.weatherapp.dataClass.CardDay
import com.example.weatherapp.dataClass.CardHour
import com.example.weatherapp.dataClass.Element
import com.example.weatherapp.model.Data
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.card_hour_item.*
import kotlinx.android.synthetic.main.element_items_row.*
import org.json.JSONArray
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by lazy { ViewModelProvider(this)[MainViewModel::class.java] }
    private val infoList: ArrayList<Element> = arrayListOf()
    private var myAdapter = ElementAdapter(infoList)
    private var cityNameInitial: String = "Amman"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        refresh_layout.setOnRefreshListener(this::tryAgainButton)

//        scrollView.setOnScrollChangeListener { v: View, scrollX: Int, scrollY: Int, _: Int, _: Int ->
//            refresh_layout.isEnabled = scrollY == 0
//        }

        setTime()
        setupInfoUIList()
        setInfoList()
        changeText()


    }

    private fun setTime() {
        val df: DateFormat = SimpleDateFormat("h:mm a")
        val date: String = df.format(Calendar.getInstance().time)
        txt_time.text = "Time Now : $date"
    }

    private fun setupInfoUIList() {
        rvElement.layoutManager = LinearLayoutManager(this)
        rvElement.setHasFixedSize(true)

        rvCardDay.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvCardDay.setHasFixedSize(true)

        rvCardHour.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvCardHour.setHasFixedSize(true)


        empty_view.error().setOnClickListener {
            empty_view.showLoading()
            viewModel.getData(cityNameInitial)
            empty_view.postDelayed({ empty_view.showContent() }, 2000)
            setTime()
        }

        viewModel.resultMutableLiveData.observe(this@MainActivity){ result->
            when(result){
                is Result.Failure ->
                {
                    empty_view.error().show()
                }
                Result.Loading -> {
                    empty_view.loading().show()
                }
                is Result.Success -> {
                    empty_view.content().show()
                    setTime()
                    setDataOnViews(result.data) //the thing exchange between viewModel and Activity

                }
            }
        }

        myAdapter.setOnItemClickListener(object : ElementAdapter.OnItemClickedListener {
            override fun onItemClick(position: Int) {
                edt_city_name.setText(myAdapter.newList[position].city_name)
                val cityName = edt_city_name.text.toString()
                rvElement.visibility = View.INVISIBLE
                viewModel.getData(cityName)
                cityNameInitial = cityName


            }
        }
        )
    }


    private fun setDataOnViews(body: Data) {
        val cardDayList: ArrayList<CardDay> = arrayListOf()
        val cardDayAdapter = CardDayAdapter(cardDayList)

        for (i in 1 until 14) {
            val date = "Date: ${body.weather[i].date}"
            val degree = "Degree: ${body.weather[i].maxtempC}°C"
            val sunRise = "SunRise: ${body.weather[i].astronomy[0].sunrise}"
            cardDayList.add(CardDay(date, degree, sunRise))
        }
        //Error : filterCList.add(Card(cardDate.toString(),cardDegree.toString(),cardSunRise.toString())
        //because those TextViews not EditTexts(textView,textView,textView) not Strings will print textView as self(android.design.material...)
        //OR : filterCList.add(Card(cardDate.toString()  as String,cardDegree.text as String,cardSunRise.text as String))

        rvCardDay.adapter = cardDayAdapter


        val cardHourList: ArrayList<CardHour> = arrayListOf()
        val cardHourAdapter = CardHourAdapter(cardHourList)

        for (i in 0 until 8) {
            val calculateExactlyHour = (body.weather[1].hourly[i].time).toInt() / 100
            val cardHourHour: String = if (calculateExactlyHour < 12) {
                if (calculateExactlyHour == 0) {
                    "Hour: 12 AM"
                } else {
                    "Hour: ${(body.weather[1].hourly[i].time).toInt() / 100} AM"
                }

            } else {
                "Hour: ${(body.weather[1].hourly[i].time).toInt() / 100} PM"
            }

            val cardDegreeHour = "Degree: ${body.weather[1].hourly[i].tempC}°C"
            val cardHourDesc = "Desc: ${body.weather[1].hourly[i].weatherDesc[0].value}"
            val urlHour = body.weather[1].hourly[i].weatherIconUrl[0].value
//            if (urlHour[4] != 's') {
//                urlHour = urlHour.substring(0, 4) + "s" + urlHour.substring(4)
//            }
            cardHourList.add(CardHour(cardHourHour, cardDegreeHour, cardHourDesc, urlHour))
        }
        rvCardHour.adapter = cardHourAdapter


        tv_degree.text = "${body.currentCondition[0].tempC}°C"
        tv_city_name.text = "City : ${body.request[0].query}"
        tv_humidity.text = "${body.currentCondition[0].humidity}%"
        tv_last_update.text = body.currentCondition[0].observationTime
        tv_weather_desc.text = body.currentCondition[0].weatherDesc[0].value
        tv_visibility.text = body.currentCondition[0].visibility
        txt_date.text = "Date : ${body.weather[0].date}"

        //here because the api return http so not safe so don't work ,fixed it by adding 's' secure
        val url = body.currentCondition[0].weatherIconUrl[0].value
//        if (url[4] != 's') {
//            url = url.substring(0, 4) + "s" + url.substring(4)
//        }
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

    private fun tryAgainButton() {
        refresh_layout.isRefreshing = false
        empty_view.showLoading()
        viewModel.getData(cityNameInitial)
        empty_view.postDelayed({ empty_view.showContent() }, 2000)
        setTime()
    }

}

sealed class Result {
    data class Success(val data: Data) : Result()
    data class Failure(val exception: Throwable) : Result()
    object Loading : Result()
}

//as known (((((State)))))