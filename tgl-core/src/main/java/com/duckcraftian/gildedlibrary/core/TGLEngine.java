package com.duckcraftian.gildedlibrary.core;

import com.duckcraftian.gildedlibrary.api.system.Builder;
import com.duckcraftian.gildedlibrary.api.system.IDisposable;
import com.duckcraftian.gildedlibrary.api.system.engine.EngineMode;
import com.duckcraftian.gildedlibrary.api.system.engine.IEngine;
import com.duckcraftian.gildedlibrary.api.system.engine.IEngineContext;
import com.duckcraftian.gildedlibrary.api.system.engine.SubsystemType;
import com.duckcraftian.gildedlibrary.api.system.event.EventBus;
import com.duckcraftian.gildedlibrary.api.system.event.RegisteredListener;
import com.duckcraftian.gildedlibrary.api.system.gfx.RenderBackend;
import com.duckcraftian.gildedlibrary.api.system.gfx.WindowBackend;
import com.duckcraftian.gildedlibrary.api.system.registries.RecordRegistry;
import com.duckcraftian.gildedlibrary.api.system.registries.RegistryManager;
import com.duckcraftian.gildedlibrary.api.system.vfs.IVFS;
import com.duckcraftian.gildedlibrary.core.system.mods.ModLoader;
import com.duckcraftian.gildedlibrary.core.system.mods.PluginLoader;
import com.duckcraftian.gildedlibrary.core.system.records.ItemRecord;
import com.duckcraftian.gildedlibrary.core.system.records.WeaponRecord;
import com.duckcraftian.gildedlibrary.core.system.vfs.VirtualFileSystem;
import org.tinylog.Logger;

import java.io.IOException;

public class TGLEngine implements IEngine, IDisposable {

    private final EngineMode engineMode;
    private final RegistryManager registryManager;
    private final VirtualFileSystem vfs;
    private final PluginLoader pluginLoader;
    private final ModLoader modLoader;
    private final EventBus eventBus;
    private final WindowBackend windowBackend;

    private final RenderBackend renderBackend;
    private final IEngineContext engineContext;

    private boolean isRunning = false;

    public TGLEngine(EngineBuilder builder) {
        if (!TGL.ENGINE_PATH.toFile().exists()) {
            TGL.createDataFolder();
        }

        this.engineContext = builder.engineContext;
        this.engineMode = this.engineContext.getMode();
        this.windowBackend = builder.windowBackend;
        this.renderBackend = builder.renderBackend;

        this.registryManager = new RegistryManager();
        TGL.REGISTRY_MANAGER = registryManager;

        this.vfs = new VirtualFileSystem();
        this.eventBus = new EventBus();

        // Create the default registries
        registryManager.addRecordRegistry("items", new RecordRegistry<ItemRecord>("items"));
        registryManager.addRecordRegistry("weapons", new RecordRegistry<WeaponRecord>("weapons"));

        // TODO Replace `AbstractRecord` with the appropriate concrete Record type
        registryManager.addRecordRegistry("armors", new RecordRegistry<>("armors"));
        registryManager.addRecordRegistry("clothing", new RecordRegistry<>("clothing"));
        registryManager.addRecordRegistry("races", new RecordRegistry<>("races"));
        registryManager.addRecordRegistry("npcs", new RecordRegistry<>("npcs"));
        registryManager.addRecordRegistry("creatures", new RecordRegistry<>("creatures"));
        registryManager.addRecordRegistry("quests", new RecordRegistry<>("quests"));
        registryManager.addRecordRegistry("dialogue", new RecordRegistry<>("dialogue"));
        registryManager.addRecordRegistry("factions", new RecordRegistry<>("factions"));
        registryManager.addRecordRegistry("skills", new RecordRegistry<>("skills"));
        registryManager.addRecordRegistry("attributes", new RecordRegistry<>("attributes"));
        registryManager.addRecordRegistry("abilities", new RecordRegistry<>("abilities"));
        registryManager.addRecordRegistry("containers", new RecordRegistry<>("containers"));


        // TODO Add the other Serializers when they're created
        registryManager.addSerializerRegistry("items", new ItemRecord.ItemSerializer());
        registryManager.addSerializerRegistry("weapons", new WeaponRecord.WeaponSerializer());

        this.pluginLoader = new PluginLoader(TGL.getEngineFolder("plugins"), registryManager);
        this.modLoader = new ModLoader(TGL.getEngineFolder("mods"), registryManager, vfs);
    }

