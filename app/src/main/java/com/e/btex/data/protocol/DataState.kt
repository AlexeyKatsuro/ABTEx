package com.e.btex.data.protocol

sealed class DataState<out R> {

    class isLoading(val progress: Int) : DataState<Nothing>()
    class Success<out T>(val data: T) : DataState<T>()
}