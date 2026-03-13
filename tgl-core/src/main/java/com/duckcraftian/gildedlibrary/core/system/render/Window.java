package com.duckcraftian.gildedlibrary.core.system.render;

import com.duckcraftian.gildedlibrary.api.system.IDisposable;
import com.duckcraftian.gildedlibrary.api.system.Pointer;
import org.lwjgl.sdl.SDLError;
import org.lwjgl.sdl.SDLVideo;

public class Window implements IDisposable {

    private String title;
    private int width, height;

    private final Pointer address;

    public Window(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;

        address = new Pointer(SDLVideo.SDL_CreateWindow(title, width, height, SDLVideo.SDL_WINDOW_OPENGL));
        if (address.isNull())
            throw new RuntimeException("Window could not be created. SDL_ERROR: " + SDLError.SDL_GetError());
    }

    public void setTitle(String title) {
        this.title = title;
        SDLVideo.SDL_SetWindowTitle(address.address(), title);
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

    @Override
    public void onDispose() {
        SDLVideo.SDL_DestroyWindow(getAddress().address());
    }
}
