package com.e.btex.data.protocol

import com.e.btex.data.protocol.assemblers.AssemblerFactory
import com.e.btex.data.protocol.assemblers.DataAssembler
import com.e.btex.util.extensions.toPositiveInt

class ProtocolDataParser {

    companion object {
        const val SIGN_1ST = 0xDE.toByte()
        const val SIGN_2ND = 0xAF.toByte()
    }

    private var assembler: DataAssembler<*>? = null

    fun parse(array: ByteArray, size: Int): DataState<*>? {
        val list: MutableList<Byte> = array.slice(0 until size).toMutableList()

        //Cycle for finding Signature
        for (index: Int in 0 until size - 2) {
            if (array[index] == SIGN_1ST && array[index + 1] == SIGN_2ND) {
                val code = array[index + 2].toPositiveInt()
                assembler = AssemblerFactory.getAssembler(code)
                //remove command Head
                list.removeAt(index)
                list.removeAt(index)
                list.removeAt(index)
                break
            }
        }

        return assembler?.assemble(list.toByteArray())
    }

}