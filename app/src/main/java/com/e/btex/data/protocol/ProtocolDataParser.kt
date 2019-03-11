package com.e.btex.data.protocol

import com.e.btex.data.protocol.commands.Response
import com.e.btex.util.extensions.toPositiveInt

class ProtocolDataParser {

    private var assembler: DataAssembler<*>? = null

    fun parse(array: ByteArray, size: Int): DataState<*>? {
        val list: MutableList<Byte> = array.slice(0 until  size).toMutableList()

        for (index: Int in 0 until size - 2) {
            if (array[index] == Response.SIGN_1ST && array[index + 1] == Response.SIGN_2ND) {
                val code = array[index + 2].toPositiveInt()
                assembler = DataAssembler.AssemblerFactory.getAssembler(code)

                //remove command Head
                list.removeAt(index)
                list.removeAt(index+1)
                list.removeAt(index+2)
            }
        }
        return assembler?.assemble(list.toByteArray())
    }

}