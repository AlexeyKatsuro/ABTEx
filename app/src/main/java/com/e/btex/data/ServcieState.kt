package com.e.btex.data

enum class ServcieState {
    OnStartConnecting,
    OnFailedConnecting,
    OnCreateConnection,
    OnDestroyConnection,
    OnReceiveData
}