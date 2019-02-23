/*
 * Copyright 2018 LWO LLC
 */

package com.e.btex.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import kotlin.reflect.KProperty1

/**
 * A generic RecyclerView adapter that uses Data Binding & DiffUtil.
 *
 * @param <T> Type of the items in the list
 * @param <V> The type of the ViewDataBinding
 *
 * @param layoutRes Id of the layout for inflating the ViewDataBinding object
 * @param comparable Unique identification properties for DiffUtil comparison
 */
open class DataBoundAdapter<T : Any, V : ViewDataBinding>(
    @LayoutRes private val layoutRes: Int,
    comparable: KProperty1<T, Any>
) : ListAdapter<T, DataBoundViewHolder<T, V>>(object : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return comparable.get(oldItem) == comparable.get(newItem)
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }
}) {

    protected open var itemClickListener: ((binding: V, position: Int, item: T) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBoundViewHolder<T, V> {
        val binding = createBinding(parent)
        return DataBoundViewHolder<T, V>(binding).apply {
            setOnItemClickListener(itemClickListener)
        }
    }

    open fun createBinding(parent: ViewGroup): V {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            layoutRes,
            parent,
            false
        )
    }

    override fun onBindViewHolder(holder: DataBoundViewHolder<T, V>, position: Int) {
       holder.bind(getItem(position), position)
    }


    open fun setOnItemClickListener(listener: (binding: V, position: Int, item: T) -> Unit) {
        itemClickListener = listener
    }


}
