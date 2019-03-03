package com.e.btex.data

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BtDevice(val name: String, val macAddress: String) : Parcelable

fun BtDevice.getRemoteDevice(adapter: BluetoothAdapter): BluetoothDevice {
    return adapter.getRemoteDevice(macAddress)
}

fun BluetoothDevice.mapToBtDevice(): BtDevice = BtDevice(name ?: "Device",address)