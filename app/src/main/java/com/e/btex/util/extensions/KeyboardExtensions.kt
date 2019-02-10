/*
 * Copyright 2018 LWO LLC
 */

package com.e.btex.util.extensions

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment

val Context.keyboard: InputMethodManager
    get() = getSystemService<InputMethodManager>()!!

val Activity.keyboard: InputMethodManager
    get() = getSystemService<InputMethodManager>()!!

fun View.showKeyboard() = context.keyboard.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)

fun View.hideKeyboard() = context.keyboard.hideSoftInputFromWindow(windowToken, 0)

fun Activity.showKeyboard() = keyboard.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)

fun Activity.hideKeyboard() = keyboard.hideSoftInputFromWindow(window.decorView.windowToken, 0)

fun Fragment.hideKeyboard() = act.hideKeyboard()

fun Fragment.showKeyboard() = act.showKeyboard()
