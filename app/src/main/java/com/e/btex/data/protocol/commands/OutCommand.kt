package com.e.btex.data.protocol.commands

import java.nio.ByteBuffer
import java.nio.ByteOrder

abstract class OutCommand {
    protected abstract val code: Byte

    val bytes: ByteArray
        get() {
            val buffer = ByteBuffer.allocate(100)
            buffer.order(ByteOrder.LITTLE_ENDIAN)
            buffer.putShort(0xAFDE.toShort())
            buffer.put(code)
            fillBody(buffer)
            return buffer.array().copyOf(buffer.position())
        }

    protected abstract fun fillBody(buffer: ByteBuffer)
}