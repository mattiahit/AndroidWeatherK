package pl.mattiahit.androidweatherk.di.modules

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import pl.mattiahit.androidweatherk.viewmodels.factories.HomeViewModelFactory
import javax.inject.Singleton

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindsHomeViewModelFactory(factory: HomeViewModelFactory): ViewModelProvider.Factory
}