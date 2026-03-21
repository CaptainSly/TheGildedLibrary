package com.duckcraftian.gildedlibrary.core.system.event;

import com.duckcraftian.gildedlibrary.api.system.event.CancellableEvent;

public class TestEventCancellable extends CancellableEvent {

    public TestEventCancellable() {
        super("test_cancellable_event", System.currentTimeMillis());
    }
}
