package com.e.btex.data.protocol

import com.e.btex.data.entity.ArrayLogData
import com.e.btex.data.entity.LogRowData
import com.e.btex.data.entity.SensorsData
import com.e.btex.data.entity.StatusData
import com.e.btex.util.extensions.percentageOf
import com.e.btex.util.extensions.toPositiveInt
import timber.log.Timber
import java.nio.ByteBuffer
import java.nio.ByteOrder

abstract class DataAssembler<T> {

    abstract val commandCode: Int

    abstract fun assemble(bytes: ByteArray): DataState<T>?

    open fun prepare() {
        byteList.clear()
    }

    protected val byteList = mutableListOf<Byte>()

    object AssemblerFactory {
        private val assemblers: List<DataAssembler<out RemoteData>> = listOf(
            StatusAssembler(),
            ArrayLogAssembler()
        )

        fun getAssembler(code: Int): DataAssembler<*>? {
            return assemblers.find { it.commandCode == code }?.apply {
                prepare()
            }
        }
    }
}

class StatusAssembler : DataAssembler<StatusData>() {

    override fun assemble(bytes: ByteArray): DataState<StatusData>? {
        byteList.addAll(bytes.toList())
        return if (byteList.size >= size) {
            val buffer = ByteBuffer.wrap(byteList.toByteArray()).order(ByteOrder.LITTLE_ENDIAN)
            DataState.Success(
                StatusData(
                    buffer.get().toPositiveInt(),
                    buffer.get().toPositiveInt(),
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
            )
        } else DataState.IsLoading(byteList.size.percentageOf(size))
    }


    override val commandCode: Int
        get() = 0

    companion object {
        const val size = 6 + 14
    }
}

class ArrayLogAssembler : DataAssembler<ArrayLogData>() {

    var fromId: Int = 0
    var toId: Int? = null

    override val commandCode: Int
        get() = 1

    override fun prepare() {
        super.prepare()
        fromId = 0
        toId = null
    }

    override fun assemble(bytes: ByteArray): DataState<ArrayLogData>? {
        byteList.addAll(bytes.toList())
        Timber.e("fromId: $fromId , toId $toId,  byteList: ${byteList.size} bytes , size: $size")
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

    private val preliminarySize = 8
    private val logRowSize = 8 + 14
    val size
        get() = preliminarySize + logCount * logRowSize
    val logCount
         get() = (toId ?: 0 - fromId)

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
                ))
        }
    }
}

