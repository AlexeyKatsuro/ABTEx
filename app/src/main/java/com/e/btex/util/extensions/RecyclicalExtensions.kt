package com.e.btex.util.extensions

import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.afollestad.recyclical.*
import com.afollestad.recyclical.itemdefinition.RealItemDefinition
import com.e.btex.data.BtDevice
import com.e.btex.databinding.ItemBluetoothDeciveBinding
import com.e.btex.ui.adapters.DataBoundViewHolder

typealias ViewDataBindingCreator<T> = (View) -> T

inline fun <reified IT : Any> RecyclicalSetup.includeItem(
    @LayoutRes layoutRes: Int,
    noinline block: ItemDefinition<IT>.() -> Unit
): ItemDefinition<IT> {
    return RealItemDefinition(this, IT::class.java)
        .apply(block)
        .also { definition ->
            registerItemDefinition(
                className = definition.itemClassName,
                viewType = layoutRes,
                definition = definition
            )
        }
}

fun <B : ViewDataBinding,IT:Any > ItemDefinition<IT>.onDataBindingBind(
    bindingCreator: ViewDataBindingCreator<B>,
    block: DataBoundViewHolder<IT,B>.(Int, IT) -> Unit = {_, _ ->}
): ItemDefinition<IT>{
    onBind({ DataBoundViewHolder<IT, B>(bindingCreator(it)) }){ index, item ->
        this.bind(item,index)
        this.block(index,item)
    }
    return this
}