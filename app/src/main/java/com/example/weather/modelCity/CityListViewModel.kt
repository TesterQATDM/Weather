package com.example.weather.modelCity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weather.Repository.City.CityListener
import com.example.weather.Repository.City.CityService
import com.example.weather.dataClass.City
import com.example.weather.base.BaseViewModel
import com.example.weather.modelWeather.WeatherInCityFragment
import com.example.weather.navigator.Navigator

class CityListViewModel (
    private val cityService: CityService,
    private val navigator: Navigator,
): BaseViewModel() {

    private val _cities = MutableLiveData<List<City>>()
    val cities: LiveData<List<City>> = _cities

    private val listener: CityListener = {
        _cities.value = it
    }

    init {
        cityService.add(listener)
    }

    fun gotoWeatherinCitywithCity(city: City){
        val screen = WeatherInCityFragment.Screen(city = city)
        navigator.launch(screen)
    }

    override fun onCleared() {
        super.onCleared()
        cityService.removeListener(listener)
    }

    fun deleteCity(city: City){
        cityService.deleteCity(city)
    }

    fun move(city: City, moveBy: Int){
        cityService.moveCity(city, moveBy)
    }
}