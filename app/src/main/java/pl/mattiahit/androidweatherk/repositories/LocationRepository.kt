package pl.mattiahit.androidweatherk.repositories

import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import pl.mattiahit.androidweatherk.WeatherApplication
import pl.mattiahit.androidweatherk.database.AppDatabase
import pl.mattiahit.androidweatherk.models.Location
import pl.mattiahit.androidweatherk.rest.APIService
import pl.mattiahit.androidweatherk.rest.BaseRestTask
import pl.mattiahit.androidweatherk.rest.model.WeatherResponse
import javax.inject.Inject

class LocationRepository(application: WeatherApplication) {

    @Inject lateinit var appDatabase: AppDatabase
    @Inject lateinit var apiService: APIService

    init {
        application.getAppComponent().inject(this)
    }

    fun getLocations():List<Location>{
        return this.appDatabase.locationDao().getAllLocations()
    }

    fun getWeatherForLocation(lat: Double, lon: Double) : Single<WeatherResponse>{
       return this.apiService.getWeatherForLocation(lat, lon, BaseRestTask.API_KEY)
    }

    fun getWeatherForCity(city: String) : Single<WeatherResponse>{
        return this.apiService.getWeatherForCity(city, BaseRestTask.API_KEY)
    }

}