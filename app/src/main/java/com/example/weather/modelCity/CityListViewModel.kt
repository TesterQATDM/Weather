package com.example.weather.modelCity


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weather.asynchrony.PendingResultWeather
import com.example.weather.base.BaseViewModel
import com.example.weather.base.LiveResult
import com.example.weather.base.MutableLiveResult
import com.example.weather.dataClass.City
import com.example.weather.modelWeather.WeatherInCityFragment
import com.example.weather.navigator.Navigator
import com.example.weather.repository.city.CityService

typealias LiveResult<T> = LiveData<Result<T>>
typealias MutableLiveResult<T> = MutableLiveData<Result<T>>

class CityListViewModel (
    private val cityService: CityService,
    private val navigator: Navigator,
): BaseViewModel() {

    private val _cities = MutableLiveResult<List<City>>(PendingResultWeather())
    val cities: LiveResult<List<City>> = _cities

    init {
        load()
    }

    fun gotoWeatherinCitywithCity(city: City){
        val screen = WeatherInCityFragment.Screen(city = city)
        navigator.launch(screen)
    }

    fun deleteCity(city: City){
        _cities.value = PendingResultWeather()
        cityService.deleteCity(city)
    }

    fun move(city: City, moveBy: Int){
        cityService.moveCity(city, moveBy)
    }

    private fun load() = into(_cities){ cityService.getAvailableCity() }

}