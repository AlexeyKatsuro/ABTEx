package com.e.btex.data.protocol.assemblers

import com.e.btex.data.protocol.DataState

abstract class DataAssembler<T> {

    abstract val commandCode: Int
    abstract val size: Int

    abstract fun assemble(bytes: ByteArray): DataState<T>?

    protected val byteList = mutableListOf<Byte>()
}




