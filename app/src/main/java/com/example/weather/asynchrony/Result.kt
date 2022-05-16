package com.example.weather.asynchrony

import java.lang.Exception


/**
 * ResultWeather class for async action
 */

typealias Mapper<Input, Output> = (Input) -> Output

sealed class ResultWeather<T> {

    fun<R> map(mapper: Mapper<T, R>? = null): ResultWeather<R> = when(this){
        is PendingResultWeather -> PendingResultWeather()
        is ExceptionResultWeather -> ExceptionResultWeather(this.exception)
        is SuccessResultWeather -> {
            if (mapper == null) throw IllegalArgumentException("Error")
            SuccessResultWeather(mapper(this.data))
        }
    }
}

class PendingResultWeather<T>: ResultWeather<T>()

class SuccessResultWeather<T>(
    val data: T
): ResultWeather<T>()

class ExceptionResultWeather<T>(
    val exception: Exception
) : ResultWeather<T>()