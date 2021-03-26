package pl.mattiahit.androidweatherk.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pl.mattiahit.androidweatherk.models.Location

class HomeViewModel : ViewModel() {

    private lateinit var mLocations: MutableLiveData<List<Location>>

    fun getLocations(): LiveData<List<Location>> {
        return this.mLocations
    }
}