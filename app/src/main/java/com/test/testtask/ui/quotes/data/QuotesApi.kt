package com.test.testtask.ui.quotes.data

import com.test.testtask.ui.quotes.model.TopSecuritiesRequest
import com.test.testtask.ui.quotes.model.TopSecuritiesResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface QuotesApi {
    @POST("/api")
    suspend fun getTopSecurities(@Body request: TopSecuritiesRequest): Response<TopSecuritiesResponse>
}

