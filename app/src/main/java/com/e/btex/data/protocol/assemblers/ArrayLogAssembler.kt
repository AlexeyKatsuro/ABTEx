package com.e.btex.data.protocol.assemblers

import com.e.btex.data.entity.ArrayLogData
import com.e.btex.data.entity.LogRowData
import com.e.btex.data.entity.SensorsData
import com.e.btex.data.protocol.DataState
import com.e.btex.util.extensions.percentageOf
import timber.log.Timber
import java.nio.ByteBuffer
import java.nio.ByteOrder

class ArrayLogAssembler : DataAssembler<ArrayLogData>() {

    private val preliminarySize = 8
    private val logRowSize = 8 + 14

    override val size
        get() = preliminarySize + logCount * logRowSize

    private val logCount
        get() = ((toId ?: 0) - (fromId ?: 0)).coerceAtLeast(0)

    var fromId: Int? = null
    var toId: Int? = null

    override val commandCode: Int
        get() = 1

    override fun assemble(bytes: ByteArray): DataState<ArrayLogData>? {
        byteList.addAll(bytes.toList())
        Timber.e("fromId: $fromId , toId $toId, logCount: $logCount, byteList: ${byteList.size} bytes , size: $size")
        return when {
            (byteList.size < preliminarySize) -> {
                DataState.IsLoading(0)
            }
            (byteList.size >= preliminarySize && toId == null) -> {

                val buffer = ByteBuffer.wrap(byteList.slice(0 until preliminarySize).toByteArray())
                    .order(ByteOrder.LITTLE_ENDIAN)
                fromId = buffer.int
                toId = buffer.int

                DataState.IsLoading(byteList.size.percentageOf(size))
            }
            (byteList.size >= size) -> {

                val buffer = ByteBuffer.wrap(byteList.toByteArray())
                    .order(ByteOrder.LITTLE_ENDIAN)
                DataState.Success(ArrayLogData(buffer.int, buffer.int, assembleList(buffer)))
            }
            else -> DataState.IsLoading(byteList.size.percentageOf(size))
        }
    }


    private fun assembleList(buffer: ByteBuffer): List<LogRowData>{
        return MutableList(logCount){
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