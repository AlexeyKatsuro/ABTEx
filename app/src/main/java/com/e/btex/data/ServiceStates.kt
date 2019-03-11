package com.e.btex.data

enum class ServiceStates {
    OnStartConnecting,
    OnFailedConnecting,
    OnCreateConnection,
    OnDestroyConnection,
    OnReceiveData
}