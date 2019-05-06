package com.e.btex.connection.bleservice


interface BleServiceLifeCycle{
    fun onStartConnecting()
    fun onFailedConnecting()
    fun onCreateConnection()
    fun onDestroyConnection()
    fun onReceiveBytes(buffer: ByteArray, bytes: Int)
}