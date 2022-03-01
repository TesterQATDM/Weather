package com.example.weather

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.example.weather.databinding.FragmentWeatherInCityBinding
import com.example.weather.modelCity.City
import com.example.weather.modelWeather.ModelWeather
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.round

class WeatherInCityFragment : Fragment() {

        private lateinit var bindingWeatherInCity: FragmentWeatherInCityBinding
        private val city
            get() = requireArguments().getSerializable(AGR_CITY) as City

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingWeatherInCity = FragmentWeatherInCityBinding.inflate(inflater, container, false)
        bindingWeatherInCity.desCity.text = getString(R.string.City)
        bindingWeatherInCity.temp.text = getString(R.string.temp)
        bindingWeatherInCity.temp.text = getString(R.string.weather)
        CoroutineScope(Dispatchers.Main).launch {
            val result = withContext(Dispatchers.IO) {
                return@withContext ModelWeather(city)
            }
            bindingWeatherInCity.temp.text = round(result.mMineUserEntity.main["temp"]!!.toDouble() - 273.15).toString()
            bindingWeatherInCity.desCity.text = result.mMineUserEntity.name
            bindingWeatherInCity.description.text = result.mMineUserEntity.weather[0]["description"]

        }
        return bindingWeatherInCity.root
    }

    companion object {
        private const val AGR_CITY = "ARG_CITY"
        fun newInstance(city: City): WeatherInCityFragment{
            val fragmentWeatherInCity = WeatherInCityFragment()
            fragmentWeatherInCity.arguments = bundleOf(AGR_CITY to city)
            return fragmentWeatherInCity
        }
    }
}