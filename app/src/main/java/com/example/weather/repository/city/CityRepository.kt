package com.example.weather.repository.city

import com.example.weather.repository.Repository
import com.example.weather.dataClass.City
import kotlinx.coroutines.flow.Flow

interface CityRepository: Repository {

    suspend fun getAvailableCity(): List<City>

    fun moveCity(city: City, moveBy: Int)

    fun deleteCity(city: City)

    fun add(listener: CityListener)

    fun removeListener(listener: CityListener)

}