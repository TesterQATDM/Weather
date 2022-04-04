package com.example.weather

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weather.modelCity.CityListViewModel
import com.example.weather.modelWeather.WeatherDetailsViewModel

class ViewModelFactory(
    val app: App
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val viewModel = when(modelClass) {
            CityListViewModel::class.java -> CityListViewModel(app.cityService)
            WeatherDetailsViewModel::class.java -> WeatherDetailsViewModel(app.cityService)
            else -> {
                Log.d("Log", modelClass.name.toString())
            }
        }
        return viewModel as T
    }
}

fun Fragment.factory() = ViewModelFactory(requireContext().applicationContext as App)
fun Fragment.contract(): AppContract = requireActivity() as AppContract