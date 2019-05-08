package com.e.btex.data.protocol.assemblers

import com.e.btex.data.entity.ArrayLogData
import com.e.btex.data.entity.LoadingData
import com.e.btex.data.entity.LogRowData
import com.e.btex.data.entity.SensorsData
import com.e.btex.data.protocol.DataState
import timber.log.Timber
import java.nio.ByteBuffer
import java.nio.ByteOrder

class ArrayLogAssembler : DataAssembler<ArrayLogData>() {

    private val preliminarySize = 8
    private val logRowSize = 8 + 14

    override val size
        get() = preliminarySize + logCount * logRowSize

    private val logCount
        get() = ((toId ?: 0) - (fromId ?: 0)).coerceAtLeast(0) + 1

    var fromId: Int? = null
    var toId: Int? = null

    override val commandCode: Int
        get() = 1

    private val dataState = DataState<ArrayLogData>(loadingInfo = LoadingData(0, size))
    private val loadingProgress: Int
        get() = (byteArray.size - preliminarySize) / logRowSize.coerceAtLeast(0)

    override fun assemble(bytes: ByteArray): DataState<ArrayLogData>? {
        byteArray += bytes
        dataState.loadingInfo.apply {
            progress = loadingProgress
            size = logCount
        }

        Timber.e("fromId: $fromId , toId $toId, logCount: $logCount, byteList: ${byteArray.size} bytes , size: $size")
        return when {
            (byteArray.size < preliminarySize) -> {
                null
            }
            (byteArray.size >= preliminarySize && toId == null && fromId == null) -> {

                val buffer = ByteBuffer.wrap(byteArray.sliceArray(0 until preliminarySize))
                    .order(ByteOrder.LITTLE_ENDIAN)
                fromId = buffer.int
                toId = buffer.int

                dataState
            }
            (byteArray.size >= size) -> {

                val buffer = ByteBuffer.wrap(byteArray)
                    .order(ByteOrder.LITTLE_ENDIAN)

                dataState.copy( data = ArrayLogData(buffer.int, buffer.int, assembleList(buffer)))
            }
            else -> dataState
        }
    }


    private fun assembleList(buffer: ByteBuffer): List<LogRowData> {
        return MutableList(logCount) {
            LogRowData(
                buffer.int,
                buffer.int,
                SensorsData(
                    buffer.short,
                    buffer.short,
                    buffer.short,
                    buffer.short,
                    buffer.short,
                    buffer.short,
                    buffer.short
                )
            )
        }
    }
}