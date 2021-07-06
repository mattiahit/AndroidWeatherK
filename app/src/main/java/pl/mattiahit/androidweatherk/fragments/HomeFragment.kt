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
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
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
    private var searchMode = false
    private lateinit var locationFromGPS: WeatherLocation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as WeatherApplication).getAppComponent().inject(this)
        this.mHomeViewModel = ViewModelProvider(this, this.mHomeViewModelFactory).get(HomeViewModel::class.java)
        this.locationAdapter = LocationAdapter(requireActivity(), weatherLocationsList) {
            val isFavourite = mHomeViewModel.isLocationExistsAsFavourities(locationNameEditText.text.toString()).subscribeOn(Schedulers.io()).map { return@map it }.blockingGet()
            if(!isFavourite){
                this.mHomeViewModel.setFavouriteLocation(WeatherLocation(Tools.getRandomLongId(), it.name, it.coord.lat, it.coord.lon, isFavourite))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this.getStoreLocationObserver())
            }else{
                this.mHomeViewModel.deleteFromFavourites(it.name)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this.getDeleteLocationObserver())
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locations_list.layoutManager = LinearLayoutManager(activity)
        locations_list.adapter = this.locationAdapter
        searchLocationBtn.setOnClickListener {
            if (!locationNameEditText.text.isBlank() && !searchMode) {
                val isFavourite = mHomeViewModel.isLocationExistsAsFavourities(locationNameEditText.text.toString()).subscribeOn(Schedulers.io()).map { return@map it }.blockingGet()
                (activity as MainActivity).hideKeyboard(view)
                weatherLocationsList.clear()
                searchMode = true
                searchLocationBtn.setImageResource(android.R.drawable.ic_menu_close_clear_cancel)
                mHomeViewModel.getWeatherForCity(locationNameEditText.text.toString())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getLoadWeatherObserver(isFavourite))
            }else if(searchMode){
                resetSearchArea()
            }
        }
        locateMeBtn.setOnClickListener {
            locationFromGPS.let {
                locationNameEditText.setText(it.locationName)
                searchLocationBtn.performClick()
            }
        }
        this.initializeLocation()
        this.initLocationsObserver()
    }

    private fun resetSearchArea() {
        searchMode = false
        mHomeViewModel.getLocationsFromDb()
        locationNameEditText.text.clear()
        searchLocationBtn.setImageResource(android.R.drawable.ic_menu_search)
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
        this.mHomeViewModel.getCurrentLocation().observe(viewLifecycleOwner,{
            Log.i("GPS", it.locationName)
            locationFromGPS = it
        })
    }

    private fun initLocationsObserver() {
        this.mHomeViewModel.getLocations().observe(requireActivity(), Observer {
            it?.let {
                weatherLocationsList.clear()
                for ( weatherLocation in it){
                    this.mHomeViewModel.getWeatherForLocation(weatherLocation)
                        .subscribeOn(Schedulers.io())
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

    private fun getDeleteLocationObserver(): DisposableSingleObserver<Int> {
        return object : DisposableSingleObserver<Int>(){
            override fun onSuccess(t: Int?) {
                Toast.makeText(requireContext(), getString(R.string.location_deleted), Toast.LENGTH_LONG).show()
                resetSearchArea()
            }

            override fun onError(e: Throwable) {
                e.message?.let { Log.e(javaClass.name, it) };
            }

        }
    }

    private fun getStoreLocationObserver(): DisposableCompletableObserver {
        return object: DisposableCompletableObserver() {
            override fun onComplete() {
                Toast.makeText(requireContext(), getString(R.string.location_saved), Toast.LENGTH_LONG).show()
                resetSearchArea()
            }

            override fun onError(e: Throwable?) {
                Toast.makeText(requireContext(), "ERROR", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getLoadWeatherObserver(isFavourite: Boolean): DisposableSingleObserver<WeatherResponse> {
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