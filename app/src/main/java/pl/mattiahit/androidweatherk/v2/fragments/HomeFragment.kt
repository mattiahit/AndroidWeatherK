package pl.mattiahit.androidweatherk.v2.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import pl.mattiahit.androidweatherk.R
import pl.mattiahit.androidweatherk.WeatherApplication
import pl.mattiahit.androidweatherk.databinding.FragmentHomeV2Binding
import pl.mattiahit.androidweatherk.enums.DayTime
import pl.mattiahit.androidweatherk.models.ForecastDataLocal
import pl.mattiahit.androidweatherk.utils.Tools
import pl.mattiahit.androidweatherk.viewmodels.HomeViewModel
import pl.mattiahit.androidweatherk.viewmodels.factories.HomeViewModelFactory
import pl.mattiahit.androidweatherk.widgets.ForecastDataView
import javax.inject.Inject

class HomeFragment : Fragment(R.layout.fragment_home_v2) {

    @Inject
    lateinit var mHomeViewModelFactory: HomeViewModelFactory
    private lateinit var binding: FragmentHomeV2Binding
    private lateinit var mHomeViewModel: HomeViewModel
    private lateinit var dayTimeData: DayTime

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
            dayTimeData = it
        }

        mHomeViewModel.getCurrentLocation().observe(viewLifecycleOwner) {
            binding.pbMainWidget.visibility = View.GONE
            binding.tvCityName.text = it.locationName
            mHomeViewModel.getWeatherForCity(it.locationName)
            mHomeViewModel.getForecastForCity(it.locationName)
        }

        mHomeViewModel.weatherData.observe(viewLifecycleOwner) {
            binding.tvMainTemperature.text = requireContext().resources.getString(R.string.degree_scale, (it.main.temp - 273).toInt())
            binding.tvMainWind.text = requireContext().resources.getString(R.string.wind_speed_scale, it.wind.speed.toInt())
            binding.tvMainPressure.text = requireContext().resources.getString(R.string.pressure_scale, it.main.pressure)
            binding.tvMainClouds.text = requireContext().resources.getString(R.string.clouds_scale, it.clouds.all)
            binding.tvDataTime.text = Tools.getCurrentTime()
            binding.ivTodayWeatherIco.setImageDrawable(mHomeViewModel.getDrawableFromName(it.weather[0].main, requireContext()))
        }
        mHomeViewModel.forecastData.observe(viewLifecycleOwner) {
            binding.forecastDataLayout.removeAllViews()
            val dataList = mHomeViewModel.prepareForecastDataLocalList(it, requireContext())
            for(fdLocal: ForecastDataLocal in dataList) {
                binding.forecastDataLayout.addView(
                    ForecastDataView(
                        requireContext(),
                        fdLocal)
                )
            }
        }
    }
}