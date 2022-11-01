package pl.mattiahit.androidweatherk.home.ui.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.SingleObserver
import pl.mattiahit.androidweatherk.R
import pl.mattiahit.androidweatherk.home.data.model.ForecastDataLocal
import pl.mattiahit.androidweatherk.database.entities.WeatherLocation
import pl.mattiahit.androidweatherk.home.data.repository.LocationRepository
import pl.mattiahit.androidweatherk.home.data.repository.WeatherRepository
import pl.mattiahit.androidweatherk.rest.model.ForecastData
import pl.mattiahit.androidweatherk.rest.model.ForecastResponse
import pl.mattiahit.androidweatherk.rest.model.WeatherResponse
import pl.mattiahit.androidweatherk.utils.SchedulerProvider
import pl.mattiahit.androidweatherk.utils.TimeProvider
import pl.mattiahit.androidweatherk.utils.Tools
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val locationRepository: LocationRepository,
                                        private val weatherRepository: WeatherRepository,
                                        private val schedulerProvider: SchedulerProvider,
                                        private val timeProvider: TimeProvider)
    : ViewModel() {

    fun getCurrentLocation(): LiveData<WeatherLocation> {
        return this.locationRepository.getLocationFromGps()
    }

    fun getWeatherForCity(cityName: String, observer: SingleObserver<WeatherResponse>) {
        if(cityName.isNotEmpty()) {
            this.weatherRepository.getWeatherForCity(cityName)
                .subscribeOn(schedulerProvider.subscribeScheduler)
                .observeOn(schedulerProvider.observeOnScheduler)
                .timeout(timeProvider.getApiResponseTimeoutSeconds(), TimeUnit.SECONDS)
                .onErrorReturnItem(
                    this.errorWeatherResponseWithMessage("Something went wrong...")
                )
                .subscribe(observer)
        } else {
            observer.onSuccess(
                this.errorWeatherResponseWithMessage("City Name Should Not Be empty")
            )
        }
    }

    fun getForecastForCity(cityName: String, observer: SingleObserver<ForecastResponse>) {
        if(cityName.isNotEmpty()){
            this.weatherRepository.getForecastForCity(cityName)
                .subscribeOn(schedulerProvider.subscribeScheduler)
                .observeOn(schedulerProvider.observeOnScheduler)
                .timeout(timeProvider.getApiResponseTimeoutSeconds(), TimeUnit.SECONDS)
                .onErrorReturnItem(
                    this.errorForecastResponseWithMessage("Something went wrong...")
                )
                .subscribe(observer)
        } else {
            observer.onSuccess(
                this.errorForecastResponseWithMessage("City Name Should Not Be empty")
            )
        }

    }

    fun getForecastDataLocalFromForecastResponse(t: ForecastResponse, context: Context): List<ForecastDataLocal> {
        val result = mutableListOf<ForecastDataLocal>()
        var date = ""
        t.list?.let {
            for(fData:ForecastData in it){
                if(fData.dt_txt.contains("12:00")) {
                    val tempData = Tools.getDayAndMonthFromTimestamp(fData.dt)
                    if(date !== tempData){
                        date = tempData
                        result.add(
                            ForecastDataLocal(
                            date,
                            context.resources.getString(R.string.degree_scale, (fData.main.temp - 273).toInt()),
                            getDrawableFromName(fData.weather[0].main, context))
                        )
                    }
                }
            }
        }
        return result
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun getDrawableFromName(name: String, context: Context): Drawable =
        when(name) {
            "Clouds" -> {
                context.resources.getDrawable(R.drawable.cloudy, context.theme)
            }
            "Clear" -> {
                context.resources.getDrawable(R.drawable.clear, context.theme)
            }
            "Rain" -> {
                context.resources.getDrawable(R.drawable.rainy, context.theme)
            }
            "Thunderstorm" -> {
                context.resources.getDrawable(R.drawable.stormy, context.theme)
            }
            "Mist" -> {
                context.resources.getDrawable(R.drawable.foog, context.theme)
            }
            "Snow" -> {
                context.resources.getDrawable(R.drawable.snowy, context.theme)
            }
            else -> context.resources.getDrawable(R.drawable.cloud, context.theme)
        }

    private fun errorWeatherResponseWithMessage(message: String): WeatherResponse =
        WeatherResponse(
            null,
            null,
            -1,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            false,
            message
        )

    private fun errorForecastResponseWithMessage(message: String): ForecastResponse =
        ForecastResponse(
            null,
            0,
            -1,
            null,
            message
        )
}