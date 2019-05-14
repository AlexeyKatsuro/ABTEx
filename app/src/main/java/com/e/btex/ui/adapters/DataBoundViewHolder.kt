/*
 * Copyright 2018 LWO LLC
 */

package com.e.btex.ui.adapters

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.e.btex.BR
import com.e.btex.util.extensions.executeAfter

/**
 * A generic ViewHolder that works with a [ViewDataBinding].
 * @param <T> The type of the ViewDataBinding.
 */
class DataBoundViewHolder<T : Any, out V : ViewDataBinding>(val itemBinding: V) :
    RecyclerView.ViewHolder(itemBinding.root) {

    lateinit var item: T
    var itemPosition: Int = -1

    //private var itemClickListener: ((binding: V, position: Int, item: T) -> Unit)? = null
//
//    init {
//        itemBinding.root.setOnClickListener {
//            itemClickListener?.invoke(itemBinding, itemPosition, item)
//        }
//    }

    fun bind(newItem: T, position: Int) {
        item = newItem
        itemPosition = position
        itemBinding.executeAfter {
            setVariable(BR.item, item)
        }

    }

//    fun setOnItemClickListener(listener: ((binding: V, position: Int, item: T) -> Unit)?) {
//        itemClickListener = listener
//    }
}
