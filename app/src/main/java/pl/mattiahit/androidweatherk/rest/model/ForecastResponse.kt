package pl.mattiahit.androidweatherk.rest.model

data class ForecastResponse(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<ForecastData>,
    val message: Int
)