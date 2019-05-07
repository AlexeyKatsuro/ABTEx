@file:JvmName("NumberUtils")
package com.e.btex.util.extensions

fun Int.percentageOf(value: Int): Int = (this * 100) / value

fun Int.applyDispersion(persent: Double): Int {
    return IntRange(this - (this * persent).toInt(), this + (this * persent).toInt()).random()
}


