package com.e.btex.connection

import android.bluetooth.BluetoothAdapter
import android.content.Context

abstract class BleManager(val context: Context, val bleAdapter: BluetoothAdapter) {

    init {

    }
}