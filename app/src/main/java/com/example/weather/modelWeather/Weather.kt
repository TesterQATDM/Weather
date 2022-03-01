package com.example.weather.modelWeather

data class Weather(
    var coord :Map<String, String>,
    var weather: List<Map<String, String>>,
    var base: String,
    var main: Map<String, String>,
    var visibility: String,
    var wind: Map<String, String>,
    var clouds: Map<String, String>,
    var dt: String,
    var sys: Map<String, String>,
    var timezone: String,
    var id: String,
    var name: String,
    var cod: String
)