package pl.mattiahit.androidweatherk.modules

import dagger.Module
import dagger.Provides
import pl.mattiahit.androidweatherk.rest.BaseRestTask
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun providesRetrofitInstance():Retrofit{
        return Retrofit.Builder()
            .baseUrl(BaseRestTask.SERVER_ADDRESS)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}