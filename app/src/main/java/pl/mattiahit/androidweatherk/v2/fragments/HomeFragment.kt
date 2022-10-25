package pl.mattiahit.androidweatherk.v2.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import pl.mattiahit.androidweatherk.MainActivity
import pl.mattiahit.androidweatherk.R
import pl.mattiahit.androidweatherk.WeatherApplication
import pl.mattiahit.androidweatherk.databinding.FragmentHomeV2Binding
import pl.mattiahit.androidweatherk.rest.model.ForecastResponse
import pl.mattiahit.androidweatherk.rest.model.WeatherResponse
import pl.mattiahit.androidweatherk.utils.PermissionHelper
import pl.mattiahit.androidweatherk.utils.PermissionListener
import pl.mattiahit.androidweatherk.utils.Tools
import pl.mattiahit.androidweatherk.viewmodels.HomeViewModel
import pl.mattiahit.androidweatherk.viewmodels.factories.HomeViewModelFactory
import pl.mattiahit.androidweatherk.widgets.ForecastAdapter
import javax.inject.Inject

class HomeFragment : Fragment(R.layout.fragment_home_v2), PermissionListener {

    @Inject
    lateinit var mHomeViewModelFactory: HomeViewModelFactory
    private lateinit var binding: FragmentHomeV2Binding
    private lateinit var mHomeViewModel: HomeViewModel
    private lateinit var mWeatherDisposable: Disposable
    private lateinit var mForecastDisposable: Disposable
    private val permissionHelper = PermissionHelper(this, this)

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeV2Binding.bind(view)

        (activity?.application as WeatherApplication).getAppComponent().inject(this)
        this.mHomeViewModel = ViewModelProvider(
            this,
            this.mHomeViewModelFactory
        )[HomeViewModel::class.java]

        permissionHelper.checkForRequiredPermissions()
    }

    private fun startCheckingWeather() {
        mHomeViewModel.getCurrentLocation().observe(viewLifecycleOwner) {
            binding.pbMainWidget.visibility = View.GONE
            binding.tvCityName.text = it.locationName

            mHomeViewModel.getWeatherForCity(it.locationName, object : SingleObserver<WeatherResponse>{
                override fun onSubscribe(d: Disposable) {
                    mWeatherDisposable = d
                }

                @SuppressLint("StringFormatInvalid")
                override fun onSuccess(it: WeatherResponse) {
                    if(it.cod != 200) {
                        it.message?.let { errorMessage ->
                            (activity as MainActivity).showNegativeMessage(errorMessage)
                        }
                    } else {
                        binding.tvMainTemperature.text = requireContext().resources.getString(R.string.degree_scale, if(it.main != null) (it.main.temp - 273).toInt() else -1)
                        binding.tvMainWind.text = requireContext().resources.getString(R.string.wind_speed_scale, if(it.wind != null) it.wind.speed.toInt() else -1)
                        binding.tvMainPressure.text = requireContext().resources.getString(R.string.pressure_scale, if(it.main != null) it.main.pressure else -1)
                        binding.tvMainClouds.text = requireContext().resources.getString(R.string.clouds_scale, if(it.clouds != null) it.clouds.all else -1)
                        binding.tvDataTime.text = Tools.getCurrentTime()
                        if(it.weather != null) {
                            binding.ivTodayWeatherIco.setImageDrawable(mHomeViewModel.getDrawableFromName( it.weather[0].main, requireContext()))
                        }
                    }
                }

                override fun onError(e: Throwable) {
                    e.message?.let { errorMessage ->
                        Log.e("ERROR", errorMessage)
                        (activity as MainActivity).showNegativeMessage(errorMessage)
                    }
                }
            })
            mHomeViewModel.getForecastForCity(it.locationName, object : SingleObserver<ForecastResponse>{
                override fun onSubscribe(d: Disposable) {
                    mForecastDisposable = d
                }

                override fun onSuccess(t: ForecastResponse) {
                    if(t.cod != 200) {
                        t.message?.let { errorMessage ->
                            (activity as MainActivity).showNegativeMessage(errorMessage)
                        }
                    } else {
                        val dataList = mHomeViewModel.getForecastDataLocalFromForecastResponse(t, requireContext())
                        binding.rvWeatherDaysList.adapter = ForecastAdapter(dataList)
                    }
                }

                override fun onError(e: Throwable) {
                    e.message?.let { errorMessage ->
                        Log.e("ERROR", errorMessage)
                        (activity as MainActivity).showNegativeMessage(errorMessage)
                    }
                }

            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this.mWeatherDisposable.dispose()
        this.mForecastDisposable.dispose()
    }

    override fun isPermissionGranted(isGranted: Boolean) {
        if(isGranted) {
            (activity as MainActivity).showPositiveMessage(getString(R.string.ok))
            startCheckingWeather()
        } else {
            (activity as MainActivity).showNegativeMessage(getString(R.string.permissions_required))
        }
    }

}