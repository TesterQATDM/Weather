package com.example.weather.modelCity

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.weather.asynchrony.PendingResultWeather
import com.example.weather.asynchrony.SuccessResultWeather
import com.example.weather.base.BaseViewModel
import com.example.weather.base.LiveResult
import com.example.weather.base.MutableLiveResult
import com.example.weather.dataClass.City
import com.example.weather.modelWeather.WeatherInCityFragment
import com.example.weather.navigator.Navigator
import com.example.weather.repository.city.CityService
import com.example.weather.utils.share
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch

class CityListViewModel (
    private val cityService: CityService,
    private val navigator: Navigator,
): BaseViewModel() {

    private val _cities = MutableLiveResult<List<City>>(PendingResultWeather())
    val cities: LiveResult<List<City>> = _cities

    private val _state = MutableLiveData(State())
    val state = _state.share()


    //for navigate
    //private val _navigateToTabsEvent = MutableUnitLiveEvent()
    //val navigateToTabsEvent = _navigateToTabsEvent.share()

    init{
        listenerCurrentListCitiesVM()// listener events
        load()//load _cities
    }

    private fun listenerCurrentListCitiesVM(): Job = viewModelScope.launch {
        cityService.listenerCurrentListCities()
            .collect {
                _cities.value = SuccessResultWeather(it)
            }
    }

    fun gotoWeatherInCityWithCity(city: City){
        val screen = WeatherInCityFragment.Screen(city = city)
        navigator.launch(screen)
    }

    fun deleteCity(city: City) = viewModelScope.launch {
        _state.value = State(InProgress = 0)
        cityService.deleteCity(city).collect { process ->
            _state.value = State(InProgress = process)
        }
        _state.value = State(InProgress = 100)
    }


    fun move(city: City, moveBy: Int): Job = viewModelScope.launch {
        showProgress()
        try {
            cityService.moveCity(city, moveBy)
            processIsOK()
        } catch (e: RuntimeException) {
            processRuntimeException()
        }
    }

    private fun load() = into(_cities){ cityService.getAvailableCity() }

    private fun processRuntimeException() {
        _state.value = State(InProgress = 100)
    }

    private fun showProgress() {
        _state.value = State(InProgress = 0)
    }

    private fun processIsOK() {
        _state.value = State(InProgress = 100)
    }

    fun cancel() {
        viewModelScope.coroutineContext.cancelChildren()
        listenerCurrentListCitiesVM()
        processIsOK()
    }

    //private fun launchTabsScreen() = _navigateToTabsEvent.publishEvent()

    data class State(
        var InProgress: Int = 0
    )
}