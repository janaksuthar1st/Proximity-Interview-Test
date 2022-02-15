package com.proximity.practicaljanak.repository.network

import androidx.lifecycle.MutableLiveData
import com.proximity.practicaljanak.BuildConfig
import com.proximity.practicaljanak.common.Resource
import com.proximity.practicaljanak.extensions.nullSafe
import com.proximity.practicaljanak.extensions.showDLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI
import kotlin.coroutines.CoroutineContext


class AirQualityNetworkRepository {

    private val job = Job()

    private val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    private lateinit var webSocketClient: WebSocketClient


    fun fetchAllAirQualityData(isLoaderRequired: Boolean? = false): MutableLiveData<Any> {
        val data = MutableLiveData<Any>()

        try {
            if (isLoaderRequired.nullSafe()) {
                data.postValue(Resource.Loading<Boolean>(true))
            }

            CoroutineScope(coroutineContext).launch {
                webSocketClient = object : WebSocketClient(URI(BuildConfig.BASE_URL)) {

                    override fun onOpen(handshakedata: ServerHandshake?) {
                        showDLog("==>WebSocket:", "onOpen")
                        webSocketClient.sendPing()
                    }

                    override fun onMessage(message: String?) {
                        showDLog("==>WebSocket:", "onMessage: $message")

                        if (isLoaderRequired.nullSafe()) {
                            data.postValue(Resource.Loading<Boolean>(false))
                        }

                        data.postValue(Resource.Success(message))
                    }

                    override fun onClose(code: Int, reason: String?, remote: Boolean) {
                        showDLog("==>WebSocket:", "onClose")
                    }

                    override fun onError(ex: Exception?) {
                        showDLog("==>WebSocket:", "onError: ${ex?.message}")
                        if (isLoaderRequired.nullSafe()) {
                            data.postValue(Resource.Loading<Boolean>(false))
                        }
                    }

                }

                webSocketClient.connect()
            }
        } catch (e: Exception) {
            data.postValue(Resource.Error<String>(e.localizedMessage.nullSafe()))
            e.printStackTrace()
        }

        return data
    }
}