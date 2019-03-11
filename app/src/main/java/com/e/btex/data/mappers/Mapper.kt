package com.e.btex.data.mappers

interface Mapper<F, T> {
    fun map(from: F): T
}
