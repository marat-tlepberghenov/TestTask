package com.test.testtask.ui.quotes.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun QuotesScreen(
    uiState: QuotesUiState
) {
    when (uiState) {
        is QuotesUiState.HasQuotes -> {
            LazyColumn {
                items(uiState.quotes) {
                    QuoteItem(quote = it)
                    Divider(
                        color = Color(0xFFf2f2f2),
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
        }
        is QuotesUiState.NoQuotes -> {
            Text(text = uiState.error)
        }
    }
}