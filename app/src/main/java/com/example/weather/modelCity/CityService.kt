package com.example.weather.modelCity

class CityService {

    val cities: List<City> = (0..1).map {City(
        id = it,
        name = names[it],
        description = descriptions[it],
        mLatitudeTextView = 0.0,
        mLongitudeTextView = 0.0
    )}

    companion object{
        private val names = listOf<String>("moscow", "saint%20petersburg")
        private val descriptions = listOf<String>("Москва", "Санкт-Петербург")
    }
}