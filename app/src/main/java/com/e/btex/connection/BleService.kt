package com.e.btex.connection

import android.app.IntentService
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import com.e.btex.data.BtDevice
import com.e.btex.data.ServiceStates
import com.e.btex.data.protocol.commands.OutCommand
import com.e.btex.data.protocol.commands.SyncCommand
import com.e.btex.data.getRemoteDevice
import com.e.btex.data.protocol.DataState
import com.e.btex.data.protocol.ProtocolDataParser
import com.e.btex.data.protocol.commands.ReadCommand
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import timber.log.Timber
import java.io.InputStream
import java.io.OutputStream
import java.util.*

class BleService : IntentService(BleService::class.java.simpleName) {

    companion object {
        private const val ARG_DEVICE = "arg_device"
        private const val ARG_RESULT_RECEIVER = "arg_result_receiver"
        private const val appName = "BTEx"

        private val uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

        private var isRunning: Boolean = false

        var instance: BleService? = null

        fun startService(context: Context, device: BtDevice, callback: ServiceStateCallback) {
            if (!isRunning) {
                val intent = Intent(context, BleService::class.java).apply {
                    putExtra(ARG_DEVICE, device)
                    putExtra(ARG_RESULT_RECEIVER, BleResultReceiver(Handler(context.mainLooper), callback))
                }
                ContextCompat.startForegroundService(context, intent)
            }
        }
    }

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    private lateinit var device: BtDevice
    private lateinit var socket: BluetoothSocket

    private lateinit var inputStream: InputStream
    private lateinit var outputStream: OutputStream

    private lateinit var receiver: ResultReceiver

    private val parser = ProtocolDataParser()


    override fun onHandleIntent(intent: Intent) {
        Timber.d("onHandleIntent")
        device = intent.getParcelableExtra(ARG_DEVICE)
        receiver = intent.getParcelableExtra(ARG_RESULT_RECEIVER)
        sendMessage(ServiceStates.OnStartConnecting)
        if (!connect(device)) {
            sendMessage(ServiceStates.OnFailedConnecting)
            return
        }
        sync()
        sendMessage(ServiceStates.OnCreateConnection)
        val buffer = ByteArray(1024)
        var bytes: Int
        while (true) {
            try {
                bytes = inputStream.read(buffer)

                val dataState = parser.parse(buffer, bytes)
                dataState?.let {
                    if(it is DataState.Success){
                        val data = bundleOf(BleResultReceiver.PARAM_DATA to it.data)
                        Timber.e("${it.data}")
                        sendMessage(ServiceStates.OnReceiveData, data)
                    }
                    if (it is DataState.IsLoading){
                        Timber.e("Loading ${it.progress}%")
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                sendMessage(ServiceStates.OnDestroyConnection)
                break
            }
        }
    }


    override fun onCreate() {
        super.onCreate()
        Timber.d("onCreate")
        isRunning = true
        instance = this

    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("onDestroy")
        isRunning = false
        instance = null
        closeConnection()
    }

    private fun createBluetoothSocket(device: BtDevice, adapter: BluetoothAdapter): BluetoothSocket {
        Timber.d("trying to connect to the device: ${device}")
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

    private fun sendMessage(state: ServiceStates, data: Bundle = bundleOf()) {
        receiver.send(state.ordinal, data)
    }

    fun write(bytes: ByteArray) {
        outputStream.write(bytes)
    }

    fun sendCommand(command: OutCommand) {
        write(command.bytes)
    }

    fun sync() {
        sendCommand(SyncCommand())
    }

    fun readLogs(fromId: Int, toId: Int){
        sendCommand(ReadCommand(fromId, toId))
    }
}