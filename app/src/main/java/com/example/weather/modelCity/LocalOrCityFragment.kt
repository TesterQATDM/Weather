package com.example.weather.modelCity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather.CityActionListener
import com.example.weather.CityAdapter
import com.example.weather.R
import com.example.weather.base.BaseFragment
import com.example.weather.base.BaseScreen
import com.example.weather.base.screenViewModel
import com.example.weather.dataClass.City
import com.example.weather.databinding.FragmentLocalOrCityBinding
import com.google.android.gms.location.LocationServices
import kotlinx.android.parcel.Parcelize

class LocalOrCityFragment : BaseFragment(){

    private lateinit var bindingLocalOrCity: FragmentLocalOrCityBinding
    private lateinit var currentCity: City
    private lateinit var adapter: CityAdapter
    override val viewModel by screenViewModel<CityListViewModel>()
    private val requestLocationPermissionsLauncher = registerForActivityResult(
        RequestMultiplePermissions(),   // contract for requesting more than 1 permission
        ::onGotLocationPermissionsResult
    )

    @Parcelize
    class Screen : BaseScreen

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingLocalOrCity = FragmentLocalOrCityBinding.inflate(inflater, container, false)
        viewModel.cities.observe(viewLifecycleOwner, Observer {result ->
            renderResult(
                root = bindingLocalOrCity.root,
                result = result,
                onPending = {},
                onError = {},
                onSuccess= {
                    adapter.cities = it
                }
            )
        })

        adapter = CityAdapter(object : CityActionListener {
            override fun onCityMove(city: City, moveBy: Int) {
                viewModel.move(city, moveBy)
            }

            override fun details(city: City) {
                viewModel.gotoWeatherinCitywithCity(city)
            }

            override fun deleteCity(city: City) {
                viewModel.deleteCity(city)
            }
        })
        bindingLocalOrCity.rcItem.layoutManager = LinearLayoutManager(requireContext())
        bindingLocalOrCity.rcItem.adapter = adapter
        bindingLocalOrCity.local.setOnClickListener{
            if (statusInternet()) {
                requestLocationPermissionsLauncher.launch(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
            }
            else Toast.makeText(requireActivity(), "Проверьте состояние инернета", Toast.LENGTH_LONG).show()
        }
        return bindingLocalOrCity.root
    }

    private fun onGotLocationPermissionsResult(grantResults: Map<String, Boolean>){
        if (grantResults[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
            lastLocation()
        } else {
            // example of handling 'Deny & don't ask again' user choice
            if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                askUserForOpeningAppSettings()
            } else {
                Toast.makeText(requireContext(), R.string.permission_denied, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun askUserForOpeningAppSettings() {
        val appSettingsIntent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", requireActivity().packageName, null)
        )
        if (requireActivity().packageManager.resolveActivity(appSettingsIntent, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            Toast.makeText(requireActivity(), R.string.permissions_denied_forever, Toast.LENGTH_SHORT).show()
        } else {
            AlertDialog.Builder(requireContext())
                .setTitle(R.string.permission_denied)
                .setMessage(R.string.permission_denied_forever_message)
                .setPositiveButton(R.string.open) { _, _ ->
                    startActivity(appSettingsIntent)
                }
                .create()
                .show()
        }
    }


    private fun lastLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                Log.d("Log", location.toString())
                location?.let { it: Location ->
                    currentCity = City(111, "", "Локальный город", location.latitude, location.longitude)
                    viewModel.gotoWeatherinCitywithCity(currentCity)
                }
            }
        }
    }

    private fun statusInternet(): Boolean{
        val cm = requireActivity().getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
        val nInfo = cm.activeNetworkInfo
        return nInfo != null && nInfo.isAvailable && nInfo.isConnected
    }
}