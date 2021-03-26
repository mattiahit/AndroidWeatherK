package pl.mattiahit.androidweatherk.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Location(@PrimaryKey val id: Long,
@ColumnInfo(name = "location_name") val locationName: String,
@ColumnInfo(name = "location_lat") val locationLat: Double,
@ColumnInfo(name = "location_lon") val locationLon: Double)