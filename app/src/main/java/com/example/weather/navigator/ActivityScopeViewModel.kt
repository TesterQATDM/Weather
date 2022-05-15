package com.example.weather.navigator

import androidx.lifecycle.ViewModel

/**
 * Implementation of [Navigator].
 * It is based on activity view-model because instances of [Navigator]
 * should be available from fragments' view-models (usually they are passed to the view-model constructor).
 */

// mainVM
class ActivityScopeViewModel(

    val navigator: IntermediateNavigator
) : ViewModel(),
    Navigator by navigator{

    override fun onCleared() {
        super.onCleared()
        navigator.clear()
    }

}