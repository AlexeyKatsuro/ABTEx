package com.e.btex.binding

import android.view.View
import android.widget.ProgressBar
import androidx.annotation.MenuRes
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter

/**
 * Data Binding adapters specific to the app.
 */

@BindingAdapter("visibleGone")
fun showHide(view: View, show: Boolean) {
    view.isVisible = show
}

@BindingAdapter("visibleInvisible")
fun showHideInvisible(view: View, show: Boolean) {
    view.isInvisible = !show
}

@BindingAdapter("inflateMenu")
fun setMenu(toolbar: Toolbar, @MenuRes resId: Int?) {
    resId?.let { toolbar.inflateMenu(it) }
}

//@BindingAdapter("progress")
//fun setProgress(progressBar: ProgressBar, progress: Int) {
//   progressBar.progress = progress
//}
