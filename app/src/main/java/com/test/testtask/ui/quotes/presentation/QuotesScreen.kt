package com.test.testtask.ui.quotes.presentation

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.test.testtask.ui.quotes.model.Quote
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun QuotesScreen(
    quotesSharedFlow: SharedFlow<Quote>,
    updateQuoteIfNeeded: (Quote?, Quote) -> Quote
) {
    val quotes = remember { mutableStateMapOf<String, Quote>() }
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(key1 = Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            quotesSharedFlow.collect {
                val updatedQuote = updateQuoteIfNeeded(quotes[it.ticker], it)
                quotes[updatedQuote.ticker] = updatedQuote
            }
        }
    }

    LazyColumn {
        items(quotes.map { it.value }) {
            QuoteItem(quote = it)
            Divider(color = Color.Gray)
        }
    }
}