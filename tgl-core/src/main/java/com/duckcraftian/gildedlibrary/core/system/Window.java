package com.duckcraftian.gildedlibrary.core.system;

import com.duckcraftian.gildedlibrary.api.system.IDisposable;
import com.duckcraftian.gildedlibrary.core.system.utils.Pointer;
import org.lwjgl.sdl.SDLError;
import org.lwjgl.sdl.SDLVideo;
import org.lwjgl.sdl.SDL_Surface;

public class Window implements IDisposable {

    private String title;
    private int width, height;

    private final Pointer address;
    private final SDL_Surface surface;

    public Window(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;

        address = new Pointer(SDLVideo.SDL_CreateWindow(title, width, height, SDLVideo.SDL_WINDOW_OPENGL));
        if (address.isNull())
            throw new RuntimeException("Window could not be created. SDL_ERROR: " + SDLError.SDL_GetError());

        surface = SDLVideo.SDL_GetWindowSurface(address.address());
    }

    public String getTitle() {
        return title;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Pointer getAddress() {
        return address;
    }

    public SDL_Surface getSurface() {
        return surface;
    }

    @Override
    public void onDispose() {
        SDLVideo.SDL_DestroyWindow(getAddress().address());
    }
}
