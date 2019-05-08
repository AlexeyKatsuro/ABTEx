package com.e.btex.data.protocol

import com.e.btex.data.entity.LoadingData
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataState<T : RemoteData>(
    val data: T? = null,
    val loadingInfo: LoadingData
): RemoteData