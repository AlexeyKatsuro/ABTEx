package com.e.btex.data

enum class ServiceStates {
    StartConnecting,
    FailedConnecting,
    CreateConnection,
    DestroyConnection,
    OnReceiveData
}