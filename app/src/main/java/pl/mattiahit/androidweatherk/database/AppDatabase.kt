package pl.mattiahit.androidweatherk.database

import androidx.room.Database
import androidx.room.RoomDatabase
import pl.mattiahit.androidweatherk.models.WeatherLocation
import pl.mattiahit.androidweatherk.models.daos.WeatherLocationDao

@Database(entities = [WeatherLocation::class], exportSchema = false, version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun locationDao(): WeatherLocationDao
}