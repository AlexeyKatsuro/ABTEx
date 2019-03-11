package com.e.btex.data

import com.e.btex.data.protocol.RemoteData

sealed class ServiceState {
    object OnStartConnecting : ServiceState()
    object OnFailedConnecting : ServiceState()
    object OnCreateConnection : ServiceState()
    object OnDestroyConnection : ServiceState()
    class OnReceiveData(val data: RemoteData) : ServiceState()
}