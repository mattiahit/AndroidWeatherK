package pl.mattiahit.androidweatherk.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import pl.mattiahit.androidweatherk.R
import pl.mattiahit.androidweatherk.enums.DayTime
import pl.mattiahit.androidweatherk.models.ForecastDataLocal
import pl.mattiahit.androidweatherk.models.WeatherLocation
import pl.mattiahit.androidweatherk.repositories.LocationRepository
import pl.mattiahit.androidweatherk.repositories.WeatherRepository
import pl.mattiahit.androidweatherk.rest.model.ForecastData
import pl.mattiahit.androidweatherk.rest.model.ForecastResponse
import pl.mattiahit.androidweatherk.rest.model.WeatherResponse
import pl.mattiahit.androidweatherk.utils.SchedulerProvider
import pl.mattiahit.androidweatherk.utils.Tools
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val locationRepository: LocationRepository,
                                        private val weatherRepository: WeatherRepository,
                                        private val schedulerProvider: SchedulerProvider)
    : ViewModel() {

    private var _forecastData: MutableLiveData<ForecastResponse> = MutableLiveData<ForecastResponse>()
    var forecastData: LiveData<ForecastResponse> = _forecastData
    private var _dayTimeResourceData: MutableLiveData<DayTime> = MutableLiveData()
    var dayTimeResourceData: LiveData<DayTime> = _dayTimeResourceData


    @SuppressLint("SimpleDateFormat")
    private fun checkDayTime() {
        val df = SimpleDateFormat("HH")
        val time = df.format(Calendar.getInstance().time)
        when(time.toInt()) {
            in 0..5 -> _dayTimeResourceData.value = DayTime.NIGHT
            in 5..8 -> _dayTimeResourceData.value = DayTime.DAWN
            in 8..12 -> _dayTimeResourceData.value = DayTime.MORNING
            in 12..17 -> _dayTimeResourceData.value = DayTime.MIDDAY
            in 17..20 -> _dayTimeResourceData.value = DayTime.DUSK
            in 20..23 -> _dayTimeResourceData.value = DayTime.NIGHT
        }
    }

    fun getLocationsFromDb() {
        locationRepository.getLocationsFromDb()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<List<WeatherLocation>>{
                override fun onSubscribe(d: Disposable) {
                    TODO("Not yet implemented")
                }

                override fun onSuccess(t: List<WeatherLocation>) {
                    TODO("Not yet implemented")
                }

                override fun onError(e: Throwable) {
                    TODO("Not yet implemented")
                }

            })
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

    fun getWeatherForCity(cityName: String, observer: SingleObserver<WeatherResponse>) {
        if(cityName.isNotEmpty()) {
            this.weatherRepository.getWeatherForCity(cityName)
                .subscribeOn(schedulerProvider.subscribeScheduler)
                .observeOn(schedulerProvider.observeOnScheduler)
                .timeout(15, TimeUnit.SECONDS)
                .onErrorReturnItem(
                    this.errorWeatherResponseWithMessage("Something went wrong...")
                )
                .subscribe(observer)
        } else {
            observer.onSuccess(
                this.errorWeatherResponseWithMessage("City Name Should Not Be empty")
            )
        }
    }

    fun getForecastForCity(cityName: String, observer: SingleObserver<ForecastResponse>) {
        if(cityName.isNotEmpty()){
            this.weatherRepository.getForecastForCity(cityName)
                .subscribeOn(schedulerProvider.subscribeScheduler)
                .observeOn(schedulerProvider.observeOnScheduler)
                .timeout(15, TimeUnit.SECONDS)
                .onErrorReturnItem(
                    this.errorForecastResponseWithMessage("Something went wrong...")
                )
                .subscribe(observer)
        } else {
            observer.onSuccess(
                this.errorForecastResponseWithMessage("City Name Should Not Be empty")
            )
        }

    }

    fun prepareForecastDataLocalList(t: ForecastResponse, context: Context): List<ForecastDataLocal> {
        val result = mutableListOf<ForecastDataLocal>()
        var date = ""
        t.list?.let {
            for(fData:ForecastData in it){
                if(fData.dt_txt.contains("12:00")) {
                    val tempData = Tools.getDayAndMonthFromTimestamp(fData.dt)
                    if(date !== tempData){
                        date = tempData
                        result.add(ForecastDataLocal(
                            date,
                            context.resources.getString(R.string.degree_scale, (fData.main.temp - 273).toInt()),
                            getDrawableFromName(fData.weather[0].main, context))
                        )
                    }
                }
            }
        }
        return result
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun getDrawableFromName(name: String, context: Context): Drawable =
        when(name) {
            "Clouds" -> {
                if(_dayTimeResourceData.value == DayTime.NIGHT)
                    context.resources.getDrawable(R.drawable.cloudy_night, context.theme)
                else
                    context.resources.getDrawable(R.drawable.cloudy_day, context.theme)
            }
            "Clear" -> {
                if(_dayTimeResourceData.value == DayTime.NIGHT)
                    context.resources.getDrawable(R.drawable.night, context.theme)
                else
                    context.resources.getDrawable(R.drawable.sun, context.theme)
            }
            "Rain" -> {
                if(_dayTimeResourceData.value == DayTime.NIGHT)
                    context.resources.getDrawable(R.drawable.rainy_night, context.theme)
                else
                    context.resources.getDrawable(R.drawable.rainy_day, context.theme)
            }
            "Thunderstorm" -> {
                if(_dayTimeResourceData.value == DayTime.NIGHT)
                    context.resources.getDrawable(R.drawable.stormy_night, context.theme)
                else
                    context.resources.getDrawable(R.drawable.stormy_day, context.theme)
            }
            "Mist" -> {
                context.resources.getDrawable(R.drawable.foog, context.theme)
            }
            "Snow" -> {
                context.resources.getDrawable(R.drawable.snowy, context.theme)
            }
            else -> context.resources.getDrawable(R.drawable.cloud, context.theme)
        }


    fun isLocationExistsAsFavourities(name: String): Single<Boolean> {
        return this.locationRepository.isLocationNameExistsInDb(name)
    }

    private fun errorWeatherResponseWithMessage(message: String): WeatherResponse =
        WeatherResponse(
            null,
            null,
            -1,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            false,
            message
        )

    private fun errorForecastResponseWithMessage(message: String): ForecastResponse =
        ForecastResponse(
            null,
            0,
            -1,
            null,
            message
        )
}