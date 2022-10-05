package pl.mattiahit.androidweatherk.v2.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import pl.mattiahit.androidweatherk.R
import pl.mattiahit.androidweatherk.WeatherApplication
import pl.mattiahit.androidweatherk.databinding.FragmentHomeV2Binding
import pl.mattiahit.androidweatherk.enums.DayTime
import pl.mattiahit.androidweatherk.models.ForecastDataLocal
import pl.mattiahit.androidweatherk.rest.model.WeatherResponse
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

        mHomeViewModel.setObserveOnThread(AndroidSchedulers.mainThread())
        mHomeViewModel.dayTimeResourceData.observe(viewLifecycleOwner) {
            dayTimeData = it
        }

        mHomeViewModel.getCurrentLocation().observe(viewLifecycleOwner) {
            binding.pbMainWidget.visibility = View.GONE
            binding.tvCityName.text = it.locationName
            mHomeViewModel.getWeatherForCity(it.locationName, object : SingleObserver<WeatherResponse>{
                override fun onSubscribe(d: Disposable) {
                }

                override fun onSuccess(it: WeatherResponse) {
                    binding.tvMainTemperature.text = requireContext().resources.getString(R.string.degree_scale, if(it.main != null) (it.main.temp - 273).toInt() else -1)
                    binding.tvMainWind.text = requireContext().resources.getString(R.string.wind_speed_scale, if(it.wind != null) it.wind.speed.toInt() else -1)
                    binding.tvMainPressure.text = requireContext().resources.getString(R.string.pressure_scale, if(it.main != null) it.main.pressure else -1)
                    binding.tvMainClouds.text = requireContext().resources.getString(R.string.clouds_scale, if(it.clouds != null) it.clouds.all else -1)
                    binding.tvDataTime.text = Tools.getCurrentTime()
                    if(it.weather != null) {
                        binding.ivTodayWeatherIco.setImageDrawable(mHomeViewModel.getDrawableFromName( it.weather[0].main, requireContext()))
                    }
                }

                override fun onError(e: Throwable) {
                    e.message?.let { Log.e("ERROR", e.message!!) }
                }
            })
            mHomeViewModel.getForecastForCity(it.locationName)
        }

//        mHomeViewModel.weatherData.observe(viewLifecycleOwner) {
//            binding.tvMainTemperature.text = requireContext().resources.getString(R.string.degree_scale, if(it.main != null) (it.main.temp - 273).toInt() else -1)
//            binding.tvMainWind.text = requireContext().resources.getString(R.string.wind_speed_scale, if(it.wind != null) it.wind.speed.toInt() else -1)
//            binding.tvMainPressure.text = requireContext().resources.getString(R.string.pressure_scale, if(it.main != null) it.main.pressure else -1)
//            binding.tvMainClouds.text = requireContext().resources.getString(R.string.clouds_scale, if(it.clouds != null) it.clouds.all else -1)
//            binding.tvDataTime.text = Tools.getCurrentTime()
//            if(it.weather != null) {
//                binding.ivTodayWeatherIco.setImageDrawable(mHomeViewModel.getDrawableFromName( it.weather[0].main, requireContext()))
//            }
//        }
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