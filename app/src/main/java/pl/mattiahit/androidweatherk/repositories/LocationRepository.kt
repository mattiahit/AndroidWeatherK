package pl.mattiahit.androidweatherk.repositories

import pl.mattiahit.androidweatherk.WeatherApplication
import pl.mattiahit.androidweatherk.database.AppDatabase
import pl.mattiahit.androidweatherk.models.Location
import pl.mattiahit.androidweatherk.rest.APIService
import javax.inject.Inject

class LocationRepository(private val application: WeatherApplication) {

    @Inject lateinit var appDatabase: AppDatabase
    @Inject lateinit var apiService: APIService

    init {
        (application as WeatherApplication).getAppComponent().inject(this)
    }

    fun getLocations():List<Location>{
        return this.appDatabase.locationDao().getAllLocations()
    }

}