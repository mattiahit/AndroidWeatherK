package pl.mattiahit.androidweatherk.rest

import io.reactivex.rxjava3.core.Single
import pl.mattiahit.androidweatherk.rest.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {

    @GET("weather")
    fun getWeatherForCity(@Query("q") cityName: String, @Query("appid") appid: String): Single<WeatherResponse>

    @GET("forecast")
    fun getForecastForCity(@Query("q") cityName: String, @Query("appid") appid: String): Single<WeatherResponse>

    @GET("weather")
    fun getWeatherForLocation(@Query("lat") lat: Double, @Query("lon") lon: Double, @Query("appid") appid: String): Single<WeatherResponse>

    @GET("forecast")
    fun getForecastForLocation(@Query("lat") lat: Double, @Query("lon") lon: Double, @Query("appid") appid: String): Single<WeatherResponse>
}