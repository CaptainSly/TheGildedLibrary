package com.duckcraftian.gildedlibrary.api.system.mods.plugins;

import com.duckcraftian.gildedlibrary.api.system.IDisposable;

public interface IPlugin extends IDisposable {
    void onInitialize(PluginContext context);

    void onPostInitialize();

    void onEnable();

    boolean canDisable();

    void onDisable();

    void onShutdown();
}
