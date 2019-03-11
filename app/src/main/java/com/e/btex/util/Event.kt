package com.e.btex.util

/**
 * Used as a wrapper for data that is exposed via a LiveData that represents an event.
 */
open class Event<out T>(val content: T) {
    var hasBeenHandled = false
        private set

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T{
        hasBeenHandled = true
        return content
    }
}
