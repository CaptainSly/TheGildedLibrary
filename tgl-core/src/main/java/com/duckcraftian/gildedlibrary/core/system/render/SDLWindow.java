package com.duckcraftian.gildedlibrary.core.system.render;

import com.duckcraftian.gildedlibrary.api.system.Pointer;
import com.duckcraftian.gildedlibrary.api.system.engine.IEngine;
import com.duckcraftian.gildedlibrary.api.system.gfx.WindowBackend;
import org.lwjgl.sdl.*;

import static org.lwjgl.sdl.SDLInit.SDL_INIT_VIDEO;
import static org.lwjgl.sdl.SDLInit.SDL_Init;

public class SDLWindow extends WindowBackend {

    public SDLWindow(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;
    }

    @Override
    public void onInitialize() {
        if (!SDL_Init(SDL_INIT_VIDEO))
            throw new RuntimeException("SDL3 could not initialize! SDL_ERROR: " + SDLError.SDL_GetError());

        windowPointer = new Pointer(SDLVideo.SDL_CreateWindow(title, width, height, SDLVideo.SDL_WINDOW_OPENGL));
        if (windowPointer.isNull())
            throw new RuntimeException("SDLWindow could not be created. SDL_ERROR: " + SDLError.SDL_GetError());

    }

    @Override
    public void handleEvents(IEngine engine) {
        SDL_Event event = SDL_Event.malloc();
        while (SDLEvents.SDL_PollEvent(event)) {

            if (event.type() == SDLEvents.SDL_EVENT_QUIT)
                engine.shouldShutdown();

            // Map Input

            // Map Controllers


        }
        event.free();
    }

    @Override
    public void onResize(int width, int height) {

    }

    public void setTitle(String title) {
        this.title = title;
        SDLVideo.SDL_SetWindowTitle(windowPointer.address(), title);
    }

    @Override
    public void onDispose() {
        SDLVideo.SDL_DestroyWindow(windowPointer.address());
        SDLInit.SDL_Quit();
    }

}
