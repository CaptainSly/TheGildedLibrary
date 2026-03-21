package com.duckcraftian.gildedlibrary.core.system.event;

import com.duckcraftian.gildedlibrary.api.system.event.EventBus;
import com.duckcraftian.gildedlibrary.api.system.event.EventListener;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EventBusTest {

    private static final EventBus eBus = new EventBus();


    @Test
    void testListenerSubscription() {
        boolean[] called = {false};

        eBus.subscribe(TestEvent.class, _ -> called[0] = true, 0, false);
        eBus.fire(new TestEvent());

        assertTrue(called[0]);
    }

    @Test
    void testListenerPriority() {
        List<Integer> order = new ArrayList<>();

        eBus.subscribe(TestEvent.class, event -> order.add(1), 1, false);
        eBus.subscribe(TestEvent.class, event -> order.add(10), 10, false);
        eBus.fire(new TestEvent());

        assertEquals(10, order.get(0));
        assertEquals(1, order.get(1));
    }

    @Test
    void testEventCancellation() {
        boolean[] secondCalled = {false};

        eBus.subscribe(TestEventCancellable.class, event -> event.setCancelled(true), 10, false);
        eBus.subscribe(TestEventCancellable.class, event -> secondCalled[0] = true, 1, false);
        eBus.fire(new TestEventCancellable());

        assertFalse(secondCalled[0]);
    }

    @Test
    void testIgnoreCancelledListener() {
        boolean[] secondCalled = {false};

        eBus.subscribe(TestEventCancellable.class, event -> event.setCancelled(true), 10, false);
        eBus.subscribe(TestEventCancellable.class, event -> secondCalled[0] = true, 1, true);
        eBus.fire(new TestEventCancellable());

        assertTrue(secondCalled[0]);
    }

    @Test
    void testListenerUnsubscribe() {
        boolean[] called = {false};

        EventListener<TestEvent> listener = event -> called[0] = true;
        eBus.subscribe(TestEvent.class, listener, 0, false);
        eBus.unsubscribe(TestEvent.class, listener);
        eBus.fire(new TestEvent());

        assertFalse(called[0]);
    }

    @Test
    void testUnsubscribeWithNoListeners() {
        assertDoesNotThrow(() -> eBus.unsubscribe(TestEvent.class, event -> {
        }));
    }

}
