package pl.mattiahit.androidweatherk.di.modules

import dagger.Module
import dagger.Provides
import pl.mattiahit.androidweatherk.WeatherApplication
import pl.mattiahit.androidweatherk.home.domain.repository.LocationRepository
import pl.mattiahit.androidweatherk.home.domain.repository.WeatherRepository
import javax.inject.Singleton

@Module
class RepositoryModule(private val weatherApplication: WeatherApplication) {

    @Singleton
    @Provides
    fun provideLocationRepository(): LocationRepository {
        return LocationRepository(weatherApplication)
    }

    @Singleton
    @Provides
    fun provideWeatherRepository(): WeatherRepository {
        return WeatherRepository(weatherApplication)
    }
}