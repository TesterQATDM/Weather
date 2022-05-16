package com.example.weather.navigator

import com.example.weather.base.BaseScreen

/**
 * Mediator that holds nav actions in the queue if real navigator is not active.
 * when activity is action
 * работает на стороне VM складывает фрагменты в стек очереди если активити is not active
 */
class IntermediateNavigator : Navigator {

    private val targetNavigator = MainActivityActions<Navigator>()/*stack element of navigation*/

    override fun launch(screen: BaseScreen) = targetNavigator {
        it.launch(screen)
    }

    fun setTarget(navigator: Navigator?) {
        targetNavigator.resource = navigator
    }

    fun clear() {
        targetNavigator.clear()
    }

}