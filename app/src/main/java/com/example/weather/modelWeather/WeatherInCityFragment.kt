package com.example.weather.modelWeather

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.weather.interfaces.HasCustomTitle
import com.example.weather.R
import com.example.weather.databinding.FragmentWeatherInCityBinding
import com.example.weather.modelCity.City
import com.example.weather.utils.factory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.round

class WeatherInCityFragment : Fragment(), HasCustomTitle {

    private lateinit var bindingWeatherInCity: FragmentWeatherInCityBinding
    private val viewModelD: WeatherDetailsViewModel by viewModels{factory()}
    private lateinit var city: City

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelD.loadCity(requireArguments().getParcelable<City>(AGR_CITY) as City)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingWeatherInCity = FragmentWeatherInCityBinding.inflate(inflater, container, false)
        viewModelD.cityD.observe(viewLifecycleOwner, Observer {
            city = it
        })
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
        fun newInstance(city: City): WeatherInCityFragment {
            val fragmentWeatherInCity = WeatherInCityFragment()
            fragmentWeatherInCity.arguments = bundleOf(AGR_CITY to city)
            return fragmentWeatherInCity
        }
    }

    override fun getTitleRes(): Int = R.string.weather_in_city

}