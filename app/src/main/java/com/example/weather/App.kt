package com.example.weather

import android.app.Application
import com.example.weather.modelCity.CityService

class App: Application() {

    val cityService = CityService()
}