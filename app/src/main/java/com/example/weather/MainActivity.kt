package com.example.weather

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.weather.modelCity.LocalOrCityFragment
import com.example.weather.navigator.*

class MainActivity : AppCompatActivity(), FragmentsHolder {

    private lateinit var navigatorActivity: StackFragmentNavigator/*на стороне активити*/

    private val activityViewModel by viewModelCreator<ActivityScopeViewModel> {
        ActivityScopeViewModel(
            navigator = IntermediateNavigator()/*жизненный цикл активити стек фрагметов*/
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigatorActivity = StackFragmentNavigator(
            activity = this,
            containerId = R.id.fragmentContainer,
            defaultTitle = getString(R.string.app_name),
            initialScreenCreator = { LocalOrCityFragment.Screen() }
        )
        navigatorActivity.onCreate(savedInstanceState)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onDestroy() {
        navigatorActivity.onDestroy()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        // execute navigation actions only when activity is active
        activityViewModel.navigator.setTarget(navigatorActivity)
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
        navigatorActivity.notifyScreenUpdates()
    }
}