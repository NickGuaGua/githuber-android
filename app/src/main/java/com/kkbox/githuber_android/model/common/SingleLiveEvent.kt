package com.kkbox.githuber_android.model.common

import androidx.annotation.MainThread
import androidx.lifecycle.*

/**
 * A lifecycle-aware observable that sends only new updates after subscription, used for events like navigation and Snackbar messages.
 *
 * This avoids a common problem with events: on configuration change (like rotation) an update can be emitted if the observer is active.
 * This LiveData only calls the observable if there's an explicit call to setValue() or call().
 *
 * Note that only one observer is going to be notified of changes.
 *
 * https://proandroiddev.com/livedata-with-single-events-2395dea972a8
 */
class SingleLiveEvent<T> : MediatorLiveData<T>() {

    private val observers = mutableMapOf<Observer<in T>, ObserverWrapper<in T>>()

    override fun observeForever(observer: Observer<in T>) {
        val wrapper = ObserverWrapper(observer, null)
        observers[observer] = wrapper
        super.observeForever(wrapper)
    }

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        val wrapper = ObserverWrapper(observer, owner)
        observers[observer] = wrapper
        setLifecycleObserver(owner)
        super.observe(owner, wrapper)
    }

    @MainThread
    override fun removeObserver(observer: Observer<in T>) {
        observers[observer]?.let {
            observers.remove(observer)
            super.removeObserver(it)
        }
    }

    override fun removeObservers(owner: LifecycleOwner) {
        val remains = observers.filterValues { !it.isAttachTo(owner) }
        observers.clear()
        observers.putAll(remains)
        super.removeObservers(owner)
    }

    @MainThread
    override fun setValue(t: T?) {
        observers.values.forEach { it.newValue() }
        super.setValue(t)
    }

    private fun setLifecycleObserver(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                removeObservers(owner)
                owner.lifecycle.removeObserver(this)
            }
        })
    }

    private class ObserverWrapper<T>(val observer: Observer<T>, val lifecycleOwner: LifecycleOwner?) : Observer<T> {

        private var pending = false

        override fun onChanged(t: T?) {
            if (pending) {
                pending = false
                observer.onChanged(t)
            }
        }

        fun newValue() {
            pending = true
        }

        fun isAttachTo(lifecycleOwner: LifecycleOwner): Boolean = lifecycleOwner == this.lifecycleOwner
    }
}