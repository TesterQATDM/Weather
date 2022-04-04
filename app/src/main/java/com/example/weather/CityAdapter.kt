package com.example.weather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.databinding.CityItemBinding
import com.example.weather.modelCity.City


interface CityActionListener{

    fun details(city: City)
}

class CityAdapter(
    private val actionListener: CityActionListener
) : RecyclerView.Adapter<CityAdapter.MyViewHolder>(), View.OnClickListener {

    var cities: List<City> = emptyList()
        set(new) {
            field = new
            notifyDataSetChanged()
        }
    override fun onClick(v: View) {
        val city = v.tag as City
        actionListener.details(city)
    }

    class MyViewHolder(val binding: CityItemBinding) : RecyclerView.ViewHolder(binding.root) {}

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val city = cities[position]
        holder.itemView.tag = city
        holder.binding.textView.text = city.description
    }

    override fun getItemCount() = cities.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CityItemBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(this)
        return MyViewHolder(binding)
    }
}