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
import com.example.weather.repository.city.CityListener
import com.example.weather.repository.city.CityService
import com.example.weather.utils.requireValue
import com.example.weather.utils.share
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

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

    init {
        viewModelScope.launch {
            cityService.listenerCurrentListCities()
                .collect {
                    _cities.value = SuccessResultWeather(it)
                }
        }
        load()
    }

    fun gotoWeatherInCityWithCity(city: City){
        val screen = WeatherInCityFragment.Screen(city = city)
        navigator.launch(screen)
    }

    fun deleteCity(city: City): Job = viewModelScope.launch {
            showProgress()
            try {
                cityService.deleteCity(city)
                processIsOK()
            } catch (e: RuntimeException) {
                processRuntimeException()
            }
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
        _state.value = _state.requireValue().copy(
            InProgressDeleteOrMove = false
        )
    }

    private fun showProgress() {
        _state.value = State(InProgressDeleteOrMove = true)
    }

    private fun processIsOK() {
        _state.value = _state.requireValue().copy(
            InProgressDeleteOrMove = false
        )
    }

    fun cancel() {
        viewModelScope.coroutineContext.cancelChildren()
        processIsOK()
    }

    //private fun launchTabsScreen() = _navigateToTabsEvent.publishEvent()

    data class State(
        val InProgressDeleteOrMove: Boolean = false
    ) {
        val showProgress: Boolean get() = InProgressDeleteOrMove
        val enableViews: Boolean get() = !InProgressDeleteOrMove
    }
}