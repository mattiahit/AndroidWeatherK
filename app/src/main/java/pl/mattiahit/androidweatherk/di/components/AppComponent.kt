package pl.mattiahit.androidweatherk.di.components

import dagger.Component
import pl.mattiahit.androidweatherk.MainActivity
import pl.mattiahit.androidweatherk.di.modules.ApiModule
import pl.mattiahit.androidweatherk.di.modules.AppModule
import pl.mattiahit.androidweatherk.di.modules.ViewModelModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ApiModule::class, ViewModelModule::class])
interface AppComponent {
    fun inject(mainActivity: MainActivity)
}