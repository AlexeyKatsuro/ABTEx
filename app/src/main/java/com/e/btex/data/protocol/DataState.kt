package com.e.btex.data.protocol


sealed class DataState<out R> {


    class Success<out T>(val data: T) : DataState<T>()
    class IsLoading(private val _progress: Int, private val range: ClosedRange<Int> = 0..100) : DataState<Nothing>(){
        val progress : Int
            get() = _progress.coerceIn(range)
    }
}