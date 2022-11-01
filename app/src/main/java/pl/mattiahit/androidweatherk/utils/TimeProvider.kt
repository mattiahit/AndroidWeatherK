package pl.mattiahit.androidweatherk.utils

import android.annotation.SuppressLint
import android.icu.util.Calendar
import pl.mattiahit.androidweatherk.enums.DayTime
import java.text.SimpleDateFormat
import java.util.*

class TimeProvider {

    private val _apiResponseTimeoutSeconds = 15L

    fun getApiResponseTimeoutSeconds(): Long = this._apiResponseTimeoutSeconds

    @SuppressLint("SimpleDateFormat")
    fun getDayPhase(): DayTime {
        val df = SimpleDateFormat("HH")
        val time = df.format(java.util.Calendar.getInstance().time)
        return when(time.toInt()) {
            in 0..5 -> DayTime.NIGHT
            in 5..8 -> DayTime.DAWN
            in 8..12 -> DayTime.MORNING
            in 12..17 -> DayTime.MIDDAY
            in 17..20 -> DayTime.DUSK
            in 20..23 -> DayTime.NIGHT
            else -> DayTime.MORNING
        }
    }
}