package pl.mattiahit.androidweatherk.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pl.mattiahit.androidweatherk.WeatherApplication
import pl.mattiahit.androidweatherk.models.Location
import pl.mattiahit.androidweatherk.repositories.LocationRepository
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val locationRepository: LocationRepository) : ViewModel() {

    private var mLocations: MutableLiveData<List<Location>> = MutableLiveData<List<Location>>()

    init {
        this.mLocations.value = locationRepository.getLocations()
    }

    fun getLocations(): LiveData<List<Location>> {
        return this.mLocations
    }
}