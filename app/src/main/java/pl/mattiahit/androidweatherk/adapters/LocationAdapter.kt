package pl.mattiahit.androidweatherk.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_location_weather.view.*
import pl.mattiahit.androidweatherk.R
import pl.mattiahit.androidweatherk.rest.model.WeatherResponse

class LocationAdapter(var context: Context, var weatherResponses: List<WeatherResponse>, val listener: (WeatherResponse) -> Unit): RecyclerView.Adapter<LocationAdapter.LocationHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationHolder {
        return LocationHolder(LayoutInflater.from(context)
            .inflate(R.layout.list_item_location_weather, parent, false))
    }

    override fun onBindViewHolder(
        holder: LocationHolder,
        position: Int
    ) {
        holder.bind(weatherResponses[position])
    }

    override fun getItemCount(): Int {
        return weatherResponses.size
    }

    inner class LocationHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun bind(weatherResponse: WeatherResponse){
            weatherResponse.weather?.let {
                Picasso.get().load("http://openweathermap.org/img/w/" + it[0].icon + ".png").into(itemView.location_weather_icon)
            }
            itemView.location_weather_city_name.text = weatherResponse.name
            itemView.location_weather_temperature.text = context.resources.getString(R.string.degree_scale,
                if(weatherResponse.main != null) (weatherResponse.main.temp - 273).toInt() else -1)
            itemView.location_weather_wind.text = context.resources.getString(R.string.wind_speed_scale,
                if(weatherResponse.wind != null) weatherResponse.wind.speed.toInt() else -1)
            itemView.location_weather_clouds.text = context.resources.getString(R.string.clouds_scale,
                if(weatherResponse.clouds != null) weatherResponse.clouds.all else -1)
            itemView.location_weather_pressure.text = context.resources.getString(R.string.pressure_scale,
                if(weatherResponse.main != null) weatherResponse.main.pressure else -1)
//            if(weatherResponse.isFavourite){
//                itemView.manage_favourites_btn.setImageResource(android.R.drawable.btn_star_big_on)
//            }else{
//                itemView.manage_favourites_btn.setImageResource(android.R.drawable.btn_star_big_off)
//            }
            itemView.manage_favourites_btn.setOnClickListener {
                listener(weatherResponse)
            };
        }
    }
}