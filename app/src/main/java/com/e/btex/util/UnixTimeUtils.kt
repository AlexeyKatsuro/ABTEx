package com.e.btex.util

import java.util.TimeZone

object UnixTimeUtils {
    val currentUnixTimeSeconds: Int //Seconds
        get() {
            val offset = TimeZone.getDefault().rawOffset + TimeZone.getDefault().dstSavings
            val now = System.currentTimeMillis() + offset
            return (now / 1000).toInt()
        }
    val currentUnixTimeMillis: Long
        get() {
            val offset = TimeZone.getDefault().rawOffset + TimeZone.getDefault().dstSavings
            return System.currentTimeMillis() + offset
        }
}
