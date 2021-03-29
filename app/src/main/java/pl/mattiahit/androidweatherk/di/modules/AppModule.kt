package pl.mattiahit.androidweatherk.di.modules

import android.app.Application
import android.content.Context
import android.location.LocationManager
import androidx.room.Room
import dagger.Module
import dagger.Provides
import pl.mattiahit.androidweatherk.database.AppDatabase
import pl.mattiahit.androidweatherk.rest.BaseRestTask
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {

    @Singleton
    @Provides
    fun provideRetrofitInstance():Retrofit{
        return Retrofit.Builder()
            .baseUrl(BaseRestTask.SERVER_ADDRESS)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideRoomInstance():AppDatabase{
        return Room.databaseBuilder(this.application, AppDatabase::class.java, "WeatherDatabase").build()
    }

    @Singleton
    @Provides
    fun provideLocationManagerInstance():LocationManager{
        return this.application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }
}