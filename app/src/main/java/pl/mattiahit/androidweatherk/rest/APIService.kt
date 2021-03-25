package pl.mattiahit.androidweatherk.rest

import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {

    @GET("weather")
    fun getWeatherForCity(@Query("q") cityName: String, @Query("appid") appid: String): Call<JSONObject>

    @GET("forecast")
    fun getForecastForCity(@Query("q") cityName: String, @Query("appid") appid: String): Call<JSONObject>

    @GET("weather")
    fun getWeatherForLocation(@Query("lat") lat: Double, @Query("lon") lon: Double, @Query("appid") appid: String): Call<JSONObject>

    @GET("forecast")
    fun getForecastForLocation(@Query("lat") lat: Double, @Query("lon") lon: Double, @Query("appid") appid: String): Call<JSONObject>
}