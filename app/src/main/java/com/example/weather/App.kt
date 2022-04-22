package com.example.weather

import android.app.Application
import com.example.weather.Repository.City.CityService


class App: Application() {

    val models = listOf<Any>(
        CityService()
    )

}