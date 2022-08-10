package pl.mattiahit.androidweatherk

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity(){

    companion object {
        const val PERMISSION_REQUEST_LOCATION = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as WeatherApplication).getAppComponent().inject(this)
        setContentView(R.layout.activity_main)
    }

    fun hideKeyboard(view: View) {
        val imm: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}