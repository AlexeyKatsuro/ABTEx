package com.e.btex.data.protocol

import com.e.btex.data.protocol.commands.Response
import com.e.btex.util.extensions.toPositiveInt
import timber.log.Timber

class ProtocolDataParser {

    private var assembler: DataAssembler<*>? = null

    fun parse(array: ByteArray, size: Int): DataState<*>? {
        val list: MutableList<Byte> = array.slice(0 until size).toMutableList()

        for (index: Int in 0 until size - 2) {
            if (array[index] == Response.SIGN_1ST && array[index + 1] == Response.SIGN_2ND) {
                val code = array[index + 2].toPositiveInt()
                assembler = DataAssembler.AssemblerFactory.getAssembler(code)
                Timber.e("-----------------")
                Timber.e("$list")
                //remove command Head
                list.removeAt(index)
                list.removeAt(index)
                list.removeAt(index )
                Timber.e("$list")
                break
            }
        }

        return assembler?.assemble(list.toByteArray())
    }

}