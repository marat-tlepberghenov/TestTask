package com.test.testtask.ui.quotes.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.testtask.network.WebSocketEvent
import com.test.testtask.ui.quotes.data.QuotesRepository
import com.test.testtask.ui.quotes.model.Quote
import com.test.testtask.ui.quotes.model.TopSecuritiesRequest
import com.test.testtask.utils.QuoteParser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.text.Typography.quote

@HiltViewModel
class QuotesViewModel @Inject constructor(
    private val repository: QuotesRepository,
    private val parser: QuoteParser
) : ViewModel() {
    private val _quotes = MutableSharedFlow<Quote>()
    val quotes = _quotes.asSharedFlow()

    init {
        subscribeOnRealQuotes()
    }

    private fun subscribeOnRealQuotes() {
        viewModelScope.launch {
            val list = repository.getTopSecurities(TopSecuritiesRequest())
            Log.d("QuotesViewModel", "Ticker list $list")
            repository.getQuotes()
                .catch { Log.d("QuotesViewModel", "Quotes fetching error") }
                .collect { event ->
                    when (event) {
                        is WebSocketEvent.Error -> {
                            Log.d("QuotesViewModel", "WebSocketEvent error")
                        }
                        is WebSocketEvent.Message -> {
                            parser.getObj(event.value)?.let { quote -> _quotes.emit(quote) }
                            Log.d("QuotesViewModel", "Quotes: $quote")
                        }
                        is WebSocketEvent.Open -> {}
                    }
                }
        }
    }
}

