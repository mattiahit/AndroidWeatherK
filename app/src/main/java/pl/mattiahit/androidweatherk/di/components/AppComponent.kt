package pl.mattiahit.androidweatherk.di.components

import dagger.Component
import pl.mattiahit.androidweatherk.MainActivity
import pl.mattiahit.androidweatherk.di.modules.*
import pl.mattiahit.androidweatherk.fragments.HomeFragment
import pl.mattiahit.androidweatherk.repositories.LocationRepository
import pl.mattiahit.androidweatherk.repositories.WeatherRepository
import pl.mattiahit.androidweatherk.viewmodels.HomeViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ApiModule::class, ViewModelModule::class, RepositoryModule::class, LiveDataModule::class])
interface AppComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(homeFragment: HomeFragment)
    fun inject(homeFragmentV2: pl.mattiahit.androidweatherk.v2.fragments.HomeFragment)
    fun inject(locationRepository: LocationRepository)
    fun inject(weatherRepository: WeatherRepository)
    fun inject(homeViewModel: HomeViewModel)
}