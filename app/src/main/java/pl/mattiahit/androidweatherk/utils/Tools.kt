package pl.mattiahit.androidweatherk.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*


object Tools {
    fun getRandomLongId(): Long{
        return Random().nextLong();
    }

    @SuppressLint("SimpleDateFormat")
    fun getCurrentTime(): String {
        val df = SimpleDateFormat("dd.MM.yyyy HH:mm")
        return df.format(Calendar.getInstance().time)
    }

    @SuppressLint("SimpleDateFormat")
    fun getTimeFromTimestamp(time: Int): String {
        val df = SimpleDateFormat("HH:mm")
        return df.format(time)
    }
}