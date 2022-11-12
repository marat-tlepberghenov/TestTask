package com.test.testtask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.test.testtask.ui.quotes.model.Quote
import com.test.testtask.ui.quotes.presentation.QuoteItem
import com.test.testtask.ui.quotes.presentation.QuotesScreen
import com.test.testtask.ui.quotes.presentation.QuotesViewModel
import com.test.testtask.ui.theme.QuotesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: QuotesViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuotesTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    ScreenSetup(viewModel = viewModel)
                }
            }
        }
    }

    @Composable
    fun ScreenSetup(viewModel: QuotesViewModel) {
        QuotesScreen(viewModel.quotes)
    }

    @Preview(showBackground = true)
    @Composable
    fun QuoteItemPreview() {
        QuoteItem(
            quote = Quote(
                "AAPL",
                -3.38,
                "NYSE",
                "Apple Inc",
                144.78,
                234.6
            )
        )
    }
}