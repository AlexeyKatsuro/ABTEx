package com.e.btex.connection

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.ResultReceiver
import androidx.core.content.ContextCompat
import com.e.btex.connection.bleservice.BleResultReceiver
import com.e.btex.connection.bleservice.BleService
import com.e.btex.connection.bleservice.BleServiceLifeCycle
import com.e.btex.connection.bleservice.sendState
import com.e.btex.data.BtDevice
import com.e.btex.data.ServiceStates
import com.e.btex.data.dao.SensorsDao
import com.e.btex.data.entity.ArrayLogData
import com.e.btex.data.entity.StatusData
import com.e.btex.data.mappers.RangeSensorMapper
import com.e.btex.data.mappers.StatusDataMapper
import com.e.btex.data.protocol.ProtocolDataParser
import com.e.btex.data.protocol.RemoteData
import com.e.btex.data.protocol.commands.OutCommand
import com.e.btex.data.protocol.commands.ReadCommand
import com.e.btex.data.protocol.commands.SyncCommand
import com.e.btex.util.NotificationUtils
import dagger.android.AndroidInjection
import timber.log.Timber
import javax.inject.Inject


class AqsService : BleService(), AqsInterface {

    @Inject
    lateinit var sensorsDao: SensorsDao

    @Inject
    lateinit var rangeDataMapper: RangeSensorMapper

    @Inject
    lateinit var statusDataMapper: StatusDataMapper

    companion object {
        private const val ARG_RESULT_RECEIVER = "arg_result_receiver"
        private const val CHANNEL_ID ="1234 Channel ID"
        private const val NOTIFICATION_ID = 1234

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
            receiver.sendState(ServiceStates.StartConnecting)
        }

        override fun onFailedConnecting() {
            receiver.sendState(ServiceStates.FailedConnecting)
        }

        override fun onCreateConnection() {
            receiver.sendState(ServiceStates.CreateConnection)
            sync()
        }

        override fun onDestroyConnection() {
            receiver.sendState(ServiceStates.DestroyConnection)
        }

        override fun onReceiveBytes(buffer: ByteArray, bytes: Int) {
            val dataState = parser.parse(buffer, bytes)

            if (dataState != null) {
                dataState.loadingInfo.let {
                    Timber.e("Loading ${it.progress}/${it.size}")
                    if (dataState.data is ArrayLogData?) dataClassDisposing(it) //TODO
                }

                dataState.data?.let {
                    Timber.e("onReceiveData $it")
                    dataClassDisposing(it)
                }
            }
        }

    }

    //TODO
    private fun dataClassDisposing(data: RemoteData) {
        when (data) {
            is ArrayLogData -> {
                sensorsDao.insertAll(rangeDataMapper.map(data))
            }
            is StatusData -> {
                val lastStorageId = sensorsDao.getLastId() ?: 0
                val lastAqsId = data.lastLogId
                Timber.e("Status: lastStorageId: $lastStorageId, lastAqsId: $lastAqsId")
                if (lastAqsId == lastStorageId + 1) {
                    sensorsDao.insert(statusDataMapper.map(data))
                } else {
                    readLogs(lastStorageId + 1, lastAqsId)
                }
                //receiver.sendState(ServiceStates.OnReceiveData, data)
            }
            else -> {
                Timber.e("$data")
                receiver.sendState(ServiceStates.OnReceiveData, data)
            }
        }
    }


    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
        NotificationUtils.createNotificationCompat(this, NOTIFICATION_ID, CHANNEL_ID)
        instance = this
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }

}

