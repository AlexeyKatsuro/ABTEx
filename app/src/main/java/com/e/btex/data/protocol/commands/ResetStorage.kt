package com.e.btex.data.protocol.commands

import java.nio.ByteBuffer


class ResetStorage : OutCommand() {
    override val code: Byte
        get() = 2

    override fun fillBody(b: ByteBuffer) = Unit
}