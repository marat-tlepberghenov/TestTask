package com.test.testtask.ui.quotes.model

data class TopSecuritiesRequest(
    val cmd: String = "getTopSecurities",
    val params: Map<String, Any> = mapOf(
        "type" to "stocks",
        "exchange" to "europe",
        "gainers" to 1,
        "limit" to 30,
    )
)