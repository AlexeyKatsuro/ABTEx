package com.e.btex.util.extensions

import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

fun DrawerLayout.setAvailability(isAvailable: Boolean) {
    val mode = if (isAvailable) {
        DrawerLayout.LOCK_MODE_UNLOCKED
    } else {
        DrawerLayout.LOCK_MODE_LOCKED_CLOSED
    }
    setDrawerLockMode(mode)
}

val NavigationView.header: ViewGroup
    get() = getHeaderView(0) as ViewGroup