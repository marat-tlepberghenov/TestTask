package com.test.testtask.ui.quotes.data

import com.test.testtask.network.WebSocketEvent
import com.test.testtask.ui.quotes.model.Quote
import com.test.testtask.ui.quotes.model.TopSecuritiesRequest
import com.test.testtask.utils.QuoteParser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject

class QuotesRepository @Inject constructor(
    private val remoteDataSource: QuotesRemoteDataSource,
    private val parser: QuoteParser,
    private val dispatcherIO: CoroutineDispatcher
) {
    private val mutex = Mutex()
    private val quotes = mutableMapOf<String, Quote>()

    fun getQuotes(requestMessage: String) = remoteDataSource.getQuotes(requestMessage)
        .map { event ->
            when (event) {
                is WebSocketEvent.Error -> {}
                is WebSocketEvent.Message -> {
                    addQuote(parser.getObj(event.value))
                }
                is WebSocketEvent.Open -> {}
            }
            quotes.map { it.value }
        }

    private suspend fun addQuote(currentQuote: Quote?) {
        if (currentQuote == null) return
        val oldQuote = quotes[currentQuote.ticker]
        val updatedQuote = updateQuoteIfNeeded(oldQuote, currentQuote)
        mutex.withLock {
            quotes[updatedQuote.ticker] = updatedQuote
        }
    }

    private fun updateQuoteIfNeeded(old: Quote?, current: Quote): Quote {
        if (old == null) return current
        return old.copy(
            _ticker = current._ticker ?: old._ticker,
            _priceChangePercent = current._priceChangePercent ?: old._priceChangePercent,
            _lastTradeExchange = current._lastTradeExchange ?: old._lastTradeExchange,
            _shareName = current._shareName ?: old._shareName,
            _lastTradePrice = current._lastTradePrice ?: old._lastTradePrice,
            _priceChangePoint = current._priceChangePoint ?: old._priceChangePoint
        )
    }

    suspend fun getTopSecurities(request: TopSecuritiesRequest): List<String> {
        val response = withContext(dispatcherIO) {
            val result = runCatching { remoteDataSource.getTopSecurities(request) }
            result.getOrNull()
        }
        return response?.body()?.tickers ?: emptyList()
    }

    companion object {
        private const val DELIMITER = ","
        fun defaultTopSecurities(): List<String> {
            val rawShares =
                "RSTI,GAZP,MRKZ,RUAL,HYDR,MRKS,SBER,FEES,TGKA,VTBR,ANH.US,VICL.US,BURG.US,NBL.US,YETI.US,WSFS.US,NIO.US,DXC.US,MIC.US,HSBC.US,EXPN.EU,GSK.EU,SHP.EU,MAN.EU,DB1.EU,MUV2.EU,TATE.EU,KGF.EU,MGGT.EU,SGGD.EU"
            return rawShares.split(DELIMITER)
        }
    }
}