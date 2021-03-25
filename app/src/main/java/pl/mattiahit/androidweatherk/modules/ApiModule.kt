package pl.mattiahit.androidweatherk.modules

import dagger.Module
import dagger.Provides
import pl.mattiahit.androidweatherk.rest.APIService
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class ApiModule {

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): APIService{
        return retrofit.create(APIService::class.java)
    }
}