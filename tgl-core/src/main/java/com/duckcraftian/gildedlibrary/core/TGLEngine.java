package com.duckcraftian.gildedlibrary.core;

import com.duckcraftian.gildedlibrary.api.assets.RenderBackend;
import com.duckcraftian.gildedlibrary.api.system.Builder;
import com.duckcraftian.gildedlibrary.api.system.IDisposable;
import com.duckcraftian.gildedlibrary.api.system.records.AbstractRecord;
import com.duckcraftian.gildedlibrary.api.system.registries.RecordRegistry;
import com.duckcraftian.gildedlibrary.api.system.registries.RegistryManager;
import com.duckcraftian.gildedlibrary.core.system.EngineMode;
import com.duckcraftian.gildedlibrary.core.system.Game;
import com.duckcraftian.gildedlibrary.core.system.render.Window;
import com.duckcraftian.gildedlibrary.core.system.mods.PluginLoader;
import com.duckcraftian.gildedlibrary.core.system.records.ItemRecord;
import com.duckcraftian.gildedlibrary.core.system.records.WeaponRecord;
import com.duckcraftian.gildedlibrary.core.system.render.GLBackend;
import com.duckcraftian.gildedlibrary.core.system.vfs.VirtualFileSystem;
import org.lwjgl.sdl.*;
import org.tinylog.Logger;

import java.io.IOException;

import static org.lwjgl.sdl.SDLInit.SDL_INIT_VIDEO;
import static org.lwjgl.sdl.SDLInit.SDL_Init;

public class TGLEngine implements IDisposable {

    private EngineMode engineMode;
    private RegistryManager registryManager;
    private VirtualFileSystem vfs;
    private PluginLoader pluginLoader;
    private Window window;

    private RenderBackend renderBackend;

    private Game game;

    private boolean isRunning = false;

    public TGLEngine(EngineBuilder builder) {
        if (!TGL.ENGINE_PATH.toFile().exists()) {
            TGL.createDataFolder();
        }

        this.engineMode = builder.engineMode;
        this.renderBackend = builder.renderBackend;

        this.registryManager = new RegistryManager();
        this.vfs = new VirtualFileSystem();
        this.game = new Game();

        // Create the default registries
        registryManager.addRecordRegistry("items", new RecordRegistry<ItemRecord>("items"));
        registryManager.addRecordRegistry("weapons", new RecordRegistry<WeaponRecord>("weapons"));

        // TODO Replace `AbstractRecord` with the appropriate concrete Record type
        registryManager.addRecordRegistry("armors", new RecordRegistry<AbstractRecord>("armors"));
        registryManager.addRecordRegistry("races", new RecordRegistry<AbstractRecord>("races"));
        registryManager.addRecordRegistry("npcs", new RecordRegistry<AbstractRecord>("npcs"));
        registryManager.addRecordRegistry("creatures", new RecordRegistry<AbstractRecord>("creatures"));
        registryManager.addRecordRegistry("quests", new RecordRegistry<AbstractRecord>("quests"));
        registryManager.addRecordRegistry("dialogue", new RecordRegistry<AbstractRecord>("dialogue"));
        registryManager.addRecordRegistry("factions", new RecordRegistry<AbstractRecord>("factions"));
        registryManager.addRecordRegistry("weather", new RecordRegistry<AbstractRecord>("weather"));
        registryManager.addRecordRegistry("cells", new RecordRegistry<AbstractRecord>("cells"));

        // TODO Add the other Serializers when they're created
        registryManager.addSerializerRegistry("items", new ItemRecord.ItemSerializer());
        registryManager.addSerializerRegistry("weapons", new WeaponRecord.WeaponSerializer());

        this.pluginLoader = new PluginLoader(TGL.getEngineFolder("plugins"), registryManager);
    }

    public void run() {
        init();
        loop();

        onDispose();
    }

    private void init() {
        // Initialize SDL3
        if (!SDL_Init(SDL_INIT_VIDEO))
            throw new RuntimeException("SDL3 could not initialize! SDL_ERROR: " + SDLError.SDL_GetError());

        window = new Window("The Gilded Library", 1280, 720);
        renderBackend.setWindow(window.getAddress());

        // Initialize Render Backend
        renderBackend.onInitialize();

        // Initialize Plugin Loader
        pluginLoader.loadPlugins();

        // Initialize Mod Loader
        //modLoader.loadMods();

        // Post Initialize Plugin Loader
        pluginLoader.postInitializePlugins();

        game.onInitialize();
    }

    private void loop() {
        isRunning = true;

        long previousTime = System.nanoTime();
        while (isRunning) {
            long currentTime = System.nanoTime();
            float delta = (currentTime - previousTime) / 1E9f;
            previousTime = currentTime;

            // Handle Events
            handleEvents();

            // Update
            game.update(delta);

            // Render
            renderBackend.clearScreen();
            game.render();
            renderBackend.swapBuffers();
        }

    }

    private void handleEvents() {

        SDL_Event event = SDL_Event.malloc();
        while (SDLEvents.SDL_PollEvent(event)) {

            if (event.type() == SDLEvents.SDL_EVENT_QUIT)
                isRunning = false;

            // Map Input

            // Map Controllers


        }
        event.free();
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

        renderBackend.onDispose();
        window.onDispose();
        SDLInit.SDL_Quit();
    }

    public static class EngineBuilder extends Builder<EngineBuilder, TGLEngine> {

        private EngineMode engineMode;
        private RenderBackend renderBackend;

        public EngineBuilder engineMode(EngineMode engineMode) {
            this.engineMode = engineMode;
            return self();
        }

        public EngineBuilder renderBackend(RenderBackend renderBackend) {
            this.renderBackend = renderBackend;
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

    static void main(String[] args) {
        TGLEngine e = new EngineBuilder()
                .engineMode(EngineMode.GAME)
                .renderBackend(new GLBackend())
                .build();

        e.run();
    }

}
