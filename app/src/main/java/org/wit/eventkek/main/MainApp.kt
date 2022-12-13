package org.wit.eventkek.main

import android.app.Application
import org.wit.eventkek.models.EventMemStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    //val events = ArrayList<EventModel>()
    val events = EventMemStore()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i("Event started")
//        events.add(EventModel("One", "About one..."))
//        events.add(EventModel("Two", "About two..."))
//        events.add(EventModel("Three", "About three..."))
    }
}