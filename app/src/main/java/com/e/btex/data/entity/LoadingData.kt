package com.e.btex.data.entity

import com.e.btex.data.protocol.RemoteData
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LoadingData(var progress: Int, var size: Int) : RemoteData {
    @IgnoredOnParcel
    val isLoading
    get() = progress < size
}