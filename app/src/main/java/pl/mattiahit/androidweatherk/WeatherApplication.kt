package pl.mattiahit.androidweatherk

import android.app.Application
import pl.mattiahit.androidweatherk.components.AppComponent
import pl.mattiahit.androidweatherk.components.DaggerAppComponent
import pl.mattiahit.androidweatherk.modules.ApiModule
import pl.mattiahit.androidweatherk.modules.AppModule

class WeatherApplication : Application() {
    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule())
            .apiModule(ApiModule())
            .build()
    }

    fun getAppComponent(): AppComponent{
        return this.appComponent
    }
}