package com.e.btex.connection

import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import com.e.btex.data.ServiceState.*

class BleResultReceiver(handler: Handler, private val callback: ServiceStateCallback) : ResultReceiver(handler) {

    companion object {
        const val PARAM_DATA = "param_data"
        const val PARAM_DATA_SIZE = "param_data_size"
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
                val data = resultData.getByteArray(PARAM_DATA)!!
                val size = resultData.getInt(PARAM_DATA_SIZE)
                callback.onReceiveData(data,size)
            }
        }
    }
}
