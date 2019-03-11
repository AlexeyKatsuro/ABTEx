package com.e.btex.data.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
@Parcelize
data class SensorsData(
    val temperature: Short,
    val humidity: Short,
    val co2: Short,
    val pm1: Short,
    val pm10: Short,
    val pm25: Short,
    val tvoc: Short
) : Parcelable

