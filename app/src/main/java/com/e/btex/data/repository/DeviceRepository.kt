package com.e.btex.data.repository

import com.e.btex.data.BtDevice
import com.e.btex.data.preferences.PreferenceStorage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceRepository @Inject constructor(
    private val preferenceStorage: PreferenceStorage,
    private val bluetoothDataSource: BluetoothDataSource
) {
    fun getTargetAddress(): String? = preferenceStorage.targetDeviceAddress

    fun setTargetAddress(deviceAddress: String) {
        preferenceStorage.targetDeviceAddress = deviceAddress
    }

    fun getTargetBtDevice(): BtDevice? {
        val address = getTargetAddress()
        return  address?.let {
            bluetoothDataSource.getBtDevice(it)
        }
    }

    fun getServiceState() = bluetoothDataSource.getServiceState()
}