package com.e.btex.data.entity

import com.e.btex.data.protocol.RemoteData
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StatusData(
    val battery: Int,
    val status: Int,
    val lastLogId: Int,
    val sensors: SensorsData
) : RemoteData