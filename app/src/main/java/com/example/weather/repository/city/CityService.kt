package com.example.weather.repository.city

import com.example.weather.dataClass.City
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
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

    /*override suspend fun getCity(city: City) = withContext(Dispatchers.IO) {
        delay(5000)
        var city1: City
        if (city.id != -1) {
            val delIndex = cities.indexOfFirst { it.id == city.id }
            city1 = cities[delIndex]
        } else{
            city1 = city
        }
        return@withContext city1
    }*/

    //override fun moveCity(city: City, moveBy: Int) {

    override fun moveCity(city: City, moveBy: Int): Flow<Int> = flow {
        var process = 0
        val oldIndex = cities.indexOfFirst { it.id == city.id }
        if (oldIndex == -1) emit(100)
        else {
            val newIndex = oldIndex + moveBy
            if (newIndex < 0 || newIndex >= cities.size) emit(100)
            else{
                Collections.swap(cities, oldIndex, newIndex)
                while (process < 0){
                    process += 1
                    delay(10)
                }
                notifyChanges()
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getAvailableCity(): List<City> = withContext(Dispatchers.IO) {
        delay(1000)
        return@withContext cities
    }

    override fun deleteCity(city: City){
        val delIndex = cities.indexOfFirst { it.id == city.id }
        cities.removeAt(delIndex)
        notifyChanges()
    }

    /*override fun add(listener: CityListener){
        listeners.add(listener)
        listener.invoke(cities)
    }

     */

/*    override fun removeListener(listener: CityListener){
        listeners -= listener
    }

 */

    override fun listenerCurrentCity(): Flow<List<City>> = callbackFlow {
        val listener: CityListener ={
            trySend(it)
        }
        listeners.add(listener)
        awaitClose{
            listeners.remove(listener)
        }
    }.buffer(Channel.CONFLATED)


    private fun notifyChanges() {
        listeners.forEach { it.invoke(cities) }
    }

    companion object{
        private val names = mutableListOf("moscow", "saint%20petersburg", "nur-sultan", "tula")
        private val descriptions = mutableListOf("Москва", "Санкт-Петербург", "Нур-Султан", "Тула")
    }
}