package pl.mattiahit.androidweatherk.database

import androidx.room.Database
import androidx.room.RoomDatabase
import pl.mattiahit.androidweatherk.database.entities.WeatherLocation
import pl.mattiahit.androidweatherk.database.daos.WeatherLocationDao

@Database(entities = [WeatherLocation::class], exportSchema = false, version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun locationDao(): WeatherLocationDao
}