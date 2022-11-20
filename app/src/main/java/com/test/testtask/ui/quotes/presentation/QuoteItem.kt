package com.test.testtask.ui.quotes.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.test.testtask.ui.quotes.model.Quote

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun QuoteItem(quote: Quote) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(8.dp)
    ) {
        val (
            icon,
            ticker,
            exchangeAndShareName,
            chevron,
            changingInPercentsBox,
            diffInPoints
        ) = createRefs()
        GlideImage(
            model = "https://tradernet.ru/logos/get-logo-by-ticker?ticker=${quote.ticker.lowercase()}",
            contentDescription = "",
            modifier = Modifier
                .size(24.dp)
                .constrainAs(icon) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
        )
        Text(
            text = quote.ticker,
            modifier = Modifier
                .constrainAs(ticker) {
                    start.linkTo(icon.end, margin = 4.dp)
                    top.linkTo(icon.top)
                    bottom.linkTo(icon.bottom)
                },
            fontSize = 18.sp,
            textAlign = TextAlign.Start,
            color = Color(0xFF343c44)
        )
        Text(
            text = quote.exchangeAndShareName,
            modifier = Modifier
                .constrainAs(exchangeAndShareName) {
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                },
            fontSize = 12.sp,
            textAlign = TextAlign.Start,
            color = Color(0xFFa3a3a8)
        )
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "",
            modifier = Modifier
                .constrainAs(chevron) {
                    end.linkTo(parent.end, 4.dp)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
            tint = Color(0xFFa3a3a8)
        )
        Box(
            modifier = Modifier
                .constrainAs(changingInPercentsBox) {
                    top.linkTo(parent.top)
                    end.linkTo(chevron.start)
                }
                .wrapContentHeight()
                .wrapContentWidth()
                .background(
                    color = quote.lastTradePriceBackground,
                    shape = RoundedCornerShape(5.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = quote.priceChangePercent,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 4.dp),
                fontSize = 20.sp,
                textAlign = TextAlign.Start,
                color = quote.priceChangePercentFontColor
            )
        }
        Text(
            text = quote.lastPriceAndPoints,
            fontSize = 12.sp,
            textAlign = TextAlign.End,
            color = Color.Black,
            modifier = Modifier.constrainAs(diffInPoints) {
                end.linkTo(chevron.start)
                bottom.linkTo(parent.bottom)
            }
        )
    }
}