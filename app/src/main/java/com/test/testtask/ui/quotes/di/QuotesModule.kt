package com.test.testtask.ui.quotes.di

import com.google.gson.Gson
import com.test.testtask.application.di.DispatcherIO
import com.test.testtask.network.AppWebSocketListener
import com.test.testtask.ui.quotes.data.QuotesApi
import com.test.testtask.ui.quotes.data.QuotesRemoteDataSource
import com.test.testtask.ui.quotes.data.QuotesRepository
import com.test.testtask.ui.quotes.model.Quote
import com.test.testtask.utils.MessageParser
import com.test.testtask.utils.QuoteParser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import retrofit2.Retrofit
import retrofit2.create

@Module
@InstallIn(ViewModelComponent::class)
object QuotesModuleProvide {

    @ViewModelScoped
    @Provides
    fun provideQuotesApi(retrofit: Retrofit): QuotesApi = retrofit.create()

    @ViewModelScoped
    @Provides
    fun provideQuotesRemoteDataSource(
        appWebSocketListener: AppWebSocketListener,
        api: QuotesApi
    ): QuotesRemoteDataSource = QuotesRemoteDataSource(appWebSocketListener, api)

    @ViewModelScoped
    @Provides
    fun provideQuotesRepository(
        quotesRemoteDataSource: QuotesRemoteDataSource,
        @DispatcherIO dispatcherIO: CoroutineDispatcher,
    ): QuotesRepository = QuotesRepository(quotesRemoteDataSource, dispatcherIO)

    @ViewModelScoped
    @Provides
    fun provideGson(): Gson = Gson()

    @ViewModelScoped
    @Provides
    fun provideQuotesParser(gson: Gson): MessageParser<Quote?> = QuoteParser(gson)
}