package com.example.weather.repository.city

import com.example.weather.repository.Repository
import com.example.weather.dataClass.City
import kotlinx.coroutines.flow.Flow

interface CityRepository: Repository {

    suspend fun getAvailableCity(): List<City>

    suspend fun moveCity(city: City, moveBy: Int)

    fun deleteCity(city: City): Flow<Int>

    fun listenerCurrentListCities(): Flow<List<City>>

}