package pl.mattiahit.androidweatherk.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pl.mattiahit.androidweatherk.models.WeatherLocation
import pl.mattiahit.androidweatherk.repositories.LocationRepository
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val locationRepository: LocationRepository) : ViewModel() {

    private var mLocations: MutableLiveData<List<WeatherLocation>> = MutableLiveData<List<WeatherLocation>>()

    init {
        this.mLocations.value = locationRepository.getLocationsFromDb()
    }

    fun getStoredLocations(): LiveData<List<WeatherLocation>> {
        return this.mLocations
    }

    fun getCurrentLocation(): WeatherLocation? {
        return this.locationRepository.getLocationFromGps()
    }
}