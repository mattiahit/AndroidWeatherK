package pl.mattiahit.androidweatherk.models.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import pl.mattiahit.androidweatherk.models.WeatherLocation

@Dao
interface WeatherLocationDao {
    @Query("SELECT * FROM WeatherLocation")
    fun getAllLocations(): LiveData<List<WeatherLocation>>

    @Query("SELECT EXISTS(SELECT 1 FROM WeatherLocation WHERE location_name LIKE :name)")
    fun isLocationExists(name: String):Boolean

    @Insert
    fun insertLocation(location: WeatherLocation)

    @Delete
    fun deleteLocation(location: WeatherLocation)
}