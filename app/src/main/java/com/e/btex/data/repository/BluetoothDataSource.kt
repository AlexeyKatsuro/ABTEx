package com.e.btex.data.repository

import android.content.Context
import com.e.btex.connection.BleService
import com.e.btex.connection.ServiceStateCallback
import com.e.btex.data.BtDevice
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BluetoothDataSource @Inject constructor(
    private val context: Context
) {
    val callback = object : ServiceStateCallback {
        override fun onStartConnecting() {
            Timber.d("onStartConnecting")
        }

        override fun onFailedConnecting() {
            Timber.d("onFailedConnecting")
        }

        override fun onCreateConnection() {
            Timber.d("onCreateConnection")
        }

        override fun onDestroyConnection() {
            Timber.d("onDestroyConnection")
        }

        override fun onReceiveData(bytes: ByteArray, size: Int) {
            Timber.d("onReceiveData")
        }

    }



    fun initConnection(device: BtDevice) {
        if (BleService.instance == null) {
            BleService.startService(context, device, callback)
        }
    }

    fun sync(){

    }

}