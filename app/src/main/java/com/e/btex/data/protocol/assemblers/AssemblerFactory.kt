package com.e.btex.data.protocol.assemblers


/**
 * The class for getting the data assembler by the command code
 *
 * For supporting a new commands implement a new DataAssembler and add it in Factory under corresponding command code
 */
object AssemblerFactory {

    fun getAssembler(code: Int): DataAssembler<*>? {
        return when (code) {
            0 -> StatusAssembler()
            1 -> ArrayLogAssembler()
            else -> null
        }

    }
}
