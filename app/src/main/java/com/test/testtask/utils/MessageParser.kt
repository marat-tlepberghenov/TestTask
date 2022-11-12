package com.test.testtask.utils

import com.google.gson.Gson
import com.test.testtask.ui.quotes.model.Quote
import javax.inject.Inject

interface MessageParser<T> {
    fun getObj(message: String): T
}

class QuoteParser @Inject constructor(
    private val gson: Gson
) : MessageParser<Quote?> {
    override fun getObj(message: String): Quote? {
        return try {
            val eventKeyValue = gson.fromJson(message, List::class.java)
            val key = eventKeyValue.firstOrNull()
            if (key?.equals("q") != true) return null
            val value = eventKeyValue?.get(1)
            return if (value is Map<*, *> && value.isNotEmpty()) {
                Quote(
                    _ticker = value["c"] as String?,
                    _priceChangePercent = value["pcp"] as Double?,
                    _lastTradeExchange = value["ltr"] as String?,
                    _shareName = value["name"] as String?,
                    _lastTradePrice = value["ltp"] as Double?,
                    _priceChangePoint = value["chg"] as Double?
                )
            } else null
        } catch (e: Exception) {
            null
        }
    }
}