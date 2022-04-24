package com.example.weather.modelWeather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.weather.R
import com.example.weather.repository.weather.ModelWeather
import com.example.weather.base.BaseFragment
import com.example.weather.base.BaseScreen
import com.example.weather.base.screenViewModel
import com.example.weather.databinding.FragmentWeatherInCityBinding
import com.example.weather.interfaces.HasCustomTitle
import com.example.weather.dataClass.City
import com.example.weather.dataClass.Weather
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.round

class WeatherInCityFragment : BaseFragment(), HasCustomTitle {

    private lateinit var bindingWeatherInCity: FragmentWeatherInCityBinding
    override val viewModel by screenViewModel<WeatherDetailsViewModel>()
    private lateinit var city: City
    private lateinit var weather: Weather

    class Screen(
        val city: City
    ) : BaseScreen

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingWeatherInCity = FragmentWeatherInCityBinding.inflate(inflater, container, false)
        viewModel.cityD.observe(viewLifecycleOwner, Observer {
            city = it
            CoroutineScope(Dispatchers.Main).launch {
                val result = withContext(Dispatchers.IO) {
                    return@withContext ModelWeather(city)
                }
                viewModel.loadWeather(result.mMineUserEntity as Weather)
            }
        })
        viewModel.weatherAPI.observe(viewLifecycleOwner, Observer {result ->
            weather = result
            bindingWeatherInCity.temp.text = round(weather.main["temp"]!!.toDouble() - 273.15).toString()
            bindingWeatherInCity.desCity.text = weather.name
            bindingWeatherInCity.description.text = weather.weather[0]["description"]
        })
        return bindingWeatherInCity.root
    }

    override fun getTitleRes(): Int = R.string.weather_in_city

}