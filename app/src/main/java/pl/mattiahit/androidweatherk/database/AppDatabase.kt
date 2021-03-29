package pl.mattiahit.androidweatherk.database

import androidx.room.Database
import androidx.room.RoomDatabase
import pl.mattiahit.androidweatherk.models.Location
import pl.mattiahit.androidweatherk.models.daos.LocationDao

@Database(entities = [Location::class], exportSchema = false, version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun locationDao(): LocationDao
}