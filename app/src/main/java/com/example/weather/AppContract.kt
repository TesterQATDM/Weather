package com.example.weather

import androidx.fragment.app.Fragment
import com.example.weather.modelCity.City
import com.example.weather.modelCity.CityService

fun Fragment.contract(): AppContract = requireActivity() as AppContract

interface AppContract {

    val cityService: CityService

    fun launchWeatherCity(city: City)

}