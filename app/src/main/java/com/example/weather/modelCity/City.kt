package com.example.weather.modelCity

import java.io.Serializable

data class City(
    val id: Int,
    val name: String,
    val description: String,
    val mLatitudeTextView: Double,
    val mLongitudeTextView: Double

): Serializable{
    override fun toString(): String {
        return description
    }
}