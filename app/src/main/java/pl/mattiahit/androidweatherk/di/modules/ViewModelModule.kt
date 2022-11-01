package pl.mattiahit.androidweatherk.di.modules

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import pl.mattiahit.androidweatherk.home.ui.viewmodel.factories.HomeViewModelFactory

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindsHomeViewModelFactory(factory: HomeViewModelFactory): ViewModelProvider.Factory
}