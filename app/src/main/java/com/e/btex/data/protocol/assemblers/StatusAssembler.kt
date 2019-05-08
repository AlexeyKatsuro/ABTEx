package com.e.btex.data.protocol.assemblers

import com.e.btex.data.entity.LoadingData
import com.e.btex.data.entity.SensorsData
import com.e.btex.data.entity.StatusData
import com.e.btex.data.protocol.DataState
import com.e.btex.util.extensions.toPositiveInt
import java.nio.ByteBuffer
import java.nio.ByteOrder

class StatusAssembler : DataAssembler<StatusData>() {


    override val commandCode: Int = 0

    override val size = 6 + 14

    override fun assemble(bytes: ByteArray): DataState<StatusData>? {
        byteArray+=bytes
        return if (byteArray.size >= size) {
            val buffer = ByteBuffer.wrap(byteArray).order(ByteOrder.LITTLE_ENDIAN)
            DataState(
               data =  StatusData(
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
                ),
                loadingInfo = LoadingData(size,size)
            )

        } else null
    }


}
