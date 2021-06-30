package pl.mattiahit.androidweatherk.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class WeatherLocation(@PrimaryKey var id: Long,
                           @ColumnInfo(name = "location_name") var locationName: String,
                           @ColumnInfo(name = "location_lat") var locationLat: Double,
                           @ColumnInfo(name = "location_lon") var locationLon: Double,
                           @Ignore var isFavourite: Boolean) {
    constructor() : this(0, "", 0.0, 0.0, true)
}