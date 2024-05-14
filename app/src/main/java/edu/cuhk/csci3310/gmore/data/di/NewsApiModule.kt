package edu.cuhk.csci3310.gmore.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.cuhk.csci3310.gmore.data.api.ApiConstants
import edu.cuhk.csci3310.gmore.data.api.NewsApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NewsApiModule {

    @Provides
    @Singleton
    fun provideApi(builder:Retrofit.Builder): NewsApi{
        return builder
            .build()
            .create(NewsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit.Builder{
        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30,TimeUnit.SECONDS).build();
        return Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL).client(client)
            .addConverterFactory(GsonConverterFactory.create())
    }

}