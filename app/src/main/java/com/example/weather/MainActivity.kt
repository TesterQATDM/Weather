package com.example.weather

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.weather.databinding.ActivityMainBinding
import com.example.weather.interfaces.HasCustomTitle
import com.example.weather.modelCity.LocalOrCityFragment
import com.example.weather.navigator.MainViewModel

class MainActivity : AppCompatActivity(){

    private lateinit var bindingMain: ActivityMainBinding
    private val currentFragment: Fragment
        get() = supportFragmentManager.findFragmentById(R.id.fragmentContainer)!!

    private val navigator by viewModels<MainViewModel> {
        ViewModelProvider.AndroidViewModelFactory(
            application
        )
    }

    private val fragmentListener = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            updateUi()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingMain = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }
        setSupportActionBar(bindingMain.toolbar)

        if (savedInstanceState == null) {
            navigator.launchFragment(this, LocalOrCityFragment.Screen(), addToBackStack = false)
        }

        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // we've called setSupportActionBar in onCreate,
        // that's w hy we need to override this method too
        updateUi()
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onResume() {
        super.onResume()
        navigator.whenActivityActive.mainActivity = this
    }

    override fun onPause() {
        super.onPause()
        navigator.whenActivityActive.mainActivity = null
    }

    fun updateUi() {
        val fragment = currentFragment
        if (fragment is HasCustomTitle) {
            bindingMain.toolbar.title = getString(fragment.getTitleRes())
        } else {
            bindingMain.toolbar.title = getString(R.string.choose_city)
        }

        if (supportFragmentManager.backStackEntryCount > 0) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        } else {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            supportActionBar?.setDisplayShowHomeEnabled(false)
        }
    }
}