package com.e.btex.ui

import android.bluetooth.BluetoothAdapter
import android.os.Bundle
import com.e.btex.R
import com.e.btex.util.extensions.longToast
import dagger.android.support.DaggerAppCompatActivity


class MainActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Close Application if Bluetooth module unavailable
        if (BluetoothAdapter.getDefaultAdapter() == null) {
            longToast(R.string.error_bluetooth_unavailable)
            finish()

        } else {
            setContentView(R.layout.activity_main)
        }
    }
}
