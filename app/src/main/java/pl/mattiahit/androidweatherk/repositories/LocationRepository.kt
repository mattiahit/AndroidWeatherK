package pl.mattiahit.androidweatherk.repositories

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import pl.mattiahit.androidweatherk.WeatherApplication
import pl.mattiahit.androidweatherk.database.AppDatabase
import pl.mattiahit.androidweatherk.livedata.LocationLiveData
import pl.mattiahit.androidweatherk.models.WeatherLocation
import javax.inject.Inject

class LocationRepository(val application: WeatherApplication) {

    @Inject lateinit var appDatabase: AppDatabase
    @Inject lateinit var locationLiveData: LocationLiveData

    init {
        application.getAppComponent().inject(this)
    }

    fun getLocationsFromDb(): Single<List<WeatherLocation>> {
        return this.appDatabase.locationDao().getAllLocations()
    }

    fun setLocationToDb(location: WeatherLocation): Completable {
        return this.appDatabase.locationDao().insertLocation(location)
    }

    fun isLocationNameExistsInDb(name: String): Single<Boolean> {
        return this.appDatabase.locationDao().isLocationExists(name)
    }

    fun getLocationFromGps(): LocationLiveData {
        return this.locationLiveData
    }

}