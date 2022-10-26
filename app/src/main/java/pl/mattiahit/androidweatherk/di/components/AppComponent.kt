package pl.mattiahit.androidweatherk.di.components

import dagger.Component
import pl.mattiahit.androidweatherk.MainActivity
import pl.mattiahit.androidweatherk.di.modules.*
import pl.mattiahit.androidweatherk.home.ui.HomeFragment
import pl.mattiahit.androidweatherk.home.domain.repository.LocationRepository
import pl.mattiahit.androidweatherk.home.domain.repository.WeatherRepository
import pl.mattiahit.androidweatherk.home.ui.viewmodel.HomeViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ApiModule::class, ViewModelModule::class, RepositoryModule::class, LiveDataModule::class])
interface AppComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(homeFragmentV2: HomeFragment)
    fun inject(locationRepository: LocationRepository)
    fun inject(weatherRepository: WeatherRepository)
    fun inject(homeViewModel: HomeViewModel)
}