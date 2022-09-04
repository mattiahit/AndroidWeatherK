package pl.mattiahit.androidweatherk.models

import android.graphics.drawable.Drawable

data class ForecastDataLocal(
    val date: String,
    val tempString: String,
    val weatherDrawable: Drawable
)
