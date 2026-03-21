package com.duckcraftian.gildedlibrary.api.system.engine;

import com.duckcraftian.gildedlibrary.api.system.gfx.RenderBackend;
import com.duckcraftian.gildedlibrary.api.system.event.EventBus;
import com.duckcraftian.gildedlibrary.api.system.gfx.WindowBackend;
import com.duckcraftian.gildedlibrary.api.system.registries.RegistryManager;
import com.duckcraftian.gildedlibrary.api.system.vfs.IVFS;

public interface IEngine {

    EngineMode getMode();

    RegistryManager getRegistryManager();

    IVFS getVFS();

    EventBus getEventBus();

    RenderBackend getRenderBackend();

    WindowBackend getWindowBackend();

    void shouldShutdown();

    void onShutdown();

}
