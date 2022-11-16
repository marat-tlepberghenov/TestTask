package com.test.testtask.application.di

import com.test.testtask.network.AppWebSocketListener
import com.test.testtask.ui.quotes.data.QuotesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
    @DispatcherIO
    @Singleton
    @Provides
    fun provideCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Singleton
    @Provides
    fun provideRequest(): Request = Request.Builder()
        .url("wss://wss.tradernet.ru")
        .build()

    @Singleton
    @Provides
    fun providesOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .baseUrl("https://tradernet.ru")
        .build()

    @Singleton
    @Provides
    fun provideAppWebSocketListener(
        okHttpClient: OkHttpClient,
        request: Request,
        @DispatcherIO dispatcherIO: CoroutineDispatcher
    ): AppWebSocketListener = AppWebSocketListener(okHttpClient, request, dispatcherIO)

}