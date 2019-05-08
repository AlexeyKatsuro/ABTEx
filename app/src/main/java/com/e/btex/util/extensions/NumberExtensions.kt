@file:JvmName("NumberUtils")

package com.e.btex.util.extensions

fun Int.percentageOf(value: Int): Int = if (value != 0) (this * 100) / value else 0

fun Int.applyDispersion(persent: Double): Int {
    return IntRange(this - (this * persent).toInt(), this + (this * persent).toInt()).random()
}


