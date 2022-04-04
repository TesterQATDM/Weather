package com.example.weather.modelWeather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.modelCity.City
import com.example.weather.modelCity.CityService

class WeatherDetailsViewModel(
    private val cityService: CityService
): ViewModel() {
    private val _cityD = MutableLiveData<City>()
    val cityD: LiveData<City> = _cityD

    fun loadCity(user: City){
        _cityD.value = cityService.getCity(user)
    }
}