    public void run() {
        init();

        if (engineMode.hasGameLoop())
            loop();

        onShutdown();
    }

    private void init() {


        if (engineMode.requires(SubsystemType.RENDERING)) {
            // Initialize Render Backend
            windowBackend.onInitialize();
            renderBackend.setWindow(windowBackend.getWindowPointer());
            renderBackend.onInitialize();
        }

        // Initialize Plugin Loader
        pluginLoader.loadPlugins();

        // Initialize Mod Loader
        modLoader.loadMods();

        // Post Initialize Plugin Loader
        pluginLoader.postInitializePlugins();

        engineContext.onEngineReady(this);
    }

    public void loop() {
        isRunning = true;

        long previousTime = System.nanoTime();
        while (isRunning) {
            long currentTime = System.nanoTime();
            float delta = (currentTime - previousTime) / 1E9f;
            previousTime = currentTime;

            // Handle Events
            if (engineMode.requires(SubsystemType.RENDERING) && engineMode.requires(SubsystemType.INPUT))
                windowBackend.handleEvents(this);

            // Update
            engineContext.onUpdate(delta);

            // Render
            if (engineMode.requires(SubsystemType.RENDERING)) {
                renderBackend.clearScreen();
                engineContext.onRender();
                renderBackend.swapBuffers();
            }
        }

    }

    @Override
    public void shouldShutdown() {
        isRunning = false;
    }

    @Override
    public void onShutdown() {
        engineContext.onShutdown();
        onDispose();
    }

    @Override
    public void onDispose() {
        Logger.info("Disposing");
        pluginLoader.disposePlugins();

        // Try and Close the VFS Mounts
        vfs.getArchiveMounts().forEach(mount -> {
            try {
                mount.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        if (engineMode.requires(SubsystemType.RENDERING))
            renderBackend.onDispose();

        if (engineMode.hasGameLoop() && engineMode.requires(SubsystemType.RENDERING))
            windowBackend.onDispose();
    }

    public static class EngineBuilder extends Builder<EngineBuilder, TGLEngine> {

        private RenderBackend renderBackend;
        private WindowBackend windowBackend;
        private IEngineContext engineContext;

        public EngineBuilder engineContext(IEngineContext engineContext) {
            this.engineContext = engineContext;
            return self();
        }

        public EngineBuilder renderBackend(RenderBackend renderBackend) {
            this.renderBackend = renderBackend;
            return self();
        }

        public EngineBuilder windowBackend(WindowBackend windowBackend) {
            this.windowBackend = windowBackend;
            return self();
        }

        @Override
        public EngineBuilder self() {
            return this;
        }

        @Override
        public TGLEngine build() {
            return new TGLEngine(this);
        }
    }

    @Override
    public EngineMode getMode() {
        return engineMode;
    }

    @Override
    public RegistryManager getRegistryManager() {
        return registryManager;
    }

    public IVFS getVFS() {
        return vfs;
    }

    @Override
    public EventBus getEventBus() {
        return eventBus;
    }

    public PluginLoader getPluginLoader() {
        return pluginLoader;
    }

    public ModLoader getModLoader() {
        return modLoader;
    }

    public WindowBackend getWindowBackend() {
        return windowBackend;
    }

    @Override
    public RenderBackend getRenderBackend() {
        return renderBackend;
    }

    public boolean isRunning() {
        return isRunning;
    }
}
