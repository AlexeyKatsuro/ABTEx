package com.e.btex.data.repository

import com.e.btex.data.preferences.PreferenceStorage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceRepository @Inject constructor(
    private val preferenceStorage: PreferenceStorage
) {
    fun getTargetAddress(): String? = preferenceStorage.targetDeviceAddress

    fun setTargetAddress(deviceAddress: String) {
        preferenceStorage.targetDeviceAddress = deviceAddress
    }
}