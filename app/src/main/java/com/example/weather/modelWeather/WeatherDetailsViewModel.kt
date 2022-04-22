package com.example.weather.modelWeather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weather.base.BaseViewModel
import com.example.weather.dataClass.City
import com.example.weather.dataClass.Weather
import com.example.weather.modelWeather.WeatherInCityFragment.Screen

class WeatherDetailsViewModel(

    screen: Screen

): BaseViewModel() {
    private val _cityD = MutableLiveData<City>()
    val cityD: LiveData<City> = _cityD
    init {
        _cityD.value = screen.city
    }

    private val _weatherAPI = MutableLiveData<Weather>()
    val weatherAPI: LiveData<Weather> = _weatherAPI

    fun loadWeather(weather: Weather){
        _weatherAPI.value= weather
    }
}