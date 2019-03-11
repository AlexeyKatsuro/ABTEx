package com.e.btex.data.protocol.commands

import com.e.btex.util.UnixTimeUtils
import java.nio.ByteBuffer

class SyncCommand : OutCommand() {

    override val code: Byte
        get() = 0

    override fun fillBody(b: ByteBuffer) {
        b.putInt(UnixTimeUtils.currentUnixTime)
    }
}
