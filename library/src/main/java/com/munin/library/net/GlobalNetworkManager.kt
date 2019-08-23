package com.munin.library.net

class GlobalNetworkManager private constructor(){

    private object Holder{
        val INSTANCE= GlobalNetworkManager()
    }
    companion object{
        val instance by lazy { Holder.INSTANCE }
    }
    val tag = "GlobalNetworkManager"
    var observerLists = ArrayList<NetworkObserver>()


    fun register(observer: NetworkObserver) {
        observerLists.add(observer)
    }

    fun unRegister(observer: NetworkObserver) {
        observerLists.remove(observer)
    }

    fun notifyNetworkChange() {
        observerLists.forEach {
            it.onNetworkChange()
        }
    }

    interface NetworkObserver {
        fun onNetworkChange()
    }
}