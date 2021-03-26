package pl.mattiahit.androidweatherk.di.modules

import dagger.Module
import dagger.Provides
import pl.mattiahit.androidweatherk.viewmodels.factories.HomeViewModelFactory
import javax.inject.Singleton

@Module
class ViewModelModule {

    @Singleton
    @Provides
    fun provideHomeViewModelFactory():HomeViewModelFactory{
        return HomeViewModelFactory()
    }
}