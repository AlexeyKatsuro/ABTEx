package com.e.btex.data.protocol.assemblers

import com.e.btex.data.entity.SensorsData
import com.e.btex.data.entity.StatusData
import com.e.btex.data.protocol.DataState
import com.e.btex.util.extensions.percentageOf
import com.e.btex.util.extensions.toPositiveInt
import java.nio.ByteBuffer
import java.nio.ByteOrder

class StatusAssembler : DataAssembler<StatusData>() {


    override val commandCode: Int = 0

    override val size = 6 + 14

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
        } else DataState.Loading(byteList.size.percentageOf(size))
    }


}