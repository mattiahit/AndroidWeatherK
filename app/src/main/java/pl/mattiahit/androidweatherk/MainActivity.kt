package pl.mattiahit.androidweatherk

import android.os.Bundle
import android.os.Message
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import pl.mattiahit.androidweatherk.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding

    companion object {
        const val PERMISSION_REQUEST_LOCATION = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as WeatherApplication).getAppComponent().inject(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        installSplashScreen()
        setContentView(binding.root)
    }

    private fun showMessage(message: String) {
        binding.tvAppMessage.text = message
    }

    fun showPositiveMessage(message: String) {
        binding.tvAppMessage.setBackgroundColor(getColor(R.color.colorPositiveDark))
        this.showMessage(message)
    }

    fun showNegativeMessage(message: String) {
        binding.tvAppMessage.setBackgroundColor(getColor(R.color.colorNegativeDark))
        this.showMessage(message)
    }

    fun hideKeyboard(view: View) {
        val imm: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}