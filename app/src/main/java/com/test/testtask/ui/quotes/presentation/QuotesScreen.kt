package com.test.testtask.ui.quotes.presentation

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun QuotesScreen(
    uiState: QuotesUiState
) {
    when (uiState) {
        is QuotesUiState.HasQuotes -> {
            LazyColumn {
                items(uiState.quotes) {
                    QuoteItem(quote = it)
                    Divider(color = Color.Gray)
                }
            }
        }
        is QuotesUiState.NoQuotes -> {
            Text(text = uiState.error)
        }
    }
}