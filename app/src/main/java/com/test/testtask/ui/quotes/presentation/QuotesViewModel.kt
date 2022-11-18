package com.test.testtask.ui.quotes.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.testtask.ui.quotes.data.QuotesRepository
import com.test.testtask.ui.quotes.model.Quote
import com.test.testtask.ui.quotes.model.TopSecuritiesRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuotesViewModel @Inject constructor(
    private val repository: QuotesRepository
) : ViewModel() {
    private val viewModelState = MutableStateFlow(QuotesViewModelState(isLoading = true))

    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    init {
        subscribeOnRealQuotes()
    }

    private fun subscribeOnRealQuotes() {
        viewModelScope.launch {
            val tickers = repository.getTopSecurities(TopSecuritiesRequest())
            repository.getQuotes(buildRequestMessage("realtimeQuotes", tickers))
                .catch { Log.d("QuotesViewModel", "Quotes fetching error") }
                .collect { quotes -> viewModelState.update { it.copy(quotes = quotes, isLoading = false) } }
        }
    }

    private fun buildRequestMessage(subscriptionName: String, tickers: List<String>): String {
        val tickerList = tickers.ifEmpty { QuotesRepository.defaultTopSecurities() }
        val request = buildString {
            append("[")
            append("\"$subscriptionName\",")
            append("[")
            tickerList.forEachIndexed { index, str ->
                append("\"$str\"")
                if (index != tickerList.lastIndex) append(",")
            }
            append("]")
            append("]")
        }
        return request
    }
}

data class QuotesViewModelState(
    val quotes: List<Quote>? = null,
    val isLoading: Boolean = false
) {
    fun toUiState(): QuotesUiState {
        return if (quotes.isNullOrEmpty()) {
            QuotesUiState.NoQuotes()
        } else {
            QuotesUiState.HasQuotes(quotes)
        }
    }
}

sealed class QuotesUiState {
    data class HasQuotes(val quotes: List<Quote>) : QuotesUiState()
    data class NoQuotes(val error: String = "No data") : QuotesUiState()
}

