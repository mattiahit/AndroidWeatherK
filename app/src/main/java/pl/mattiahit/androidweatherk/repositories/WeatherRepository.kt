package pl.mattiahit.androidweatherk.repositories

import io.reactivex.rxjava3.core.Single
import pl.mattiahit.androidweatherk.WeatherApplication
import pl.mattiahit.androidweatherk.models.WeatherLocation
import pl.mattiahit.androidweatherk.rest.APIService
import pl.mattiahit.androidweatherk.rest.BaseRestTask
import pl.mattiahit.androidweatherk.rest.model.ForecastResponse
import pl.mattiahit.androidweatherk.rest.model.WeatherResponse
import javax.inject.Inject

class WeatherRepository(val application: WeatherApplication) {

    @Inject
    lateinit var apiService: APIService

    init {
        application.getAppComponent().inject(this)
    }

    fun getWeatherForLocation(location: WeatherLocation) : Single<WeatherResponse> {
        return this.apiService.getWeatherForLocation(location.locationLat, location.locationLon, BaseRestTask.API_KEY)
    }

    fun getWeatherForCity(city: String) : Single<WeatherResponse> {
        return this.apiService.getWeatherForCity(city, BaseRestTask.API_KEY)
    }

    fun getForecastForCity(city: String): Single<ForecastResponse> {
        return this.apiService.getForecastForCity(city, BaseRestTask.API_KEY)
    }


}