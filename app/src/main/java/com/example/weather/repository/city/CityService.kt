package com.example.weather.repository.city

import android.util.Log
import com.example.weather.dataClass.City
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import java.util.*

typealias CityListener = (cities: List<City>) -> Unit

class CityService: CityRepository {

    private var cities = mutableListOf<City>()
    private val listeners = mutableSetOf<CityListener>()

    init {
        cities = (0..3).map {
            City(
                id = it,
                name = names[it],
                description = descriptions[it],
                mLatitudeTextView = 0.0,
                mLongitudeTextView = 0.0
            )
        }.toMutableList()
    }

    override fun moveCity(city: City, moveBy: Int){
        val oldIndex = cities.indexOfFirst { it.id == city.id }
        if (oldIndex == -1) return
        val newIndex = oldIndex + moveBy
        if (newIndex < 0 || newIndex >= cities.size) return
        Collections.swap(cities, oldIndex, newIndex)
        notifyChanges()
    }

    override suspend fun getAvailableCity(): List<City> = withContext(Dispatchers.IO) {
        delay(1000)
        return@withContext cities
    }

    override fun deleteCity(city: City){
        val delIndex = cities.indexOfFirst { it.id == city.id }
        cities.removeAt(delIndex)
        notifyChanges()
    }

    override fun add(listener: CityListener){
        listeners.add(listener)
        listener.invoke(cities)
    }

    override fun removeListener(listener: CityListener){
        listeners.remove(listener)
    }

    private fun notifyChanges() {
        listeners.forEach { it.invoke(cities) }
    }

    companion object{
        private val names = mutableListOf("moscow", "saint%20petersburg", "nur-sultan", "tula")
        private val descriptions = mutableListOf("Москва", "Санкт-Петербург", "Нур-Султан", "Тула")
    }
}