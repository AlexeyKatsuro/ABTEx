package com.e.btex.data.protocol.commands

import androidx.annotation.CallSuper
import com.e.btex.data.entity.LogRowData
import com.e.btex.data.entity.StatusData
import com.e.btex.data.protocol.RemoteData
import java.nio.ByteBuffer


sealed class Response<T: RemoteData> {

    companion object {
        val SIGN_1ST = 0xDE.toByte()
        val SIGN_2ND = 0xAF.toByte()
    }

    abstract val commandCode: Int

    abstract val size: Int

    abstract var data: T
    @CallSuper
    open fun initData(buffer: ByteBuffer){
        //Remove signature and commandCode bytes
        buffer.short
        buffer.get()
    }
}

class StatusResponse() : Response<StatusData>() {
    override val commandCode: Int
        get() = 0

    override lateinit var data: StatusData

    override val size: Int
        get() = 3 + 2 + 14

    override fun initData(buffer: ByteBuffer) {

    }

}

class RangeResponse() : Response<LogRowData>() {

    override val commandCode: Int
        get() = 1
    override val size: Int
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override lateinit var data: LogRowData


}

