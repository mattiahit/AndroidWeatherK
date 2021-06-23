package pl.mattiahit.androidweatherk.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Single
import pl.mattiahit.androidweatherk.models.WeatherLocation
import pl.mattiahit.androidweatherk.repositories.LocationRepository
import pl.mattiahit.androidweatherk.repositories.WeatherRepository
import pl.mattiahit.androidweatherk.rest.model.WeatherResponse
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val locationRepository: LocationRepository, private val weatherRepository: WeatherRepository) : ViewModel() {

    private var mLocations: MutableLiveData<List<WeatherLocation>> = MutableLiveData<List<WeatherLocation>>()

    init {
        this.mLocations.value = locationRepository.getLocationsFromDb()
    }

    fun getStoredLocations(): LiveData<List<WeatherLocation>> {
        return this.mLocations
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
}