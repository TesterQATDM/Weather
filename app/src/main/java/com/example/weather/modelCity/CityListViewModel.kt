package com.example.weather.modelCity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CityListViewModel (
    private val cityService: CityService
): ViewModel() {

    private val _cities = MutableLiveData<List<City>>()
    val cities: LiveData<List<City>> = _cities

    private val listener: CityListener = {
        _cities.value = it
    }

    init {
        loadCities()
    }

    override fun onCleared() {
        super.onCleared()
        cityService.removeListener(listener)
    }

    private fun loadCities(){
        cityService.add(listener)
    }
}