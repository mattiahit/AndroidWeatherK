package pl.mattiahit.androidweatherk.models.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import pl.mattiahit.androidweatherk.models.Location

@Dao
interface LocationDao {

    @Query("SELECT * FROM Location")
    fun getAllLocations(): LiveData<List<Location>>

    @Query("SELECT EXISTS(SELECT 1 FROM Location WHERE location_name LIKE :name)")
    fun isLocationExists(name: String):Boolean

    @Insert
    fun insertLocation(location: Location)

    @Delete
    fun deleteLocation(location: Location)
}