package com.e.btex.data.protocol


sealed class DataState<out R> {


    class Success<out T>(val data: T) : DataState<T>()
    class Loading(val progress: Int, val range: ClosedRange<Int> = 0..100) : DataState<Nothing>(){

    }
}