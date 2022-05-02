package com.example.weather.navigator

import androidx.lifecycle.ViewModel

/**
 * Implementation of [Navigator] and [UiActions].
 * It is based on activity view-model because instances of [Navigator] and [UiActions]
 * should be available from fragments' view-models (usually they are passed to the view-model constructor).
 */
class ActivityScopeViewModel(

    val navigator: IntermediateNavigator
) : ViewModel(),
    Navigator by navigator{

    override fun onCleared() {
        super.onCleared()
        navigator.clear()
    }

}