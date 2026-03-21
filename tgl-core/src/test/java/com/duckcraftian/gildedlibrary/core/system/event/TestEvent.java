package com.duckcraftian.gildedlibrary.core.system.event;

import com.duckcraftian.gildedlibrary.api.system.event.Event;

public class TestEvent extends Event {

    public TestEvent() {
        super("test_event", System.currentTimeMillis());
    }
}
