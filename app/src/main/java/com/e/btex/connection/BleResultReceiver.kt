package com.e.btex.connection

import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import com.e.btex.data.ServiceStates.*
import com.e.btex.data.protocol.RemoteData

class BleResultReceiver(handler: Handler, private val callback: ServiceStateCallback) : ResultReceiver(handler) {

    companion object {
        const val PARAM_DATA = "param_data"
    }


    override fun onReceiveResult(resultCode: Int, resultData: Bundle) {
        val state = values()[resultCode]

        when (state) {
            OnCreateConnection -> {
                callback.onCreateConnection()
            }
            OnDestroyConnection -> {
                callback.onDestroyConnection()
            }
            OnStartConnecting -> {
                callback.onStartConnecting()
            }
            OnFailedConnecting -> {
                callback.onFailedConnecting()
            }
            OnReceiveData -> {
                val data = resultData.getParcelable<RemoteData>(PARAM_DATA)!!
                callback.onReceiveData(data)
            }
        }
    }
}
