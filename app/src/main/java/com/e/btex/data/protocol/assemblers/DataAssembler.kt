package com.e.btex.data.protocol.assemblers

import com.e.btex.data.protocol.DataState
import com.e.btex.data.protocol.RemoteData

abstract class DataAssembler<T: RemoteData> {

    abstract val commandCode: Int
    abstract val size: Int

    abstract fun assemble(bytes: ByteArray): DataState<T>?

    protected var byteArray: ByteArray  = ByteArray(0)
}




