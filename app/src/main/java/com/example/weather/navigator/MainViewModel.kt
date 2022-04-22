package com.example.weather.navigator

import android.app.Application
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import com.example.weather.MainActivity
import com.example.weather.R
import com.example.weather.base.BaseScreen

const val ARG_SCREEN = "SCREEN"

/**
 * Navigator implementation. It extends [AndroidViewModel] because 1) we need android dependency
 * (application context); 2) it should survive after screen rotation.
 */
class MainViewModel(
    application: Application
) : AndroidViewModel(application), Navigator {

    val whenActivityActive = MainActivityActions()

    override fun launch(screen: BaseScreen) = whenActivityActive {
        launchFragment(it, screen)
    }

    override fun onCleared() {
        super.onCleared()
        whenActivityActive.clear()
    }

    fun launchFragment(activity: MainActivity, screen: BaseScreen, addToBackStack: Boolean = true) {
        val fragment = screen.javaClass.enclosingClass.newInstance() as Fragment
        fragment.arguments = bundleOf(ARG_SCREEN to screen)
        val transaction = activity.supportFragmentManager.beginTransaction()
        if (addToBackStack) transaction.addToBackStack(null)
        transaction
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}