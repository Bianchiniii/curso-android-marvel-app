package com.exemple.testing.model

import com.bianchini.vinicius.matheus.core.domain.model.Event

class EventFactory {

    fun create(event: FakeEvent) = when (event) {
        FakeEvent.FakeEvent1 -> Event(
            1,
            "http://fakeurl.jpg"
        )
    }

    sealed class FakeEvent {
        object FakeEvent1 : FakeEvent()
    }
}