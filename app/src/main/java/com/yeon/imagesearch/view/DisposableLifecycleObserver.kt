package com.yeon.imagesearch.view

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class DisposableLifecycleObserver(private var lifecycle: Lifecycle) : LifecycleObserver {

    private var enabled = false
    private var mNetWorkDisposable = CompositeDisposable()
    private val mDisposable = CompositeDisposable()
    private var saveDisposableMap = HashMap<String, Disposable>()

    init {
        lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        if (enabled) {
            mNetWorkDisposable = CompositeDisposable()
            saveDisposableMap = HashMap()
        }
    }

    fun enable() {
        enabled = true
    }

    fun putDisposableMap(tag: String, disposable: Disposable) {
        if (saveDisposableMap[tag] == null) {
            mNetWorkDisposable.add(disposable)
            saveDisposableMap[tag] = disposable
        } else {
            mNetWorkDisposable.remove(saveDisposableMap[tag]!!)
            saveDisposableMap.remove(tag)
            saveDisposableMap[tag] = disposable
            mNetWorkDisposable.add(disposable)
        }
    }

    fun removeDisposable(tag: String) {
        if (saveDisposableMap[tag] != null) {
            saveDisposableMap[tag]?.dispose()
            mNetWorkDisposable.remove(saveDisposableMap[tag]!!)
            saveDisposableMap.remove(tag)
        }
    }

    fun getDisposable(): CompositeDisposable {
        return mDisposable
    }

    fun getNetWorkDisposable(): CompositeDisposable {
        return mNetWorkDisposable
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        saveDisposableMap.clear()
        mNetWorkDisposable.dispose()
        mNetWorkDisposable.clear()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun destroy() {
        lifecycle.removeObserver(this)
        mDisposable.dispose()
        mDisposable.clear()
    }
}

