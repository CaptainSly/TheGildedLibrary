package com.duckcraftian.gildedlibrary.core;

import com.duckcraftian.gildedlibrary.api.assets.RenderBackend;
import com.duckcraftian.gildedlibrary.api.system.Builder;
import com.duckcraftian.gildedlibrary.api.system.IDisposable;
import com.duckcraftian.gildedlibrary.api.system.registries.Registry;
import com.duckcraftian.gildedlibrary.api.system.registries.RegistryManager;
import com.duckcraftian.gildedlibrary.core.system.EngineMode;
import com.duckcraftian.gildedlibrary.core.system.Game;
import com.duckcraftian.gildedlibrary.core.system.render.Window;
import com.duckcraftian.gildedlibrary.core.system.mods.PluginLoader;
import com.duckcraftian.gildedlibrary.core.system.records.ItemRecord;
import com.duckcraftian.gildedlibrary.core.system.records.WeaponRecord;
import com.duckcraftian.gildedlibrary.core.system.render.GLBackend;
import org.lwjgl.sdl.*;

import static org.lwjgl.sdl.SDLInit.SDL_INIT_VIDEO;
import static org.lwjgl.sdl.SDLInit.SDL_Init;

public class TGLEngine implements IDisposable {

    private EngineMode engineMode;
    private RegistryManager registryManager;
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
        this.game = new Game();

        // Create the default registries
        registryManager.addRegistry("weapons", new Registry<WeaponRecord>());
        registryManager.addRegistry("item", new Registry<ItemRecord>());

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
        IO.println("Disposing");
        pluginLoader.disposePlugins();
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

    public static void main(String[] args) {
        TGLEngine e = new EngineBuilder().engineMode(EngineMode.GAME).renderBackend(new GLBackend()).build();
        e.run();
    }

}
