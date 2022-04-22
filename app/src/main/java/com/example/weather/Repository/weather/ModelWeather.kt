package com.example.weather.Repository.weather

import com.example.weather.dataClass.City
import com.example.weather.dataClass.Weather
import com.google.gson.Gson
import java.net.HttpURLConnection
import java.net.URL

class ModelWeather(city: City) {

    lateinit var mMineUserEntity: Weather
    private var model = ""
    private var currentCity: City = city
    private var connection: HttpURLConnection

    init {
        connection = if (currentCity.mLatitudeTextView == 0.0) {
            URL(
                "https://api.openweathermap.org/data/2.5/weather?q=${currentCity.name}&appid=7a5e54ffb95ea5bee471a8583223fc2f&unit=metric&lang=ru"
            ).openConnection() as HttpURLConnection
        } else {
            URL(
                "https://api.openweathermap.org/data/2.5/weather?lat=${currentCity.mLatitudeTextView}" +
                        "&lon=${currentCity.mLongitudeTextView}&appid=7a5e54ffb95ea5bee471a8583223fc2f&unit=metric&lang=ru"
            ).openConnection() as HttpURLConnection
        }
        if (connection.responseCode == 200)
            try {
                model = connection.inputStream.bufferedReader().use { it.readText() }
            } finally {
                connection.disconnect()
                mMineUserEntity = Gson().fromJson(model, Weather::class.java)
            }
    }
}