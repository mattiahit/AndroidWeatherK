package pl.mattiahit.androidweatherk.livedata

import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import pl.mattiahit.androidweatherk.models.WeatherLocation
import pl.mattiahit.androidweatherk.utils.Tools
import java.util.*


class LocationLiveData(private val context: Context) : LiveData<WeatherLocation>() {

    private var fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            locationResult ?: return
            Log.i("GPS", locationResult.toString())
            for (location in locationResult.locations){
                setLocationData(location)
            }
        }
    }

    private fun setLocationData(location: Location?) {
        location?.let {
            value = WeatherLocation(Tools.getRandomLongId(), getLocationNameByLatLon(it.latitude, it.longitude), it.latitude, it.longitude)
        }
    }

    private fun getLocationNameByLatLon(lat: Double, lon: Double): String{
        val gcd = Geocoder(context, Locale.getDefault())
        val addresses: List<Address> = gcd.getFromLocation(lat, lon, 1)
        return if(addresses.isNotEmpty())
            addresses[0].locality
        else ""
    }

    override fun onActive() {
        super.onActive()
        Log.i("GPS", "ACTIVE!!!")
        if(ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location ->
                location.also {
                    setLocationData(it)
                }
            }
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }

    override fun onInactive() {
        super.onInactive()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    companion object {
        const val ONE_MINUTE: Long = 60000
        val locationRequest : LocationRequest = LocationRequest.create().apply {
            interval = ONE_MINUTE
            fastestInterval = ONE_MINUTE/4
            priority = LocationRequest.PRIORITY_LOW_POWER
        }
    }
}