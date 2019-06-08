/*
 * Copyright 2018 LWO LLC
 */

package com.e.btex.util.extensions

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment

/**
 * Find a [NavController] given a [Fragment]
 *
 * Calling this on a Fragment that is not a [NavHostFragment] or within a [NavHostFragment]
 * will result in an [IllegalStateException]
 */
fun Fragment.findNavController(): NavController =
    NavHostFragment.findNavController(this)

fun Fragment.navigateHideKeyboard(@IdRes resId: Int) {
    hideKeyboard()
    findNavController().navigate(resId)
}

fun Fragment.navigateHideKeyboard(directions: NavDirections) {
    hideKeyboard()
    findNavController().navigate(directions)
}

fun Fragment.navigateUpHideKeyboard() {
    hideKeyboard()
    findNavController().navigateUp()
}

fun NavController.navigateIfNewDirection(navDirections: NavDirections) {
    if (isNotCurrentDestination(navDirections.actionId)) {
        navigate(navDirections)
    }
}

fun NavController.navigateIfNewDirection(@IdRes resId: Int) {
    if (isNotCurrentDestination(resId)) {
        navigate(resId)
    }
}

fun NavController.isNotCurrentDestination(@IdRes resId: Int): Boolean =
    !isCurrentDestination(resId)

fun NavController.isCurrentDestination(@IdRes resId: Int): Boolean {
    val destId = currentDestination!!.getAction(resId)?.destinationId ?: resId
    return currentDestination!!.id == destId
}

