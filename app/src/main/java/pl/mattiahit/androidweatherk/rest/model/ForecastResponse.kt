package pl.mattiahit.androidweatherk.rest.model

data class ForecastResponse(
    val city: City?,
    val cnt: Int?,
    val cod: Int,
    val list: List<ForecastData>?,
    val message: String?
)