package com.example.weather.base

import android.os.Parcelable

/**
 * Base class for defining screen arguments
 */
interface BaseScreen : Parcelable{
    companion object {
        const val ARG_SCREEN = "ARG_SCREEN"
    }
}