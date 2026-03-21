package com.duckcraftian.gildedlibrary.api.system.event;

@FunctionalInterface
public interface EventListener<E extends Event> {

     void onEvent(E event);

}
