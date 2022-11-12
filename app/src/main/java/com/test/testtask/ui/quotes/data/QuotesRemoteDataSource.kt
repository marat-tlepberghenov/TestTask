package com.test.testtask.ui.quotes.data

import com.test.testtask.network.AppWebSocketListener
import com.test.testtask.ui.quotes.model.TopSecuritiesRequest
import javax.inject.Inject

class QuotesRemoteDataSource @Inject constructor(
    private val webSocketListener: AppWebSocketListener,
    private val api: QuotesApi
) {
    fun getQuotes() = webSocketListener.message

    suspend fun getTopSecurities(request: TopSecuritiesRequest) = api.getTopSecurities(request)
}