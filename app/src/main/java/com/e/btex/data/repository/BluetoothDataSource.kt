package com.e.btex.data.repository

import android.bluetooth.BluetoothAdapter
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.e.btex.connection.BleService
import com.e.btex.connection.ServiceStateCallback
import com.e.btex.data.BtDevice
import com.e.btex.data.ServiceState
import com.e.btex.data.entity.Sensors
import com.e.btex.data.entity.StatusData
import com.e.btex.data.mapToBtDevice
import com.e.btex.data.mappers.SensorsMapper
import com.e.btex.data.protocol.RemoteData
import com.e.btex.util.Event
import com.e.btex.util.EventObserver
import com.e.btex.util.extensions.postEventValue
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BluetoothDataSource @Inject constructor(
    private val context: Context,
    private val sensorsMapper: SensorsMapper
) {

    private val state = MutableLiveData<Event<ServiceState>>()

    private val bleAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    val callback = object : ServiceStateCallback {
        override fun onStartConnecting() {
            Timber.d("onStartConnecting")
            state.postEventValue(ServiceState.OnStartConnecting)
        }

        override fun onFailedConnecting() {
            Timber.d("onFailedConnecting")
            state.postEventValue(ServiceState.OnFailedConnecting)
        }

        override fun onCreateConnection() {
            Timber.d("onCreateConnection")
            state.postEventValue(ServiceState.OnCreateConnection)
        }

        override fun onDestroyConnection() {
            Timber.d("onDestroyConnection")
            state.postEventValue(ServiceState.OnDestroyConnection)
        }

        override fun onReceiveData(data: RemoteData) {
            Timber.d("onReceiveData")
            state.postEventValue(ServiceState.OnReceiveData(data))
        }

    }

    fun getBtDevice(address: String): BtDevice{
        return bleAdapter.getRemoteDevice(address).mapToBtDevice()
    }

    fun initConnection(device: BtDevice): LiveData<Event<ServiceState>> {
        if (BleService.instance == null) {
            BleService.startService(context, device, callback)
        }
        return state
    }

    fun closeConnection(){
        BleService.instance?.stopSelf()
    }

    fun getLastSensors():  LiveData<Sensors>{
        return MediatorLiveData<Sensors>().apply {
            addSource(state) {
                val state = it.content
                if (state is ServiceState.OnReceiveData && state.data is StatusData){
                    val sensors: Sensors = sensorsMapper.map(state.data.sensors)
                    this.postValue(sensors)
                }
            }
        }
    }
}