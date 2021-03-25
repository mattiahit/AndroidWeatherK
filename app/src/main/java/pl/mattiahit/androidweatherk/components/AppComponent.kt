package pl.mattiahit.androidweatherk.components

import dagger.Component
import pl.mattiahit.androidweatherk.MainActivity
import pl.mattiahit.androidweatherk.modules.ApiModule
import pl.mattiahit.androidweatherk.modules.AppModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ApiModule::class])
interface AppComponent {
    fun inject(mainActivity: MainActivity)
}