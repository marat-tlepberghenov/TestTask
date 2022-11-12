package com.test.testtask.ui.quotes.data

import com.test.testtask.ui.quotes.model.TopSecuritiesRequest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class QuotesRepository @Inject constructor(
    private val remoteDataSource: QuotesRemoteDataSource,
    private val dispatcherIO: CoroutineDispatcher
) {
    fun getQuotes() = remoteDataSource.getQuotes()

    suspend fun getTopSecurities(request: TopSecuritiesRequest): List<String> {
        val response = withContext(dispatcherIO) {
            val result = runCatching { remoteDataSource.getTopSecurities(request) }
            result.getOrNull()
        }
        val tickers = response?.body()?.tickers
        return tickers ?: emptyList()
    }

    companion object {
        private const val DELIMITER = ","
        fun getShares(): List<String> {
            val rawShares =
                "RSTI,GAZP,MRKZ,RUAL,HYDR,MRKS,SBER,FEES,TGKA,VTBR,ANH.US,VICL.US,BURG. US,NBL.US,YETI.US,WSFS.US,NIO.US,DXC.US,MIC.US,HSBC.US,EXPN.EU,GSK.EU,SH P.EU,MAN.EU,DB1.EU,MUV2.EU,TATE.EU,KGF.EU,MGGT.EU,SGGD.EU"
            return rawShares.split(DELIMITER)
        }
    }
}