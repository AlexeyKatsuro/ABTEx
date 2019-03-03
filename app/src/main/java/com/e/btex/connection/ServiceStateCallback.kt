package com.e.btex.connection


interface ServiceStateCallback {
    fun onStartConnecting()
    fun onFailedConnecting()
    fun onCreateConnection()
    fun onDestroyConnection()
    fun onReceiveData(bytes: ByteArray,size: Int)
}