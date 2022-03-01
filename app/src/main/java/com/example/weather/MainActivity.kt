package com.example.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weather.databinding.ActivityMainBinding
import com.example.weather.modelCity.City
import com.example.weather.modelCity.CityService

class MainActivity : AppCompatActivity(), AppContract {

    private lateinit var bindingMain: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingMain = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingMain.root)
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragmentContainer, LocalOrCityFragment())
            .commit()
    }

    override val cityService: CityService
        get() = (applicationContext as App).cityService

    override fun launchWeatherCity(city: City) {
        supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragmentContainer, WeatherInCityFragment.newInstance(city))
            .commit()
    }
}