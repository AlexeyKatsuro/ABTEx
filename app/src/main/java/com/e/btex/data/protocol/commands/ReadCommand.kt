package com.e.btex.data.protocol.commands

import java.nio.ByteBuffer

class ReadCommand(private val fromIdx: Int, private val toIdx: Int) : OutCommand() {
    override val code: Byte
        get() = 1

    override fun fillBody(buffer: ByteBuffer) {
        buffer.putInt(fromIdx)
        buffer.putInt(toIdx)
    }
}
