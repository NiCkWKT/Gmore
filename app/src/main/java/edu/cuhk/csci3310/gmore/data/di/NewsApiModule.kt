package edu.cuhk.csci3310.gmore.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.cuhk.csci3310.gmore.data.api.ApiContstants
import edu.cuhk.csci3310.gmore.data.api.NewsApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
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
        return Retrofit.Builder()
            .baseUrl(ApiContstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    }

}