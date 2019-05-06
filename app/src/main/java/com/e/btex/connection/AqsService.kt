package com.e.btex.connection

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import com.e.btex.connection.bleservice.BleResultReceiver
import com.e.btex.connection.bleservice.BleService
import com.e.btex.connection.bleservice.BleServiceLifeCycle
import com.e.btex.data.BtDevice
import com.e.btex.data.ServiceStates
import com.e.btex.data.dao.SensorsDao
import com.e.btex.data.entity.ArrayLogData
import com.e.btex.data.mappers.RangeSensorMapper
import com.e.btex.data.protocol.DataState
import com.e.btex.data.protocol.ProtocolDataParser
import com.e.btex.data.protocol.RemoteData
import com.e.btex.data.protocol.commands.OutCommand
import com.e.btex.data.protocol.commands.ReadCommand
import com.e.btex.data.protocol.commands.SyncCommand
import dagger.android.AndroidInjection
import timber.log.Timber
import javax.inject.Inject

class AqsService : BleService(), AqsInterface {

    @Inject
    lateinit var sensorsDao: SensorsDao

    @Inject
    lateinit var arrayLogDataMapper: RangeSensorMapper

    companion object {
        private const val ARG_RESULT_RECEIVER = "arg_result_receiver"

        var instance: AqsService? = null

        fun startService(context: Context, device: BtDevice, callback: AqsStateCallback) {
            if (!isRunning) {
                val intent = Intent(context, AqsService::class.java)
                putExtras(intent, device)
                intent.putExtra(ARG_RESULT_RECEIVER, BleResultReceiver(Handler(context.mainLooper), callback))
                ContextCompat.startForegroundService(context, intent)
            }
        }
    }


    private lateinit var receiver: ResultReceiver

    private val parser = ProtocolDataParser()

    override fun sync() {
        sendCommand(SyncCommand())
    }

    override fun readLogs(fromId: Int, toId: Int) {
        sendCommand(ReadCommand(fromId, toId))
    }

    private fun sendCommand(command: OutCommand) {
        write(command.bytes)
    }

    override val lifeCycle: BleServiceLifeCycle = object : BleServiceLifeCycle {

        override fun onStartConnecting() {
            receiver = handleIntent.getParcelableExtra(ARG_RESULT_RECEIVER)
            sendMessage(ServiceStates.StartConnecting)
        }

        override fun onFailedConnecting() {
            sendMessage(ServiceStates.FailedConnecting)
        }

        override fun onCreateConnection() {
            sendMessage(ServiceStates.CreateConnection)
            sync()
        }

        override fun onDestroyConnection() {
            sendMessage(ServiceStates.DestroyConnection)
        }

        override fun onReceiveBytes(buffer: ByteArray, bytes: Int) {
            val dataState = parser.parse(buffer, bytes)

            if (dataState != null) {
                when (dataState) {
                    is DataState.Success -> {
                       dataClassDisposing(requireNotNull(dataState.data))
                    }

                    is DataState.IsLoading -> {
                        Timber.e("Loading ${dataState.progress}%")
                    }
                }
            }
        }

    }

    private fun dataClassDisposing(data: Any) {
        when (data) {
            is ArrayLogData -> {
                sensorsDao.insertAll(*arrayLogDataMapper.map(data).toTypedArray())
            }
            else -> {
                val bundle = bundleOf(BleResultReceiver.PARAM_DATA to data)
                Timber.e("$data")
                sendMessage(ServiceStates.OnReceiveData, bundle)
            }
        }
    }

    private fun sendMessage(state: ServiceStates, data: Bundle = bundleOf()) {
        receiver.send(state.ordinal, data)
    }

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
        instance = this
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }
}