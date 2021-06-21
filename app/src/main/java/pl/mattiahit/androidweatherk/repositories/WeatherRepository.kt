package pl.mattiahit.androidweatherk.repositories

import io.reactivex.rxjava3.core.Single
import pl.mattiahit.androidweatherk.models.WeatherLocation
import pl.mattiahit.androidweatherk.rest.APIService
import pl.mattiahit.androidweatherk.rest.BaseRestTask
import pl.mattiahit.androidweatherk.rest.model.WeatherResponse
import javax.inject.Inject

class WeatherRepository {

    @Inject
    lateinit var apiService: APIService

    fun getWeatherForLocation(location: WeatherLocation) : Single<WeatherResponse> {
        return this.apiService.getWeatherForLocation(location.locationLat, location.locationLon, BaseRestTask.API_KEY)
    }

    fun getWeatherForCity(city: String) : Single<WeatherResponse> {
        return this.apiService.getWeatherForCity(city, BaseRestTask.API_KEY)
    }
}