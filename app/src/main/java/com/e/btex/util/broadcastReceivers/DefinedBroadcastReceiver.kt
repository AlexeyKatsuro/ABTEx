package com.e.btex.util.broadcastReceivers

import android.content.BroadcastReceiver

abstract class DefinedBroadcastReceiver: BroadcastReceiver(){

    abstract val actions: List<String>
}