package com.example.weather.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.asynchrony.ExceptionResultWeather
import com.example.weather.asynchrony.ResultWeather
import com.example.weather.asynchrony.SuccessResultWeather
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Base class for all view-models.
 */
typealias LiveResult<T> = LiveData<ResultWeather<T>>
typealias MutableLiveResult<T> = MutableLiveData<ResultWeather<T>>
typealias MediatorLiveResult<T> = MediatorLiveData<Result<T>>

open class BaseViewModel : ViewModel() {
    /**
     * Override this method in child classes if you want to listen for results
     * from other screens
     */
/*
    open fun onResult(result: Any) {
    }

*/
    override fun onCleared() {
        super.onCleared()
        clearScope()
    }

    private val coroutineContext = Dispatchers.Main.immediate + CoroutineExceptionHandler { _, throwable ->
        // you can add some exception handling here
    }
    protected val job = SupervisorJob()
    // custom scope which cancels jobs immediately when back button is pressed
    protected val viewModelScope = CoroutineScope(coroutineContext)



    fun <T> into(liveResult: MutableLiveResult<T>, block: suspend () -> T) {
        viewModelScope.launch {
            try {
                liveResult.postValue(SuccessResultWeather(block()))
            } catch (e: Exception) {
                if (e !is CancellationException) liveResult.postValue(ExceptionResultWeather(e))
            }
        }
    }

    private fun clearScope() {
        viewModelScope.cancel()
    }
}