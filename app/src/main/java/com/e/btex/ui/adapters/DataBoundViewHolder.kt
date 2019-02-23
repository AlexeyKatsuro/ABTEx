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
class DataBoundViewHolder<T : Any, out V : ViewDataBinding>(val binding: V) :
    RecyclerView.ViewHolder(binding.root) {

    lateinit var item: T
    var itemPosition: Int = -1

    private var itemClickListener: ((binding: V, position: Int, item: T) -> Unit)? = null

    init {
        binding.root.setOnClickListener {
            itemClickListener?.invoke(binding, itemPosition, item)
        }
    }

    fun bind(newItem: T, position: Int) {
        item = newItem
        itemPosition = position
        binding.executeAfter {
            setVariable(BR.item, item)
        }

    }

    fun setOnItemClickListener(listener: ((binding: V, position: Int, item: T) -> Unit)?) {
        itemClickListener = listener
    }
}
