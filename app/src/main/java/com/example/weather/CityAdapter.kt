package com.example.weather

import android.text.Layout
import android.view.*
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.weather.databinding.CityItemBinding
import com.example.weather.modelCity.City
import kotlinx.android.synthetic.main.city_item.view.*

interface CityActionListener{

    fun onCityMove(city: City, moveBy: Int)

    fun details(city: City)

    fun deleteCity(city: City)
}

class CityAdapter(
    private val actionListener: CityActionListener
) : RecyclerView.Adapter<CityAdapter.MyViewHolder>(), View.OnClickListener {

        var cities: List<City> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    override fun onClick(v: View) {

        if (v.tag is City)
            when(v.id) {
                R.id.textView ->
                    if (v.findViewById<TextView>(R.id.textView).text.equals("Список городов"))
                        Toast.makeText(v.context, "Список городов", Toast.LENGTH_SHORT).show()
                R.id.delCity -> {
                    showPopupMenu(v)
                }
                else -> {
                    val city = v.tag as City
                    actionListener.details(city)
                }
            }
        else
            if (v.findViewById<TextView>(R.id.textView).text.equals("Список городов"))
                Toast.makeText(v.context, "Список городов", Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(v.context, cities.size.toString(), Toast.LENGTH_SHORT).show()

    }

    open class MyViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

        class CityViewHolder(binding: CityItemBinding) : MyViewHolder(binding)

        class HeaderViewHolder(binding: CityItemBinding) : MyViewHolder(binding)

        class FooterViewHolder(binding: CityItemBinding) : MyViewHolder(binding)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        when(holder){
            is MyViewHolder.CityViewHolder -> {
                val city = cities[position - 1]
                holder.itemView.tag = city
                holder.itemView.delCity.tag = city
                holder.itemView.textView.text = city.description
            }
            is MyViewHolder.HeaderViewHolder -> {
                holder.itemView.delCity.visibility = View.GONE
                holder.itemView.textView.text = "Список городов"
                holder.itemView.textView.gravity = Gravity.CENTER
            }
            is MyViewHolder.FooterViewHolder -> {
                val size = cities.size
                holder.itemView.delCity.visibility = View.GONE
                holder.itemView.textView.text = "К-во городов $size"
                holder.itemView.textView.gravity = Gravity.CENTER
            }
        }
    }

    override fun getItemCount() = cities.size + 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CityItemBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(this)
        binding.delCity.setOnClickListener(this)
        return when(viewType) {
            TYPE_HEADER -> MyViewHolder.HeaderViewHolder(binding)
            TYPE_FOOTER -> MyViewHolder.FooterViewHolder(binding)
            TYPE_CITY -> MyViewHolder.CityViewHolder(binding)
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(position){
            0 -> TYPE_HEADER
            itemCount - 1 -> TYPE_FOOTER
            else -> TYPE_CITY
        }
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(view.context, view)
        val context = view.context
        val user = view.tag as City
        val position = cities.indexOfFirst { it.id == user.id }

        popupMenu.menu.add(0, ID_MOVE_UP, Menu.NONE, context.getString(R.string.move_up)).apply {
            isEnabled = position > 0
        }
        popupMenu.menu.add(0, ID_MOVE_DOWN, Menu.NONE, context.getString(R.string.move_down)).apply {
            isEnabled = position < cities.size - 2
        }
        popupMenu.menu.add(0, ID_REMOVE, Menu.NONE, context.getString(R.string.remove))

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                ID_MOVE_UP -> {
                    actionListener.onCityMove(user, -1)
                }
                ID_MOVE_DOWN -> {
                    actionListener.onCityMove(user, 1)
                }
                ID_REMOVE -> {
                    actionListener.deleteCity(user)
                }
            }
            return@setOnMenuItemClickListener true
        }

        popupMenu.show()
    }

    companion object{
        const val TYPE_HEADER = 0
        const val TYPE_CITY = 1
        const val TYPE_FOOTER = 2
        private const val ID_MOVE_UP = 1
        private const val ID_MOVE_DOWN = 2
        private const val ID_REMOVE = 3
    }
}