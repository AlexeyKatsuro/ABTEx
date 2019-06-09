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

    fun setTargetDevice(device: BtDevice) {
        preferenceStorage.targetDeviceName = device.name
        preferenceStorage.targetDeviceAddress = device.macAddress
    }

    fun getTargetBtDevice(): BtDevice? {
        val address = preferenceStorage.targetDeviceAddress
        return if(address != null){
            bluetoothDataSource.getBtDevice(address).let {
                if(it.name.isEmpty()){
                    it.copy(name = preferenceStorage.targetDeviceName?: "Device")
                } else it
            }
        } else null
    }

    fun getServiceState() = bluetoothDataSource.getServiceState()
}