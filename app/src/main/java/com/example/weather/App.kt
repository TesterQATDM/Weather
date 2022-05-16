package com.example.weather

import android.app.Application
import com.example.weather.navigator.BaseApplication
import com.example.weather.repository.city.CityService
import com.example.weather.repository.Repository


class App: Application(), BaseApplication {

    override val repositories: List<Repository> = listOf<Repository>(
        CityService()
    )

}