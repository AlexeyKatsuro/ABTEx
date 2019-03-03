package com.e.btex.data

enum class ServiceState {
    OnStartConnecting,
    OnFailedConnecting,
    OnCreateConnection,
    OnDestroyConnection,
    OnReceiveData
}