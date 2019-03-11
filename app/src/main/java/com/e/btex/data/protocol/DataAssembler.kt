package com.e.btex.data.protocol

import com.e.btex.data.entity.SensorsData
import com.e.btex.data.entity.StatusData
import com.e.btex.util.extensions.toPositiveInt
import java.nio.ByteBuffer
import java.nio.ByteOrder

abstract class DataAssembler<T> {

    abstract val commandCode: Int

    abstract fun assemble(bytes: ByteArray): DataState<T>?

    abstract fun prepare()

    object AssemblerFactory {
        private val assemblers: List<DataAssembler<*>> = listOf(
            StatusAssembler()
        )

        fun getAssembler(code: Int): DataAssembler<*>? {
            return assemblers.find { it.commandCode == code }?.apply {
                prepare()
            }
        }
    }
}

class StatusAssembler : DataAssembler<StatusData>() {

    private val list = mutableListOf<Byte>()

    override fun assemble(bytes: ByteArray): DataState<StatusData>? {
        list.addAll(bytes.toList())
        return if (list.size >= size){
            val buffer = ByteBuffer.wrap(list.toByteArray()).order(ByteOrder.LITTLE_ENDIAN)
            DataState.Success(StatusData(
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
                )))
        } else null
    }


    override fun prepare() {
        list.clear()
    }

    override val commandCode: Int
        get() = 0

    companion object {
        const val size = 16
    }

}

