package com.duckcraftian.gildedlibrary.core;

import com.duckcraftian.gildedlibrary.api.system.Builder;
import com.duckcraftian.gildedlibrary.api.system.IDisposable;
import com.duckcraftian.gildedlibrary.api.system.registries.RegistryManager;
import com.duckcraftian.gildedlibrary.core.system.EngineMode;
import com.duckcraftian.gildedlibrary.core.system.Window;
import com.duckcraftian.gildedlibrary.core.system.mods.PluginLoader;
import org.lwjgl.sdl.*;

import java.util.Objects;

import static org.lwjgl.sdl.SDLInit.SDL_INIT_VIDEO;
import static org.lwjgl.sdl.SDLInit.SDL_Init;

public class TGLEngine implements IDisposable {

    private EngineMode engineMode;
    private RegistryManager registryManager;
    private PluginLoader pluginLoader;
    private Window window;

    public TGLEngine(EngineBuilder builder) {
        this.engineMode = builder.engineMode;
        this.registryManager = new RegistryManager();
        this.pluginLoader = new PluginLoader(null, registryManager);
    }

    public void run() {

        init();
        loop();

        onDispose();
    }

    private void init() {
        // Initialize SDL3
        if (!SDL_Init(SDL_INIT_VIDEO)) {
            throw new RuntimeException("SDL3 could not initialize! SDL_ERROR: " + SDLError.SDL_GetError());
        } else {
            window = new Window("The Gilded Library", 1280, 720);

            SDL_PixelFormatDetails formatDetails = SDLPixels.SDL_GetPixelFormatDetails(Objects.requireNonNull(window.getSurface()).format());
            SDLSurface.SDL_FillSurfaceRect(window.getSurface(), null, SDLPixels.SDL_MapRGB(Objects.requireNonNull(formatDetails), null, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF));
            SDLVideo.SDL_UpdateWindowSurface(window.getAddress().address());
        }

        // Initialize Render Backend

        // Initialize Plugin Loader
        pluginLoader.loadPlugins();

        // Initialize Mod Loader

        // Post Initialize Plugin Loader
        pluginLoader.postInitializePlugins();

    }

    private void loop() {

        try (SDL_Event e = SDL_Event.malloc()) {

            boolean quit = false;
            while (!quit) {
                while (SDLEvents.SDL_PollEvent(e)) {

                    if (e.type() == SDLEvents.SDL_EVENT_QUIT)
                        quit = true;

                }
            }
        }

    }

    @Override
    public void onDispose() {
        window.onDispose();
        SDLInit.SDL_Quit();
    }

    public static class EngineBuilder extends Builder<EngineBuilder, TGLEngine> {

        private EngineMode engineMode;

        public EngineBuilder engineMode(EngineMode engineMode) {
            this.engineMode = engineMode;
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
        TGLEngine e = new EngineBuilder().engineMode(EngineMode.GAME).build();
        e.run();
    }

}
