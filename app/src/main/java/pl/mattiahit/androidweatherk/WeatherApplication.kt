package pl.mattiahit.androidweatherk

import android.app.Application
import pl.mattiahit.androidweatherk.di.components.AppComponent
import pl.mattiahit.androidweatherk.di.components.DaggerAppComponent
import pl.mattiahit.androidweatherk.di.modules.ApiModule
import pl.mattiahit.androidweatherk.di.modules.AppModule
import pl.mattiahit.androidweatherk.di.modules.RepositoryModule
import pl.mattiahit.androidweatherk.di.modules.ViewModelModule

class WeatherApplication : Application() {
    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .apiModule(ApiModule())
            .repositoryModule(RepositoryModule(this))
            .build()
    }

    fun getAppComponent(): AppComponent {
        return this.appComponent
    }
}