package pl.mattiahit.androidweatherk.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import pl.mattiahit.androidweatherk.R
import pl.mattiahit.androidweatherk.models.ForecastDataLocal

@SuppressLint("ViewConstructor", "InflateParams")
class ForecastDataView(context: Context, forecastData: ForecastDataLocal) : LinearLayout(context) {

    init {
        val view = LayoutInflater.from(context). inflate(R.layout.forecast_data_widget, null)
        view.findViewById<ImageView>(R.id.iv_forecast_data_image).setImageDrawable(forecastData.weatherDrawable)
        view.findViewById<TextView>(R.id.tv_forecast_data_date).text = forecastData.date
        view.findViewById<TextView>(R.id.tv_forecast_data_temperature).text = forecastData.tempString
        this.setPadding(5, 0, 5, 0)
        this.gravity = Gravity.CENTER
        this.addView(view)
    }
}