package pl.mattiahit.androidweatherk.v2.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_home_v2.view.*
import kotlinx.android.synthetic.main.list_item_location_weather.view.*
import pl.mattiahit.androidweatherk.R
import pl.mattiahit.androidweatherk.WeatherApplication
import pl.mattiahit.androidweatherk.databinding.FragmentHomeV2Binding
import pl.mattiahit.androidweatherk.enums.DayTime
import pl.mattiahit.androidweatherk.viewmodels.HomeViewModel
import pl.mattiahit.androidweatherk.viewmodels.factories.HomeViewModelFactory
import javax.inject.Inject

class HomeFragment : Fragment(R.layout.fragment_home_v2) {

    @Inject
    lateinit var mHomeViewModelFactory: HomeViewModelFactory
    private lateinit var binding: FragmentHomeV2Binding
    private lateinit var mHomeViewModel: HomeViewModel

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeV2Binding.bind(view)

        (activity?.application as WeatherApplication).getAppComponent().inject(this)
        this.mHomeViewModel = ViewModelProvider(
            this,
            this.mHomeViewModelFactory
        )[HomeViewModel::class.java]

        mHomeViewModel.dayTimeResourceData.observe(viewLifecycleOwner) {
            val backgroundDrawable: Any
            backgroundDrawable = when (it) {
                DayTime.NIGHT -> resources.getDrawable(
                    R.drawable.ic_landscape_night,
                    requireContext().theme
                )
                DayTime.DAWN -> resources.getDrawable(
                    R.drawable.ic_landscape_dawn,
                    requireContext().theme
                )
                DayTime.MORNING -> resources.getDrawable(
                    R.drawable.ic_landscape_morning,
                    requireContext().theme
                )
                DayTime.MIDDAY -> resources.getDrawable(
                    R.drawable.ic_landscape_midday,
                    requireContext().theme
                )
                DayTime.DUSK -> resources.getDrawable(
                    R.drawable.ic_landscape_dusk,
                    requireContext().theme
                )
            }
            binding.ivBackground.setImageDrawable(backgroundDrawable)
        }
        mHomeViewModel.getCurrentLocation().observe(viewLifecycleOwner) {
            binding.pbMainWidget.visibility = View.GONE
            binding.tvCityName.text = it.locationName
            mHomeViewModel.getWeatherForCity(it.locationName)
        }
        mHomeViewModel.weatherData.observe(viewLifecycleOwner) {
            binding.tvMainTemperature.text = requireContext().resources.getString(R.string.degree_scale, (it.main.temp - 273).toInt())
            binding.tvMainWind.text = requireContext().resources.getString(R.string.wind_speed_scale, it.wind.speed.toInt())
            binding.tvMainPressure.text = requireContext().resources.getString(R.string.pressure_scale, it.main.pressure)
            if(it.weather[0].main == "Clouds") {
                binding.ivTodayWeatherIco.setImageDrawable(resources.getDrawable(R.drawable.cloud, requireContext().theme))
            } else {
                binding.ivTodayWeatherIco.setImageDrawable(resources.getDrawable(R.drawable.cloudy, requireContext().theme))
            }
        }
    }
}