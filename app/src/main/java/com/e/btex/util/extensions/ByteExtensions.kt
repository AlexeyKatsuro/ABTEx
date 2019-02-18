package com.e.btex.util.extensions

import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.experimental.and


fun Byte.toPositiveInt() = toInt() and 0xFF
fun Byte.toPositiveShort() = toShort() and 0xFF

fun Short.toByteArray(order: ByteOrder = ByteOrder.LITTLE_ENDIAN)
        = ByteBuffer.allocate(2).order(order).putShort(this).array()

fun Int.toByteArray(order: ByteOrder = ByteOrder.LITTLE_ENDIAN)
        = ByteBuffer.allocate(4).order(order).putInt(this).array()

fun ByteArray.toInt(order: ByteOrder = ByteOrder.LITTLE_ENDIAN)
        = ByteBuffer.wrap(this).order(order).int

fun ByteArray.toShort(order: ByteOrder = ByteOrder.LITTLE_ENDIAN)
        = ByteBuffer.wrap(this).order(order).short

fun ByteArray.toIntList()
        = this.map ( Byte::toPositiveInt )