package org.wit.eventkek.models

interface EventStore {
    fun findAll(): List<EventModel>
    fun create(event: EventModel)
    fun update(event: EventModel)
}