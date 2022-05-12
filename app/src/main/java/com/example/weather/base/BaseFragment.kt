package com.example.weather.base

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.example.weather.R
import com.example.weather.databinding.ForResultBinding
import com.example.weather.asynchrony.PendingResultWeather
import com.example.weather.asynchrony.SuccessResultWeather
import com.example.weather.asynchrony.ExceptionResultWeather
import com.example.weather.asynchrony.ResultWeather
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
            //onSuccess(result.data)
        }
    }
}