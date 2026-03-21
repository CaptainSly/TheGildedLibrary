package com.duckcraftian.gildedlibrary.api.system.event;

public record RegisteredListener(int priority, EventListener<? extends Event> listener, boolean ignoreCancelled) {


}
