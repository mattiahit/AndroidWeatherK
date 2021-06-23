package pl.mattiahit.androidweatherk.fragments

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_home.*
import pl.mattiahit.androidweatherk.MainActivity
import pl.mattiahit.androidweatherk.R
import pl.mattiahit.androidweatherk.WeatherApplication
import pl.mattiahit.androidweatherk.adapters.LocationAdapter
import pl.mattiahit.androidweatherk.viewmodels.HomeViewModel
import pl.mattiahit.androidweatherk.viewmodels.factories.HomeViewModelFactory
import javax.inject.Inject

class HomeFragment : Fragment(R.layout.fragment_home) {

    @Inject lateinit var mHomeViewModelFactory: HomeViewModelFactory
    lateinit var mHomeViewModel: HomeViewModel
    private lateinit var locationAdapter: LocationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as WeatherApplication).getAppComponent().inject(this)
        this.mHomeViewModel = ViewModelProvider(this, this.mHomeViewModelFactory).get(HomeViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchLocationBtn.setOnClickListener {
            if (!locationNameEditText.text.isBlank()) {
                mHomeViewModel.getWeatherForCity(locationNameEditText.text.toString())
                    .doOnSuccess { response->
                        locationAdapter = LocationAdapter(requireActivity(), listOf(response))
                        locations_list.adapter = locationAdapter
                    }
            }
        }
        this.initializeLocation()
        this.loadStoredLocations()
    }

    private fun initializeLocation() {
        if(ContextCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION),
                MainActivity.PERMISSION_REQUEST_LOCATION
            )
        }else{
            Toast.makeText(activity, "Location Permission Granted", Toast.LENGTH_SHORT).show()
            requestLocationUpdates()
        }
    }

    private fun requestLocationUpdates() {
//        this.mHomeViewModel.getCurrentLocation().observe(this,{
//            Log.i("GPS", it.locationName)
//        })
    }

    private fun loadStoredLocations() {
        this.mHomeViewModel.getStoredLocations().observe(this, Observer {
            it?.let {
                for ( weatherLocation in it){
                    this.mHomeViewModel.getWeatherForLocation(weatherLocation).doOnSuccess { weatherResponse->
                        locationAdapter = LocationAdapter(requireActivity(), listOf(weatherResponse))
                        locations_list.adapter = locationAdapter
                    }
                }
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            MainActivity.PERMISSION_REQUEST_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    this.requestLocationUpdates()
                }
            }
        }
    }
}