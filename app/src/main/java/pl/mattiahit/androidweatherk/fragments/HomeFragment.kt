package pl.mattiahit.androidweatherk.fragments

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableCompletableObserver
import kotlinx.android.synthetic.main.fragment_home.*
import pl.mattiahit.androidweatherk.MainActivity
import pl.mattiahit.androidweatherk.R
import pl.mattiahit.androidweatherk.WeatherApplication
import pl.mattiahit.androidweatherk.adapters.LocationAdapter
import pl.mattiahit.androidweatherk.models.WeatherLocation
import pl.mattiahit.androidweatherk.rest.model.WeatherResponse
import pl.mattiahit.androidweatherk.utils.Tools
import pl.mattiahit.androidweatherk.viewmodels.HomeViewModel
import pl.mattiahit.androidweatherk.viewmodels.factories.HomeViewModelFactory
import java.util.ArrayList
import javax.inject.Inject

class HomeFragment : Fragment(R.layout.fragment_home) {

    @Inject lateinit var mHomeViewModelFactory: HomeViewModelFactory
    lateinit var mHomeViewModel: HomeViewModel
    private lateinit var locationAdapter: LocationAdapter
    private var weatherLocationsList = ArrayList<WeatherResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as WeatherApplication).getAppComponent().inject(this)
        this.mHomeViewModel = ViewModelProvider(this, this.mHomeViewModelFactory).get(HomeViewModel::class.java)
        this.locationAdapter = LocationAdapter(requireActivity(), weatherLocationsList) {
            if(!it.isFavourite){
                this.mHomeViewModel.setFavouriteLocation(WeatherLocation(Tools.getRandomLongId(), it.name, it.coord.lat, it.coord.lon, true))
                    .subscribeOn(io.reactivex.rxjava3.schedulers.Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this.getStoreLocationObserver())
            }else{
                // TODO Delete from list
            }

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locations_list.layoutManager = LinearLayoutManager(activity)
        locations_list.adapter = this.locationAdapter
        searchLocationBtn.setOnClickListener {
            if (!locationNameEditText.text.isBlank()) {
                weatherLocationsList.clear()
                mHomeViewModel.getWeatherForCity(locationNameEditText.text.toString())
                    .subscribeOn(io.reactivex.rxjava3.schedulers.Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getLoadWeatherObserver(false))
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
        this.mHomeViewModel.getStoredLocations().observe(requireActivity(), Observer {
            it?.let {
                weatherLocationsList.clear()
                for ( weatherLocation in it){
                    this.mHomeViewModel.getWeatherForLocation(weatherLocation)
                        .subscribeOn(io.reactivex.rxjava3.schedulers.Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getLoadWeatherObserver(weatherLocation.isFavourite))
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

    private fun getStoreLocationObserver(): DisposableCompletableObserver {
        return object: DisposableCompletableObserver() {
            override fun onComplete() {
                Toast.makeText(requireContext(), "Location stored!", Toast.LENGTH_LONG).show()
            }

            override fun onError(e: Throwable?) {
                Toast.makeText(requireContext(), "ERROR", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getLoadWeatherObserver(isFavourite: Boolean): io.reactivex.rxjava3.observers.DisposableSingleObserver<WeatherResponse> {
        return object : io.reactivex.rxjava3.observers.DisposableSingleObserver<WeatherResponse>() {
            override fun onSuccess(value: WeatherResponse) {
                value.isFavourite = isFavourite
                weatherLocationsList.add(value)
                locationAdapter.notifyDataSetChanged()
            }

            override fun onError(e: Throwable) {
                e.message?.let { Log.e(javaClass.name, it) };
            }
        }
    }
}