package pl.mattiahit.androidweatherk.repositories

import pl.mattiahit.androidweatherk.WeatherApplication
import pl.mattiahit.androidweatherk.database.AppDatabase
import pl.mattiahit.androidweatherk.livedata.LocationLiveData
import pl.mattiahit.androidweatherk.models.WeatherLocation
import javax.inject.Inject

class LocationRepository(val application: WeatherApplication) {

    @Inject lateinit var appDatabase: AppDatabase

    private val locationLiveData: LocationLiveData = LocationLiveData(application)

    init {
        application.getAppComponent().inject(this)
    }

    fun getLocationsFromDb(): List<WeatherLocation>? {
        return this.appDatabase.locationDao().getAllLocations().value
    }

    fun getLocationFromGps(): WeatherLocation? {
        return this.locationLiveData.value
    }

}