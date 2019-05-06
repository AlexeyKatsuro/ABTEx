package com.e.btex.connection

import com.e.btex.data.protocol.RemoteData


interface AqsStateCallback {
    fun onStartConnecting()
    fun onFailedConnecting()
    fun onCreateConnection()
    fun onDestroyConnection()
    fun onReceiveData(data: RemoteData)
}
