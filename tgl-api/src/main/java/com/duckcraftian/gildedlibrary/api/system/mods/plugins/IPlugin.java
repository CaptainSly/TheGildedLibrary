package com.duckcraftian.gildedlibrary.api.system.mods.plugins;

import com.duckcraftian.gildedlibrary.api.system.IDisposable;
import com.duckcraftian.gildedlibrary.api.system.editor.EditorContext;

public interface IPlugin extends IDisposable {

    void onInitialize();

    void onPreInitialize(PluginContext context);

    void onPostInitialize();

    void onEnable();

    void onEditorInitialize();

    void onEditorPreInitialize(EditorContext context);

    void onEditorPostInitialize();

    boolean canDisable();

    void onDisable();

    void onShutdown();

}
