package com.csci448.cmak_a3.ui.History

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.csci448.cmak_a3.R
import com.csci448.cmak_a3.data.Location
import org.w3c.dom.Text

class HistoryViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private lateinit var location: Location
    private val latitude: TextView = view.findViewById(R.id.latitude_item)
    private val longitude: TextView = view.findViewById(R.id.longitude_item)
    private val temperature: TextView = view.findViewById(R.id.temperature_item)
    private val weather: TextView = view.findViewById(R.id.weather_item)
    private val date: TextView = itemView.findViewById(R.id.timestamp)
    fun bind(location: Location, clickListener: (Location) -> Unit) {
        this.location = location
        itemView.setOnClickListener { clickListener(this.location) }

        this.location.run {
            latitude.text = Latitude.toString()
            longitude.text = Longitude.toString()
            temperature.text = Temperature.toString()
            weather.text = Weather
            date.text = time.toString()
        }
    }

    fun clear() {

    }
}