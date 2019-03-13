package com.e.btex.data.entity

import com.e.btex.data.protocol.RemoteData
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ArrayLogData(
    val formId: Int,
    val toId: Int,
    val logList: List<LogRowData>
) : RemoteData