package com.example.weather

import android.os.Bundle
<<<<<<< Updated upstream
import com.example.weather.databinding.ActivityMainBinding
import com.example.weather.modelCity.City
import com.example.weather.modelCity.CityService
=======
import androidx.appcompat.app.AppCompatActivity
import com.example.weather.modelCity.LocalOrCityFragment
import com.example.weather.navigator.*
>>>>>>> Stashed changes

class MainActivity : AppCompatActivity(), FragmentsHolder {

<<<<<<< Updated upstream
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
=======
    private lateinit var navigator: StackFragmentNavigator

    private val activityViewModel by viewModelCreator<ActivityScopeViewModel> {
        ActivityScopeViewModel(
            navigator = IntermediateNavigator()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigator = StackFragmentNavigator(
            activity = this,
            containerId = R.id.fragmentContainer,
            defaultTitle = getString(R.string.app_name),
            initialScreenCreator = { LocalOrCityFragment.Screen() }
        )
        navigator.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        navigator.onDestroy()
        super.onDestroy()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onResume() {
        super.onResume()
        // execute navigation actions only when activity is active
        activityViewModel.navigator.setTarget(navigator)
    }

    override fun onPause() {
        super.onPause()
        // postpone navigation actions if activity is not active
        activityViewModel.navigator.setTarget(null)
    }

    override fun getActivityScopeViewModel(): ActivityScopeViewModel {
        return activityViewModel
    }

    override fun notifyScreenUpdates() {
        navigator.notifyScreenUpdates()
    }
>>>>>>> Stashed changes
}