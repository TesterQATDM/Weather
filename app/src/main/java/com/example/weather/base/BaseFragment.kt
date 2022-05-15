package com.example.weather.base

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.weather.R
import com.example.weather.databinding.ForResultBinding
import com.example.weather.asynchrony.PendingResultWeather
import com.example.weather.asynchrony.SuccessResultWeather
import com.example.weather.asynchrony.ExceptionResultWeather
import com.example.weather.asynchrony.ResultWeather
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.lang.Exception

/**
 * Base class for all fragments
 */
abstract class BaseFragment : Fragment() {

    /**
     * View-model that manages this fragment
     */
    abstract val viewModel: BaseViewModel

    fun <T> renderResult(root: ViewGroup, result: ResultWeather<T>,
                         onPending:() -> Unit,
                         onError:(Exception) -> Unit,
                         onSuccess:(T) -> Unit
    ){
        val binding = ForResultBinding.bind(root)
        (root).children.forEach { it.visibility = View.GONE }
        when(result){
            is PendingResultWeather -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            is ExceptionResultWeather ->{
                result.exception
                binding.progressBar.visibility = View.GONE
                binding.cancelAction.visibility = View.VISIBLE
            }
            is SuccessResultWeather ->{
                root.children
                    .filter { it.id != R.id.progressBar && it.id != R.id.cancel_action }
                    .forEach { it.visibility = View.VISIBLE }
                onSuccess(result.data)
            }
        }
    }

    fun <T> BaseFragment.collectFlow(flow: Flow<T>, onCollect: (T) -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            // this coroutine is cancelled in onDestroyView
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // this coroutine is launched every time when onStart is called;
                // collecting is cancelled in onStop
                flow.collect {
                    onCollect(it)
                }
            }
        }
    }
}