package com.duckcraftian.gildedlibrary.api.system.engine;

public interface IEngineContext {

    EngineMode getMode();

    EngineConfig getConfig();

    void onEngineReady(IEngine engine);

    void onRender();

    void onUpdate(float deltaTime);

    void onShutdown();

}
