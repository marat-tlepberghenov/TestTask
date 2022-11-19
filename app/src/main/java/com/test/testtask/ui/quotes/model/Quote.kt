package com.test.testtask.ui.quotes.model

import androidx.compose.ui.graphics.Color

data class Quote(
    val _ticker: String? = null,
    val _priceChangePercent: Double? = null,
    val _lastTradeExchange: String? = null,
    val _shareName: String? = null,
    val _lastTradePrice: Double? = null,
    val _priceChangePoint: Double? = null
) {
    val ticker = _ticker ?: EMPTY
    val priceChangePercent
        get() = if (_priceChangePercent == null) {
            EMPTY
        } else {
            String.format(PERCENT_PATTERN, _priceChangePercent)
        }
    val lastTradeExchange = _lastTradeExchange ?: EMPTY
    val shareName = _shareName ?: EMPTY
    val lastTradePrice = _lastTradePrice?.toString() ?: EMPTY
    val priceChangePoint
        get() = if (_priceChangePoint == null) {
            EMPTY
        } else {
            String.format(POINT_PATTERN, _priceChangePoint)
        }
    val priceChangePercentFontColor
        get() = when {
            _priceChangePercent == null -> Color.Transparent
            _priceChangePercent > 0.0 -> Color(0xFF72bf46)
            _priceChangePercent < 0.0 -> Color(0xFFfc2d55)
            else -> Color.Gray
        }
    val exchangeAndShareName: String
        get() {
            if (lastTradeExchange.isEmpty()) return shareName
            if (shareName.isEmpty()) return lastTradeExchange
            return String.format(EXCHANGE_AND_SHARE_NAME_PATTERN, lastTradeExchange, shareName)
        }

    val lastPriceAndPoints: String
        get() {
            if (lastTradePrice.isEmpty()) return priceChangePercent
            if (priceChangePoint.isEmpty()) return lastTradePrice
            return String.format(LAST_PRICE_AND_POINTS_PATTERN, lastTradePrice, priceChangePoint)
        }

    companion object {
        private const val EMPTY = ""
        private const val PERCENT_PATTERN = "%+.2f%%"
        private const val POINT_PATTERN = "%+.2f"
        private const val EXCHANGE_AND_SHARE_NAME_PATTERN = "%s | %s"
        private const val LAST_PRICE_AND_POINTS_PATTERN = "%s ( %s )"
    }
}