package com.e.btex.util.extensions

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

/**
 * Implementation of lazy that is not thread safe. Useful when you know what thread you will be
 * executing on and are not worried about synchronization.
 */
fun <T> lazyFast(operation: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE) {
    operation()
}

fun <T> Fragment.lazyArg(operation: (Bundle) -> T): Lazy<T> = lazyFast {
    val args = arguments ?: throw IllegalStateException("Missing arguments!")
    operation(args)
}

inline fun <T : ViewDataBinding> T.executeAfter(block: T.() -> Unit) {
    block()
    executePendingBindings()
}

inline val Fragment.act: AppCompatActivity
    get() = requireActivity() as AppCompatActivity

inline val Fragment.ctx: Context
    get() = requireContext()