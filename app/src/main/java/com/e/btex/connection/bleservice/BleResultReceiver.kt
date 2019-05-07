package com.e.btex.connection.bleservice

import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.os.ResultReceiver
import androidx.core.os.bundleOf
import com.e.btex.connection.AqsStateCallback
import com.e.btex.data.ServiceStates
import com.e.btex.data.protocol.RemoteData
import java.lang.ref.WeakReference

class BleResultReceiver(handler: Handler, callback: AqsStateCallback) : ResultReceiver(handler) {

    companion object {
        const val PARAM_DATA = "param_data"
    }

    private val link: WeakReference<AqsStateCallback> = WeakReference(callback)

    override fun onReceiveResult(resultCode: Int, resultData: Bundle) {
        val state = ServiceStates.values()[resultCode]

        when (state) {
            ServiceStates.CreateConnection -> {
                link.get()?.onCreateConnection()
            }
            ServiceStates.DestroyConnection -> {
                link.get()?.onDestroyConnection()
            }
            ServiceStates.StartConnecting -> {
                link.get()?.onStartConnecting()
            }
            ServiceStates.FailedConnecting -> {
                link.get()?.onFailedConnecting()
            }
            ServiceStates.OnReceiveData -> {
                val data = resultData.getParcelable<RemoteData>(PARAM_DATA)!!
                link.get()?.onReceiveData(data)
            }
        }
    }
}

fun ResultReceiver.sendState(state: ServiceStates, data: Any? = null) {
    val bundle = data?.let { bundleOf(BleResultReceiver.PARAM_DATA to it) } ?: bundleOf()
    send(state.ordinal, bundle)
}
