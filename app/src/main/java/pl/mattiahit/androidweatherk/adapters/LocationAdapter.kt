package pl.mattiahit.androidweatherk.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_location_weather.view.*
import pl.mattiahit.androidweatherk.R
import pl.mattiahit.androidweatherk.models.Location
import pl.mattiahit.androidweatherk.rest.model.WeatherResponse

class LocationAdapter(var context: Context, var locations: List<Location>): RecyclerView.Adapter<LocationAdapter.LocationHolder>() {

    private lateinit var location: Location

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationHolder {
        return LocationHolder(LayoutInflater.from(context)
            .inflate(R.layout.list_item_location_weather, parent, false))
    }

    override fun onBindViewHolder(
        holder: LocationHolder,
        position: Int
    ) {
        // TODO pogoda z lokalizacji
    }

    override fun getItemCount(): Int {
        return locations.size
    }

    inner class LocationHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun bind(weatherResponse: WeatherResponse){
            weatherResponse.weather.let {
                Picasso.get().load(it[0].icon).into(itemView.location_weather_icon)
            }
            itemView.location_weather_city_name.text = weatherResponse.name
        }
    }
}