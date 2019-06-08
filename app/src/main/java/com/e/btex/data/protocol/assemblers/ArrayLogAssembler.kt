package com.e.btex.data.protocol.assemblers

import com.e.btex.data.entity.ArrayLogData
import com.e.btex.data.entity.LoadingData
import com.e.btex.data.entity.LogRowData
import com.e.btex.data.entity.SensorsData
import com.e.btex.data.protocol.DataState
import com.e.btex.util.extensions.toByteArray
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

    private val dataState = DataState<ArrayLogData>(loadingInfo = LoadingData(0, logCount))

    private var loadedCount: Int = 0


    override fun assemble(bytes: ByteArray): DataState<ArrayLogData>? {
        byteArray += bytes
        Timber.e("size: ${byteArray.size}")

        if (byteArray.size >= preliminarySize && toId == null && fromId == null) {
            val buffer = ByteBuffer.wrap(byteArray.sliceArray(0 until preliminarySize))
                .order(ByteOrder.LITTLE_ENDIAN)
            fromId = buffer.int
            toId = buffer.int
            return null/*dataState.apply {
                loadingInfo.progress = loadedCount
                loadingInfo.size = logCount
            }*/
        }
        if (byteArray.size >= preliminarySize + logRowSize) {
            val count = (byteArray.size - preliminarySize) / logRowSize
            val buffer = ByteBuffer.wrap(byteArray).order(ByteOrder.LITTLE_ENDIAN)
            val index = preliminarySize + logRowSize * count
            val start = byteArray.sliceArray(0 until preliminarySize)
            val end = byteArray.sliceArray(index until byteArray.size)
            byteArray = start + end
            Timber.e("size: ${byteArray.size}")


            return dataState.copy(
                data = ArrayLogData(
                    buffer.int + loadedCount,
                    buffer.int,
                    assembleList(buffer, count)
                )
            ).apply {
                loadingInfo.progress = loadedCount
            }
        }
        return null
    }

    private fun assembleList(buffer: ByteBuffer, count: Int): List<LogRowData> {
        return MutableList(count) {
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
        }.apply {
            loadedCount += size
        }.filter { it.rTime != 0 } //TODO May be unstable
    }
}
