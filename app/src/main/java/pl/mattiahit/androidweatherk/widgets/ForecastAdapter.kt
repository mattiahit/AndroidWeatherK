package pl.mattiahit.androidweatherk.widgets

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.mattiahit.androidweatherk.R
import pl.mattiahit.androidweatherk.models.ForecastDataLocal

class ForecastAdapter(private val forecastList: List<ForecastDataLocal>):
    RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {

    inner class ForecastViewHolder(private val forecastItemView: View):
            RecyclerView.ViewHolder(forecastItemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.forecast_data_widget,
            parent,
            false)
        return ForecastViewHolder(view)
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        holder.itemView.apply {
            this.findViewById<ImageView>(R.id.iv_forecast_data_image).setImageDrawable(forecastList[position].weatherDrawable)
            this.findViewById<TextView>(R.id.tv_forecast_data_date).text = forecastList[position].date
            this.findViewById<TextView>(R.id.tv_forecast_data_temperature).text = forecastList[position].tempString
        }
    }

    override fun getItemCount(): Int {
        return forecastList.size
    }
}