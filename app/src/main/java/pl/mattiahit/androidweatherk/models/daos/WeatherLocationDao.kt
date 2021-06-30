package pl.mattiahit.androidweatherk.models.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import pl.mattiahit.androidweatherk.models.WeatherLocation

@Dao
interface WeatherLocationDao {
    @Query("SELECT * FROM WeatherLocation")
    fun getAllLocations(): Single<List<WeatherLocation>>

    @Query("SELECT EXISTS(SELECT 1 FROM WeatherLocation WHERE location_name LIKE :name)")
    fun isLocationExists(name: String): Single<Boolean>

    @Insert
    fun insertLocation(location: WeatherLocation): Completable

    @Delete
    fun deleteLocation(location: WeatherLocation): Single<Int>

    @Query("DELETE FROM WeatherLocation WHERE location_name LIKE :name")
    fun deleteLocationByName(name: String): Single<Int>
}