package pl.mattiahit.androidweatherk.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class WeatherLocation(@PrimaryKey val id: Long,
                           @ColumnInfo(name = "location_name") val locationName: String,
                           @ColumnInfo(name = "location_lat") val locationLat: Double,
                           @ColumnInfo(name = "location_lon") val locationLon: Double,
                           @Ignore val isFavourite: Boolean = true)