package com.example.weather.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * Convert mutable live-data into non-mutable live-data.
 */
fun <T> MutableLiveData<T>.share(): LiveData<T> = this
