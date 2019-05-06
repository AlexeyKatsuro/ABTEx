package com.e.btex.connection

interface AqsInterface {

    fun sync()
    fun readLogs(fromId: Int, toId: Int)
}