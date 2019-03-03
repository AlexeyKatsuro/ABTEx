package com.e.btex.data.commands

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.*

abstract class OutCommand {
    protected abstract val code: Byte

    val bytes: ByteArray
        get() {
            val bb = ByteBuffer.allocate(100)
            bb.order(ByteOrder.LITTLE_ENDIAN)
            bb.putShort(0xAFDE.toShort())
            bb.put(code)
            fillBody(bb)
            return Arrays.copyOf(bb.array(), bb.position())
        }

    protected abstract fun fillBody(b: ByteBuffer)
}