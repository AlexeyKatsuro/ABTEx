@file:Suppress("NOTHING_TO_INLINE", "unused")

package com.e.btex.util.extensions

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

/**
 * Display the simple Toast message with the [Toast.LENGTH_SHORT] duration.
 *
 * @param message the message text resource.
 */
inline fun Context.toast(@StringRes message: Int) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

/**
 * Display the simple Toast message with the [Toast.LENGTH_SHORT] duration.
 *
 * @param message the message text.
 */
inline fun Context.toast(message: CharSequence) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

/**
 * Display the simple Toast message with the [Toast.LENGTH_LONG] duration.
 *
 * @param message the message text resource.
 */
fun Context.longToast(@StringRes message: Int) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

/**
 * Display the simple Toast message with the [Toast.LENGTH_LONG] duration.
 *
 * @param message the message text.
 */
fun Context.longToast(message: CharSequence) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

/**
 * Display the simple Toast message with the [Toast.LENGTH_SHORT] duration.
 *
 * @param message the message text resource.
 */
inline fun Fragment.toast(@StringRes message: Int) = requireActivity().toast(message)

/**
 * Display the simple Toast message with the [Toast.LENGTH_SHORT] duration.
 *
 * @param message the message text.
 */
inline fun Fragment.toast(message: CharSequence) = requireActivity().toast(message)

/**
 * Display the simple Toast message with the [Toast.LENGTH_LONG] duration.
 *
 * @param message the message text resource.
 */
inline fun Fragment.longToast(@StringRes message: Int) = requireActivity().toast(message)

/**
 * Display the simple Toast message with the [Toast.LENGTH_LONG] duration.
 *
 * @param message the message text.
 */
inline fun Fragment.longToast(message: CharSequence) = requireActivity().toast(message)
