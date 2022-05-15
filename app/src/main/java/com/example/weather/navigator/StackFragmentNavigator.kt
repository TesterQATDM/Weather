package com.example.weather.navigator

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.weather.base.BaseScreen
import com.example.weather.base.BaseScreen.Companion.ARG_SCREEN
import com.example.weather.interfaces.HasCustomTitle

class StackFragmentNavigator(
    private val activity: AppCompatActivity,
    @IdRes private val containerId: Int,/*контейрне в который фрагменты складывются*/
    private val defaultTitle: String,
    private val initialScreenCreator: () -> BaseScreen
) : Navigator {

    override fun launch(screen: BaseScreen) {
        launchFragment(screen)
    }

    fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            // define the initial screen that should be launched when app starts.
            launchFragment(
                screen = initialScreenCreator(),
                addToBackStack = false
            )
        }
        activity.supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentCallbacks, false)
    }

    fun onDestroy() {
        activity.supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentCallbacks)
    }

    fun notifyScreenUpdates() {
        val f = activity.supportFragmentManager.findFragmentById(containerId)
        Log.d("Log", activity.supportFragmentManager.backStackEntryCount.toString())
        if (activity.supportFragmentManager.backStackEntryCount > 0) {
            // more than 1 screen -> show back button in the toolbar
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        } else {
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }
        if (f is HasCustomTitle){
            activity.supportActionBar?.title = f.getTitleRes()
        } else {
            activity.supportActionBar?.title = defaultTitle
        }
    }

    private fun launchFragment(screen: BaseScreen, addToBackStack: Boolean = true) {
        // as screen classes are inside fragments -> we can create fragment directly from screen
        val fragment = screen.javaClass.enclosingClass.newInstance() as Fragment
        // set screen object as fragment's argument
        fragment.arguments = bundleOf(ARG_SCREEN to screen)

        val transaction = activity.supportFragmentManager.beginTransaction()
        if (addToBackStack) transaction.addToBackStack(null)
        transaction
            .replace(containerId, fragment)
            .commit()
    }

    private val fragmentCallbacks = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
            notifyScreenUpdates()
        }
    }
}