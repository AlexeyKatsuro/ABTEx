package com.e.btex.connection.bleservice

import android.app.IntentService
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import android.content.Intent
import com.e.btex.data.BtDevice
import com.e.btex.data.getRemoteDevice
import dagger.android.DaggerService
import timber.log.Timber
import java.io.InputStream
import java.io.OutputStream
import java.util.*

abstract class BleService : IntentService(BleService::class.java.simpleName) {

    companion object {
        private const val ARG_DEVICE = "arg_device"
        private const val appName = "BTEx"

        private val uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

        var isRunning: Boolean = false

        /**
         * Must be called to initialize the device to be connected
         */
        fun putExtras(intent: Intent, device: BtDevice): Intent {
            intent.putExtra(ARG_DEVICE, device)
            return intent
        }
    }

    private lateinit var device: BtDevice
    private lateinit var socket: BluetoothSocket

    private lateinit var inputStream: InputStream
    private lateinit var outputStream: OutputStream


    protected lateinit var handleIntent: Intent

    abstract val lifeCycle: BleServiceLifeCycle

    override fun onHandleIntent(intent: Intent) {
        Timber.d("onHandleIntent")
        handleIntent = intent

        device = intent.getParcelableExtra(ARG_DEVICE)
        lifeCycle.onStartConnecting()
        if (!connect(device)) {
            lifeCycle.onFailedConnecting()
            return
        }
        lifeCycle.onCreateConnection()
        val buffer = ByteArray(1024)
        var bytes: Int
        while (true) {
            try {
                bytes = inputStream.read(buffer)
                lifeCycle.onReceiveBytes(buffer, bytes)

            } catch (e: Exception) {
                e.printStackTrace()
                lifeCycle.onDestroyConnection()
                break
            }
        }
    }


    override fun onCreate() {
        super.onCreate()
        Timber.d("onCreate")
        isRunning = true

    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("onDestroy")
        isRunning = false
        closeConnection()
    }

    private fun createBluetoothSocket(device: BtDevice, adapter: BluetoothAdapter): BluetoothSocket {
        Timber.d("trying to connect to the device: $device")
        return device.getRemoteDevice(adapter).createRfcommSocketToServiceRecord(uuid)
    }


    private fun connect(device: BtDevice): Boolean {
        return try {
            socket = createBluetoothSocket(device, BluetoothAdapter.getDefaultAdapter())
            socket.connect()
            inputStream = socket.inputStream
            outputStream = socket.outputStream
            Timber.d("connection established, device: $device")
            true

        } catch (e: Exception) {
            e.printStackTrace()
            Timber.d("Connection error to device: $device")
            false
        }
    }

    private fun closeConnection() {
        try {
            inputStream.close()
            outputStream.close()
            socket.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            Timber.d("Connection closed")
        }
    }

    protected fun write(bytes: ByteArray) {
        outputStream.write(bytes)
    }
}