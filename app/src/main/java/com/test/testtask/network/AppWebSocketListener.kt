package com.test.testtask.network

import com.test.testtask.application.di.DispatcherIO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject

class AppWebSocketListener @Inject constructor(
    private val client: OkHttpClient,
    private val request: Request,
    @DispatcherIO dispatcherIO: CoroutineDispatcher,
    private val subscribeRequest: String
) {

    val message = callbackFlow {
        val socket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                trySendBlocking(WebSocketEvent.Message(text))
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                trySendBlocking(WebSocketEvent.Error(t))
            }

            override fun onOpen(webSocket: WebSocket, response: Response) {
                trySendBlocking(WebSocketEvent.Open(true))
                webSocket.send(subscribeRequest)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                trySend(WebSocketEvent.Open(false))
            }
        })

        awaitClose { socket.close(1000, null) }

    }.flowOn(dispatcherIO)
}

sealed class WebSocketEvent {
    data class Message(val value: String) : WebSocketEvent()
    data class Open(val isOpen: Boolean) : WebSocketEvent()
    data class Error(val value: Throwable) : WebSocketEvent()
}