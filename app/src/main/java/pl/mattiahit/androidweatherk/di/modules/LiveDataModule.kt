package pl.mattiahit.androidweatherk.di.modules

import dagger.Binds
import dagger.Module
import dagger.Provides
import pl.mattiahit.androidweatherk.WeatherApplication
import pl.mattiahit.androidweatherk.livedata.LocationLiveData
import javax.inject.Singleton

@Module
class LiveDataModule(private val weatherApplication: WeatherApplication) {

    @Singleton
    @Provides
    fun provideLocationLiveData(): LocationLiveData {
        return LocationLiveData(weatherApplication)
    }
}