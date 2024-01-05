package com.jp.test.pagingwithdbdemo.modules

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jp.test.pagingwithdbdemo.network.ApiService
import com.jp.test.pagingwithdbdemo.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideRetrofitService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideRetrofit() : Retrofit =
        Retrofit.Builder().client(okHttpClient).addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL).build()

    private val httpLoggingInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build()
    }

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

}