package com.e.btex.util

import android.content.BroadcastReceiver
import android.content.IntentFilter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.e.btex.util.broadcastReceivers.WithActions
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * A lazy property that gets cleaned up when the fragment is destroyed.
 *
 * Accessing this variable in a destroyed fragment will throw NPE.
 */
class AutoRegisterReceiver<T>(val fragment: Fragment) : ReadWriteProperty<Fragment, T>
    where T : BroadcastReceiver, T : WithActions {

    private lateinit var receiver: T

    init {
        fragment.lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                fragment.requireActivity().unregisterReceiver(receiver)
            }
        })
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        return receiver
    }

    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: T) {
        receiver = value
        val intentFilter = IntentFilter()
        receiver.actions.forEach {
            intentFilter.addAction(it)
        }
        fragment.requireActivity().registerReceiver(receiver, intentFilter)
    }
}

/**
 * Creates an [AutoSubscribeReceiver] associated with this fragment.
 */
fun <T> Fragment.AutoSubscribeReceiver()
    where T : BroadcastReceiver,
          T : WithActions = AutoRegisterReceiver<T>(this)
