package pl.mattiahit.androidweatherk.di.modules

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.OkHttpClient
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
            .client(OkHttpClient().newBuilder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideRoomInstance():AppDatabase{
        return Room.databaseBuilder(this.application, AppDatabase::class.java, "WeatherDatabase").build()
    }

}