package com.example.weather

import android.Manifest
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather.databinding.FragmentLocalOrCityBinding
import com.example.weather.modelCity.City
import com.example.weather.modelCity.CityListViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class LocalOrCityFragment : Fragment(){

    private lateinit var bindingLocalOrCity: FragmentLocalOrCityBinding
    private lateinit var currentCity: City
    private lateinit var adapter: CityAdapter
    private val viewModel: CityListViewModel by viewModels {factory()}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingLocalOrCity = FragmentLocalOrCityBinding.inflate(inflater, container, false)

        adapter = CityAdapter(object : CityActionListener {
            override fun details(city: City) {
                contract().launchWeatherCity(city)
            }
        })
        bindingLocalOrCity.rcItem.layoutManager = LinearLayoutManager(requireContext())
        bindingLocalOrCity.rcItem.adapter = adapter
        viewModel.cities.observe(viewLifecycleOwner, Observer {
            adapter.cities = it
        })

        bindingLocalOrCity.local.setOnClickListener{
            if (statusInternet()) checkLastLocation()
            else Toast.makeText(requireActivity(), "Проверьте состояние инернета", Toast.LENGTH_LONG).show()
        }
        return bindingLocalOrCity.root
    }

    private fun checkLastLocation() {
        val permission = ActivityCompat.checkSelfPermission(
            requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)

        // If permission is granted
        if (permission == PackageManager.PERMISSION_GRANTED) {
            lastLocation()
        } else {
            // If permission has not been granted, request permission
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), LOCATION_PERMISSION_REQUEST
            )
        }
    }

    private fun lastLocation() {
        val fusedLocationClient: FusedLocationProviderClient = LocationServices
            .getFusedLocationProviderClient(requireActivity())
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener(
                    requireActivity()
                ) { loc ->
                    Log.d("Log", "${loc.latitude} + ${loc.longitude}")
                    currentCity = City(20,"","",loc.latitude,loc.longitude)
                    contract().launchWeatherCity(currentCity)
                }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "Location Permission Denied", Toast.LENGTH_LONG).show()
            } else {
                lastLocation()
            }
        }
    }

    private fun statusInternet(): Boolean{
        val cm = requireActivity().getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
        val nInfo = cm.activeNetworkInfo
        return nInfo != null && nInfo.isAvailable && nInfo.isConnected
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST = 1
    }
}