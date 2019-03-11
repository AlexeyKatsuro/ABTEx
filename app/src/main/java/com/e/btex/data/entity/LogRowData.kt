package com.e.btex.data.entity

import com.e.btex.data.protocol.RemoteData
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LogRowData(
    val sSecs: Int,
    var rTime: Int,
    val sensorsData: SensorsData
) : RemoteData {
    companion object
}
