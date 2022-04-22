package com.example.weather.Repository.City

import com.example.weather.Repository.Repository
import com.example.weather.dataClass.City

interface CityRepository: Repository {

    fun getCity(city: City): City

    fun moveCity(city: City, moveBy: Int)

    fun deleteCity(city: City)

    fun add(listener: CityListener)

    fun removeListener(listener: CityListener)
}