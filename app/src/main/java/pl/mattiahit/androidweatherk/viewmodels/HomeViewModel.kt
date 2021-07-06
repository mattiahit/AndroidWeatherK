package pl.mattiahit.androidweatherk.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import pl.mattiahit.androidweatherk.models.WeatherLocation
import pl.mattiahit.androidweatherk.repositories.LocationRepository
import pl.mattiahit.androidweatherk.repositories.WeatherRepository
import pl.mattiahit.androidweatherk.rest.model.WeatherResponse
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val locationRepository: LocationRepository, private val weatherRepository: WeatherRepository) : ViewModel() {

    private var mLocations: MutableLiveData<List<WeatherLocation>> = MutableLiveData<List<WeatherLocation>>()

    init {
        this.getLocationsFromDb()
    }

    fun getLocationsFromDb() {
        locationRepository.getLocationsFromDb()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<List<WeatherLocation>>{
                override fun onSubscribe(d: Disposable?) {
                }

                override fun onSuccess(t: List<WeatherLocation>?) {
                    mLocations.value = t
                }

                override fun onError(e: Throwable?) {
                    Log.e(javaClass.name, "Error Getting Locations From base")
                }
            })
    }

    fun getLocations(): LiveData<List<WeatherLocation>> {
        return mLocations
    }

    fun setFavouriteLocation(weatherLocation: WeatherLocation): Completable {
        return locationRepository.setLocationToDb(weatherLocation)
    }

    fun deleteFromFavourites(name: String): Single<Int> {
        return locationRepository.deleteLocationFromDb(name)
    }

    fun getCurrentLocation(): LiveData<WeatherLocation> {
        return this.locationRepository.getLocationFromGps()
    }

    fun getWeatherForCity(cityName: String): Single<WeatherResponse> {
        return this.weatherRepository.getWeatherForCity(cityName);
    }

    fun getWeatherForLocation(location: WeatherLocation): Single<WeatherResponse> {
        return this.weatherRepository.getWeatherForLocation(location);
    }

    fun isLocationExistsAsFavourities(name: String): Single<Boolean> {
        return this.locationRepository.isLocationNameExistsInDb(name)
    }
}