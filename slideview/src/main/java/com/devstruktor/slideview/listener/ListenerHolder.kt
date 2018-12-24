package com.devstruktor.slideview.listener

import java.util.*
import kotlin.collections.HashMap

class ListenerHolder<T>() {


    var listeners: HashMap<String, T> = HashMap()
        private set

    fun addListener(listener: T, tag: String) {
        listeners[tag] = listener
    }

    fun removeListener(tag: String) {
        listeners.remove(tag)
    }

}
