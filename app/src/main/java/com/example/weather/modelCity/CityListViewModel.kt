package com.example.weather.modelCity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weather.asynchrony.PendingResultWeather
import com.example.weather.asynchrony.SuccessResultWeather
import com.example.weather.base.BaseViewModel
import com.example.weather.base.LiveResult
import com.example.weather.base.MutableLiveResult
import com.example.weather.dataClass.City
import com.example.weather.modelWeather.WeatherInCityFragment
import com.example.weather.navigator.Navigator
import com.example.weather.repository.city.CityListener
import com.example.weather.repository.city.CityService
import kotlinx.coroutines.launch

typealias LiveResult<T> = LiveData<Result<T>>
typealias MutableLiveResult<T> = MutableLiveData<Result<T>>

class CityListViewModel (
    private val cityService: CityService,
    private val navigator: Navigator,
): BaseViewModel() {

    private val _cities = MutableLiveResult<List<City>>(PendingResultWeather())
    val cities: LiveResult<List<City>> = _cities

    private val listener: CityListener = {
        _cities.value = SuccessResultWeather(it)
    }

    init {
        load()
        loadCities()
    }

    fun gotoWeatherinCitywithCity(city: City){
        val screen = WeatherInCityFragment.Screen(city = city)
        navigator.launch(screen)
    }

    fun deleteCity(city: City){
        cityService.deleteCity(city)
    }

    fun move(city: City, moveBy: Int){
        viewModelScope.launch {
            cityService.moveCity(city, moveBy)
        }
    }

    private fun loadCities(){
        cityService.add(listener)
    }

    private fun load() = into(_cities){ cityService.getAvailableCity() }
}