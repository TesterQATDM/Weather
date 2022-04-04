package com.example.weather.modelCity

typealias CityListener = (cities: List<City>) -> Unit

class CityService {

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

    fun getCity(city: City): City{
        return if (city.id != 20) {
            val delIndex = cities.indexOfFirst { it.id == city.id }
            cities[delIndex]
        } else{
            city
        }
    }

    fun add(listener: CityListener){
        listeners.add(listener)
        listener.invoke(cities)
    }

    fun removeListener(listener: CityListener){
        listeners.remove(listener)
    }

    companion object{
        private val names = mutableListOf("moscow", "saint%20petersburg", "saint%20petersburg", "saint%20petersburg")
        private val descriptions = mutableListOf("Москва1", "Санкт-Петербург", "Санкт-Петербург1", "Санкт-Петербург2")
    }
}